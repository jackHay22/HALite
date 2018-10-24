package system_utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

import system_utils.io_tools.CSVParser;
import system_utils.io_tools.TestSuiteReader;

import java.awt.Color;
import ui_graphlib.PointSet;
import ui_stdlib.SystemThemes;
import ui_graphlib.Point;

public class DataStore extends ui_framework.StateResult implements Serializable {
	private ui_framework.SystemWindow window_parent;
	
	private String save_path;
	
	private Element primary;
	private Element secondary;
	
	private Element model_data_element = Element.Hf;
	
	private DataTable xrf_data;
	private DataTable standards_data;
	private DataTable means_data;
	
	private HashMap<Element, ElementCorrelationInfo> correlations;
	
	private int elem_num;

	private static final long serialVersionUID = 1L;

	public DataStore(ui_framework.SystemWindow window_parent) {
		this.window_parent = window_parent;
		
		this.save_path = "";
		
		this.xrf_data = new DataTable();
		this.standards_data = new DataTable();
		this.means_data = new DataTable();
		
		this.correlations = new HashMap<Element, ElementCorrelationInfo>();
		
		this.elem_num = 5;
	}
	
	public void set_save_path(String path) {
		this.save_path = path;
	}
	
	public String get_path() {
		return this.save_path;
	}
	
	public boolean path_assigned() {
		return this.save_path.isEmpty();
	}
	
	public boolean check_valid_target() {
		File save_file = new File(this.save_path);
		String parent_path = save_file.getParent();
		
		if (parent_path == null) {
			return false;
		}
		else {
			File parent_dir = new File(parent_path);
			return parent_dir.exists();
		}
	}
	
	public boolean load_from_save(File path) {
		//TODO
		//try to load from save file, return true if successful
		return false;
	}
	
	private void internal_refresh() {
		for (ElementCorrelationInfo elem_corr: correlations.values()) {
			elem_corr.refresh();
		}
	}
	
	private ArrayList<Double> calculate_coords(Element elem, Boolean stand_points) {
		
		ArrayList<Double> means;
		
		// If it contains the element EXACTLY
		// FIX ME
		// This needs to parse to see if the strings are contained
		if (means_data.contains_data(new TableKey(elem.name()))) {
			means = means_data.get_data(new TableKey(elem.name())).get_data();
		} else {
			return null;
		}
		
		// Get listing of standards and unknowns from means file
		TableKey source_key = new TableKey("sourcefile");
		ArrayList<String> source = means_data.get_info(source_key);
		
		ArrayList<String> names;
		
		if (stand_points) {
			// Get listing of standards from standards file
			TableKey value_key = new TableKey("Calibrationvalues");
			names = standards_data.get_info(value_key);
		}
		else {
			// Get listing of standards from standards file
			TableKey xrf_key = new TableKey("Name");
			names = xrf_data.get_info(xrf_key);
		}
		
		//System.out.println(standards_data.contains_data(new TableKey(elem.name())));
		Data temp_stds = standards_data.get_data(new TableKey(elem.name()));
		ArrayList<Double> standards;
		
		if (temp_stds == null) {
			standards = new ArrayList<Double>();
		} else {
			standards = temp_stds.get_data();
		}
		
		Data temp_xrf = xrf_data.get_data(new TableKey(elem.name()));
		ArrayList<Double> xrf;
		
		// If we have no data to calculate coords with, return an empty arraylist
		if (temp_xrf == null) {
			xrf = new ArrayList<Double>();
		}
		else {
			xrf = temp_xrf.get_data();
		}
		
		ArrayList<Double> coords = new ArrayList<Double>();
		
		// Define starting positions for unknowns/standards
		ArrayList<String> note_vals = this.means_data.get_info(new TableKey("note"));
		
		int start_index;
		int end_index;
		
		if (stand_points) {
			start_index = note_vals.indexOf("standard");
			end_index = note_vals.lastIndexOf("standard");
		}
		else {
			start_index = note_vals.indexOf("unknown");
			end_index = note_vals.lastIndexOf("unknown");
		}
		
		// Get table values from means and standards and calculate coordinates
		for (int i = start_index; i < end_index; i++) {
			
			if (means.get(i) == null) {
				continue;
			}
			double elem_cps = means.get(i);
			
			String source_id = source.get(i);
			int pos = names.indexOf(source_id);
			if (stand_points) {
				if (pos < 0 || standards.size() == 0) {
					continue;
				}
				if (standards.get(pos) == null) {
					continue;
				}
				else {
					double elem_standard = standards.get(pos);
					coords.add(elem_cps / elem_standard);
				}
			}
			else {
				if (xrf.isEmpty() || xrf.get(pos) == null) {
					continue;
				}
				else {
					double elem_xrf = xrf.get(i);
					coords.add(elem_cps / elem_xrf);
				}
			}
		}
		
		return coords;
	}
	
