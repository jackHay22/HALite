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
import system_utils.io_tools.MeansCSVParser;
import system_utils.io_tools.TestSuiteReader;
import ui_framework.DataBackend;
import ui_framework.SystemWindow;
import java.awt.Color;
import ui_graphlib.PointSet;
import ui_stdlib.SystemThemes;
import ui_graphlib.Point;

public class DataStore extends DataBackend implements Serializable {

	private static final long serialVersionUID = 1;
	
	private String save_path;
	
	private Element primary;
	private Element secondary;
	
	private Element model_data_element = Element.Hf;
	
	private DataTable xrf_data;
	private DataTable standards_data;
	private DataTable means_data;
	private DataTable standards_means_data;
	private DataTable unknown_means_data;
	
	private HashMap<Element, ElementCorrelationInfo> correlations;
	private ArrayList<Element> displayed_elems;
	
	private int elem_num;

	public boolean calculated_vals_updated = true;
	
	public DataStore(SystemWindow window_parent) {
		super(window_parent);
		
		this.save_path = "";
		
		this.xrf_data = new DataTable();
		this.standards_data = new DataTable();
		this.means_data = new DataTable();
		this.standards_means_data = new DataTable();
		this.unknown_means_data = new DataTable();
		
		this.correlations = new HashMap<Element, ElementCorrelationInfo>();
		this.displayed_elems = new ArrayList<Element>();
		
		this.elem_num = 5;
	}
	
	public void set_window(SystemWindow window_parent) {
		super.set_window_parent(window_parent);
	}
	
	public void set_save_path(String path) {
		this.save_path = path;
	}
	
	public String get_path() {
		return this.save_path;
	}
	