	private PointSet create_pointset(Element y_elem, Element x_elem, Boolean standards) {
		String x_axis = x_elem.name();
		String y_axis = y_elem.name();
		String title = x_axis + " vs. " + y_axis;
		Color color = new Color(1, 1, 1);
		boolean render = true;
		
		ArrayList<Point> points = new ArrayList<Point>();
		
		ArrayList<Double> x_coords = calculate_coords(x_elem, standards);
		ArrayList<Double> y_coords = calculate_coords(y_elem, standards);
		
		if (x_coords == null || y_coords == null) {
			return null;
		}
		
		// Combine coordinates into points ArrayList
		for (int i = 0; i < x_coords.size(); i++) {
			if (i < x_coords.size() && i < y_coords.size()) {
				Point point = new Point(x_coords.get(i), y_coords.get(i));
				
				points.add(point);
			}
		}
		
	
		// Create point set from coordinates
		PointSet set = new PointSet(points, color, x_axis, y_axis, title, render);
		
		return set;
	}
	
	private void create_element_correlations() {
		
		// Listing of all elements
		ArrayList<Element> elements = new ArrayList<Element>(Arrays.asList(Element.values()));
		
		// Make element correlations for all elements
		for (int i = 0; i < elements.size(); i++) {
			Element x_elem = elements.get(i);
			HashMap<Element, CorrelationInfo> elem_corr = new HashMap<Element, CorrelationInfo>();
			
			for (int j = 0; j < elements.size(); j++) {
				Element y_elem = elements.get(j);
				
				PointSet standards = create_pointset(x_elem, y_elem, true);
				PointSet unknowns = create_pointset(x_elem, y_elem, false);

				if (standards != null) {
					
					standards.set_color(SystemThemes.HIGHLIGHT);
					unknowns.set_color(SystemThemes.BACKGROUND);
					
					ElementPair pair = new ElementPair(x_elem, y_elem, standards, unknowns);
					
					CorrelationInfo corr_info = new CorrelationInfo(pair);
					
					elem_corr.put(y_elem, corr_info);
					
				} else {
					elem_corr.put(y_elem, null);
				}
				
				
			}
			
			// Create new element correlation info object
			ElementCorrelationInfo elem_info = new ElementCorrelationInfo(x_elem, elem_corr);
			elem_info.set_datastore(this);
			
			// Save correlations to object
			this.correlations.put(x_elem, elem_info);
		}
	}
	
	public void import_data(String xrf, ArrayList<String> xrf_table, String calibration, ArrayList<String> calibration_table, 
			String means, ArrayList<String> means_table) throws FileNotFoundException {
		CSVParser parser = new CSVParser();
		
		// Collect all imported data sets
		this.xrf_data = parser.data_from_csv(xrf_table.get(0), new BufferedReader(new FileReader(xrf)));
		this.standards_data = parser.data_from_csv(calibration_table.get(0), new BufferedReader(new FileReader(calibration)));
		this.means_data = parser.data_from_csv(means_table.get(0), new BufferedReader(new FileReader(means)));
		
		create_element_correlations();
		
	}
	
	public void import_test_data(String xrf, String stds, String means) throws FileNotFoundException {
		CSVParser parser = new CSVParser();
		
		TestSuiteReader test_reader = new TestSuiteReader();
		
		// Collect all imported data sets
		this.xrf_data = parser.data_from_csv("XRF_DATA_RUN_229", test_reader.get_resources_input(xrf));
		this.standards_data = parser.data_from_csv("standards", test_reader.get_resources_input(stds));
		this.means_data = parser.data_from_csv("means", test_reader.get_resources_input(means));
		
		create_element_correlations();
	}
	
	public Double get_mean_value(String sample, Element elem) {
		int pos = means_data.get_info(new TableKey("sourcefile")).indexOf(sample);
		if (pos >= 0) {
			return means_data.get_data(elem).get_data(pos);
		}
		return null;
	}
	
	public void set_model_data_element(Element elem) {
		this.model_data_element = elem;
		this.notify_update();
	}
	
	public ElementCorrelationInfo get_model_data_corr() {
		return this.correlations.get(this.model_data_element);
	}
	
	public CorrelationInfo get_elem_correlation_info(Element x, Element y) {
		ElementCorrelationInfo x_correlations = this.correlations.get(x);
		
		return x_correlations.get_corr(y);
	}
	
	public CorrelationInfo get_correlation_info() {
		
		if (this.primary != null && this.secondary != null) {
			ElementCorrelationInfo elem_corr_info = this.correlations.get(this.primary);
			CorrelationInfo corr = elem_corr_info.get_corr(this.secondary);
			return corr;
		}
		
		ElementCorrelationInfo elem_corr_info = this.correlations.get(Element.Hf);
		
		CorrelationInfo corr = elem_corr_info.get_corr(Element.Si);
		
		return corr;
	}
	
	public ArrayList<String> get_STDlist() {
		return standards_data.get_info(new TableKey("Calibrationvalues"));
	}
	
	public Double get_raw_std_elem(String standard, Element elem) {
		Data elem_data = this.standards_data.get_data(elem);
		if (elem_data == null) {
			return null;
		}
		
		// Integer object so it can be tested for null
		Integer pos = standards_data.get_info(new TableKey("Calibrationvalues")).indexOf(standard);
		if (pos.equals(-1)) {
			return null;
		}
		
		Double data = elem_data.get_data().get(pos);
		if (data != null) {
			return data;
		} else {
			return null;
		}
	}
	
	public void set_elem_num(Integer num) {
		this.elem_num = num;
		
		notify_update();
	}
	
	public int get_elem_num() {
		return this.elem_num;
	}
	
	public ArrayList<Element> get_WM_header() {
		return this.correlations.get(this.model_data_element).get_selected_names();
	}
	
	public HashMap<String, HashMap<String, Double>> get_WM_data() {
		return this.correlations.get(this.model_data_element).get_WM_panel_data();
	}
	
	public Double get_current_WM(String std) {
		return this.get_WM_data().get(std).get("WM");
	}
	
	public Double get_current_actual(String std) {
		return this.get_WM_data().get(std).get("Actual");
	}
	
	public Double get_element_std_pair(String std, Element elem) {
		return this.get_WM_data().get(std).get(elem.toString());
	}
	
	public ArrayList<Pair> get_rsqrd_assoc_list(Element elem) {
		ArrayList<Pair> pairs = new ArrayList<Pair>();
		int elem_num = this.elem_num;
		
		ElementCorrelationInfo elem_corr = this.correlations.get(elem);
		HashMap<Element, CorrelationInfo> all_corr = elem_corr.get_all_corr();
		
		// Listing of all elements
		ArrayList<Element> elements = new ArrayList<Element>(Arrays.asList(Element.values()));
		
		// Remove elem we are currently comparing to
		elements.remove(elem);
		
		for (int i = 0; i < elements.size(); i++) {
			CorrelationInfo corr = all_corr.get(elements.get(i));
			
			Pair curr_pair = new Pair(elements.get(i), corr.get_r2());
			
			pairs.add(curr_pair);
		}
		
		Collections.sort(pairs, new PairComparison());
		
		ArrayList<Pair> n_pairs = new ArrayList<Pair>();
		
		// Remove all except elements with n highest r2 value
		for (int j = pairs.size() - elem_num; j < pairs.size(); j++) {
			n_pairs.add(0, pairs.get(j));
		}
		
		return n_pairs;
	}
	
	public Double get_raw_unknown_elem(String sample, Element elem) {
		Data elem_data = this.xrf_data.get_data(elem);
		if (elem_data == null) {
			return null;
		}

		// Integer object so it can be tested for null
		Integer pos = xrf_data.get_info(new TableKey("Name")).indexOf(sample);
		if (pos.equals(-1)) {
			return null;
		}

		Double data = elem_data.get_data().get(pos);
		if (data != null) {
			return data;
		} else {
			return null;
		}
	}
	
	public ArrayList<String> get_unknown_list() {
		return xrf_data.get_info(new TableKey("Name"));
	}
	
	public void set_correlation_graph_elements(Element primary, Element secondary) {

		this.primary = primary;
		this.secondary = secondary;
		
		notify_update();
	}
	
	public boolean is_pair_value_selected(Element primary, Element secondary) { 

		ElementCorrelationInfo elem_corr = this.correlations.get(primary);
		
		return elem_corr.is_selected(secondary); 
	}
	
	public void set_selected_rsqrd_assocs(Element primary, Element secondary) {
		
		ElementCorrelationInfo elem_corr = this.correlations.get(primary);
		elem_corr.add_selected(secondary);
		
		notify_update();
	}
	
	public void remove_selected_rsqrd_assocs(Element primary, Element secondary) {
		ElementCorrelationInfo elem_corr = this.correlations.get(primary);
		
		elem_corr.remove_selected(secondary);
		
		notify_update();
	}
	
	public boolean check_selected_rsqrd_assocs(Element primary, Element secondary) {
		return (this.primary == primary && this.secondary == secondary);
	}
	
	public void add_update_notify(ui_framework.SystemWindow window_parent) {
		
	}
	
	public void notify_update() {
		//on changes to data
		this.internal_refresh();
		this.window_parent.refresh();
	}
	
	public void set_primary(Element prim) {
		this.primary = prim;
	}
	
	public void set_secondary(Element sec) {
		this.secondary = sec;
	}
	
	public void set_xrf_table(DataTable tab) {
		this.xrf_data = tab;
	}
	
	public void set_means_table(DataTable tab) {
		this.means_data = tab;
	}
	
	public void set_standards_table(DataTable tab) {
		this.standards_data = tab;
	}
	
	public void set_correlations(HashMap<Element, ElementCorrelationInfo> corrs) {
		this.correlations = corrs;
	}
	
	@Override
	public String toString() {
		return new StringBuffer(" Primary : ").append(this.primary.toString())
				.append("\n Secondary : ").append(this.secondary.toString())
				.append("\n Model_element : ").append(this.model_data_element.toString())
				.append("\n xrf : ").append(this.xrf_data.get_raw_table().toString())	    // Only store raw data, parse on reload
				.append("\n stardards : ").append(this.standards_data.get_raw_table().toString())
				.append("\n means : ").append(this.means_data.get_raw_table().toString())
				.append("\n correlations : ").append(this.correlations.toString())
				.toString();
	}
}