	public boolean path_assigned() {
		return !this.save_path.isEmpty();
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

		ArrayList<String> names;
		
		ArrayList<String> source;
		
		ArrayList<Double> data_in_use;
		// If it contains the element EXACTLY
		// FIX ME
		// This needs to parse to see if the strings are contained
		if (stand_points) {

			if (standards_means_data.contains_data(new TableKey(elem.name()))) {
				
				means = standards_means_data.get_data(new TableKey(elem.name())).get_data();
				names = standards_data.get_info(new TableKey("Calibrationvalues"));
				Data temp_stds = standards_data.get_data(new TableKey(elem.name()));
				
				if (temp_stds == null) {
					data_in_use = new ArrayList<Double>();
				} else {
					data_in_use = temp_stds.get_data();

				}
			} else {
				return null;
			}
			// Get listing of standards and unknowns from means file
			
			TableKey source_key = new TableKey("sourcefile");
			source = standards_means_data.get_info(source_key);
			
		} else {

			if (unknown_means_data.contains_data(new TableKey(elem.name()))) {
				means = unknown_means_data.get_data(new TableKey(elem.name())).get_data();
				names = xrf_data.get_info(new TableKey("Name"));
				Data temp_xrf = xrf_data.get_data(new TableKey(elem.name()));
				
				// If we have no data to calculate coords with, return an empty arraylist
				if (temp_xrf == null) {
					data_in_use = new ArrayList<Double>();
				}
				else {
					data_in_use = temp_xrf.get_data();
				}
			} else {
				return null;
			}
			
			// Get listing of standards and unknowns from means file
			
			TableKey source_key = new TableKey("sourcefile");
			source = unknown_means_data.get_info(source_key);
			
		}
		
		
		
		ArrayList<Double> coords = new ArrayList<Double>();
		
		// Get table values from means and standards and calculate coordinates
		for (int i = 0; i < means.size(); i++) {

			Double elem_cps = means.get(i);
			
			if (elem_cps == null) {
				continue;
			}
			
			String source_id = source.get(i);
			int pos = names.indexOf(source_id);
			
			if (pos < 0 || data_in_use.size() == 0) {
				continue;
			}
			if (data_in_use.get(pos) == null) {
				continue;
			}
			else {
				double elem_standard = data_in_use.get(pos);
				coords.add(elem_cps / elem_standard);
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
	
	public void displayed_elems(Element elem) {
		if (this.displayed_elems.contains(elem)) {
			this.displayed_elems.remove(elem);
		}
		else {
			this.displayed_elems.add(elem);
		}
	}
	
	public ArrayList<Element> get_displayed_elems() {
		return this.displayed_elems;
	}
	
	public ElementCorrelationInfo get_correlations(Element elem) {
		return this.correlations.get(elem);
	}
	
	public void import_data(String xrf, ArrayList<String> xrf_table, String calibration, ArrayList<String> calibration_table, 
			String means, ArrayList<String> means_table) throws FileNotFoundException {
		CSVParser parser = new CSVParser();
				
		// Collect all imported data sets
		this.xrf_data = parser.data_from_csv(xrf_table.get(0), new BufferedReader(new FileReader(xrf)));
		this.standards_data = parser.data_from_csv(calibration_table.get(0), new BufferedReader(new FileReader(calibration)));
		
		MeansCSVParser means_parser = new MeansCSVParser(this.get_STDlist(), this.get_unknown_list());
		
		HashMap<String, DataTable> tables = new HashMap<String, DataTable>();
		tables = means_parser.tables_from_csv(means_table.get(0), new BufferedReader(new FileReader(means)));
		this.standards_means_data = tables.get("standards");
		this.unknown_means_data = tables.get("unknowns");
		this.means_data = parser.data_from_csv(means_table.get(0), new BufferedReader(new FileReader(means)));
		
		create_element_correlations();
		
	}
	
	public void import_test_data(String xrf, String stds, String means) throws FileNotFoundException {
		CSVParser parser = new CSVParser();
		
		TestSuiteReader test_reader = new TestSuiteReader();
		
		// Collect all imported data sets
		this.xrf_data = parser.data_from_csv("XRF", test_reader.get_resources_input(xrf));
		this.standards_data = parser.data_from_csv("standards", test_reader.get_resources_input(stds));

		MeansCSVParser means_parser = new MeansCSVParser(this.get_STDlist(), this.get_unknown_list());
		
		HashMap<String, DataTable> tables = new HashMap<String, DataTable>();
		tables = means_parser.tables_from_csv("MEANS", test_reader.get_resources_input(means));
		this.standards_means_data = tables.get("standards");
		this.unknown_means_data = tables.get("unknowns");
		this.means_data = parser.data_from_csv("MEANS", test_reader.get_resources_input(means));

		create_element_correlations();
	}
	
	public Double get_std_response_value(String sample, Element elem) {
		
		Double top = get_mean_value(sample, elem);		
		Double bottom = this.get_raw_std_elem(sample, elem);
		
		if (top == null || bottom == null) {
			return null;
		}
		
		return top/bottom;
	}
	
	public Double get_unknown_response_value(String sample, Element elem) {
		
		Double top = get_mean_value(sample, elem);
		Double bottom = this.get_raw_unknown_elem(sample, elem);
		
		if (top == null || bottom == null) {
			return null;
		}
		
		return top/bottom;
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
		calculated_vals_updated = true;
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
	
	public ArrayList<Element> get_WM_elems() {
		ArrayList<Element> headers = this.correlations.get(this.model_data_element).get_selected_names();
		return headers;
	}
	
	public ArrayList<String> get_WM_headers() {
		ArrayList<String> headers = new ArrayList<String>();
		if (get_WM_elems().size() > 0) {
			headers.add("Std Dev");
			headers.add("WM");
		}
		return headers;
	}
	
	public String get_detailed_report() {
		HashMap<Element, ElementReport> reports = new HashMap<Element, ElementReport>(); 
		for (Element elem : Element.values()) {
			ElementCorrelationInfo elem_info = this.get_correlations(elem);
			if (elem_info != null) {
				reports.put(elem, elem_info.create_report());
			}
		}
		StringBuilder sb = new StringBuilder();
		sb.append(this.report_header(reports));
		for (String s : this.get_STDlist()) {
			for (Element report_elem : Element.values()) {
				sb.append(reports.get(report_elem).get_row(s));
			}
		}
		sb.append('\n');
		sb.append('\n');
		for (String s : this.get_unknown_list()) {
			for (Element report_elem : Element.values()) {
				sb.append(reports.get(report_elem).get_row(s));
			}
		}
		return sb.toString();
	}
	
	private String report_header(HashMap<Element, ElementReport> reports) {
		StringBuilder sb = new StringBuilder();
		for (Element elem : Element.values()) {
			sb.append(reports.get(elem).get_header_row());
		}
		return sb.toString();
	}
	
	public HashMap<String, HashMap<String, Double>> get_WM_data() {
		return this.correlations.get(this.model_data_element).get_WM_panel_data();
	}
	
	public Double get_current_WM(String std) {
		Double result = this.get_WM_data().get(std).get("WM");
		if (result == null) {
			return get_current_actual(std);
		}
		return result;
	}
	
	public boolean not_used_in_model(String s, Element e) {
		ElementCorrelationInfo elem_info = this.get_correlations(this.model_data_element);
		return elem_info.not_in_model(s, e);
	}
	
	public Double get_current_actual(String std) {
		return this.get_WM_data().get(std).get("Actual");
	}
	
	public Double get_header_std_pair(String std, String item) {
		Double result = this.get_WM_data().get(std).get(item);
		if (result != null) {
			return result;
		}
		return null;
	}
	
	public Double get_current_model(String std) {
		Double result = this.get_WM_data().get(std).get("Model_Value");
		if (result == null) {
			return this.get_WM_data().get(std).get("Actual");
		}
		return result;
	}
	
	public Double get_current_stdev(String std) {
		return this.get_WM_data().get(std).get("Std Dev");
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
			if (corr == null) {
				continue;
			}
			Pair curr_pair = new Pair(elements.get(i), corr.get_r2());
			
			pairs.add(curr_pair);
		}
		
		Collections.sort(pairs, new PairComparison());
		
		ArrayList<Pair> n_pairs = new ArrayList<Pair>();
		
		// Remove all except elements with n highest r2 value
		int j;
		if (pairs.size() - elem_num > 0) {
			j = pairs.size() - elem_num;
		} else {
			j = 0;
		}
		for (; j < pairs.size(); j++) {
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
		calculated_vals_updated = true;
		notify_update();
	}
	
	// Used by CalcValPanel to remove elements from certain calculations
	public void toggle_sample_elem_pair(String s, Element e) {
		ElementCorrelationInfo elem_info = this.get_correlations(this.model_data_element);
		elem_info.toggle_pair_for_model(s, e);
		calculated_vals_updated = true;
		notify_update();
	}
	
	// Removes an element from the list of elements used to calculate the model 
	public void remove_selected_rsqrd_assocs(Element primary, Element secondary) {
		ElementCorrelationInfo elem_corr = this.correlations.get(primary);
		
		elem_corr.remove_selected(secondary);
		calculated_vals_updated = true;
		notify_update();
	}
	
	public boolean check_selected_rsqrd_assocs(Element primary, Element secondary) {
		return (this.primary == primary && this.secondary == secondary);
	}
	
	
	public void notify_update() {
		//on changes to data
		this.internal_refresh();
		super.notify_update();
	}
	
	private String get_STD_computed_string() {
		StringBuilder sb = new StringBuilder();
		for (String std : this.get_STDlist()) {
			sb.append(std);
			for (Element elem : Element.values()) {
				sb.append(",");
				Double data = this.correlations.get(elem).get_standard_computed().get(std);
				if (data != null) {
					sb.append(data.toString());
				}
			}
			sb.append("\n");
			}
		return sb.toString();
	}

	private String get_unknown_computed_string() {
		StringBuilder sb = new StringBuilder();
		for (String sample : this.get_unknown_list()) {
			sb.append(sample);
			for (Element elem : Element.values()) {
				sb.append(",");
				Double data = this.correlations.get(elem).get_unknown_computed().get(sample);
				if (data != null) {
					sb.append(data.toString());
				}
			}
			sb.append("\n");
		}
		return sb.toString();
	}

	public String get_model_string() {
		// This ~SHOULD~ be what needs to be written to whatever file they choose. (csv format)
		StringBuilder sb = new StringBuilder();
		sb.append("Sample Name");

		for (Element elem : Element.values()) {
			sb.append(",");
			sb.append(elem.toString());
		}
		sb.append("\n");
		sb.append(this.get_STD_computed_string());
		sb.append(this.get_unknown_computed_string());
		
		return sb.toString();
	}
}
