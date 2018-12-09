package system_utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.lang.IndexOutOfBoundsException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import system_utils.io_tools.CSVParser;
import system_utils.io_tools.DataExporter;
import system_utils.io_tools.MeansCSVParser;
import system_utils.io_tools.TestSuiteReader;
import ui_framework.DataBackend;
import ui_framework.SystemWindow;
import java.awt.Color;
import ui_graphlib.PointSet;
import ui_stdlib.SystemThemes;
import ui_stdlib.dialogwindows.ErrorDialog;
import ui_graphlib.Point;

public class DataStore extends DataBackend implements Serializable {

	private static final long serialVersionUID = 1;
	
	private String save_path;
	
	public String xrf_path;
	public String means_path;
	public String standards_path;
	
	private Element primary;
	private Element secondary;
	
	private Element model_data_element = Element.Hf;
	
	private ArrayList<DataTable> xrf_data;
	private ArrayList<DataTable> standards_data;
	private ArrayList<DataTable> means_data;
	
	private String xrf_in_use;
	private String standards_in_use;
	private String means_in_use;
	
	private DataTable standards_means_data;
	private DataTable unknown_means_data;
	
	private HashMap<Element, ElementCorrelationInfo> correlations;
	
	private int elem_num;

	public boolean calculated_vals_updated = true;
	
	public DataStore(SystemWindow<DataStore> window_parent) {
		super(window_parent);
		
		this.save_path = "";
		
		this.standards_means_data = new DataTable("Standards_means");
		this.unknown_means_data = new DataTable("Unknown_means");
		
		this.correlations = new HashMap<Element, ElementCorrelationInfo>();
		
		this.elem_num = 5;
	}
	
	// Reset standards data triggered from NewDialog
	public void reset_standards() {
		this.standards_means_data = new DataTable("Standards_means");
	}
	
	// Reset unknowns data triggered from NewDialog
	public void reset_unknowns() {
		this.unknown_means_data = new DataTable("Unknown_means");
	}
	
	public boolean validate_loaded() {
		
		// Doesn't continue if means and standards are completely empty
		if (this.standards_means_data.get_data().isEmpty() || this.unknown_means_data.get_data().isEmpty())
			return false;
		
		// Iterate through standards data and check if Data objects are empty
		Map<TableKey, Data> stds_map = standards_means_data.get_data();
		for (Entry<TableKey, Data> entry : stds_map.entrySet()) {
			ArrayList<Double> d = entry.getValue().get_data();
			if (d.isEmpty()) {
				new ErrorDialog<DataStore>("File Import Error", "Unable to load selected files. Please check that the correct files were selected.").show_dialog();
				return false;
			}
		}
		
		// Iterate through unknowns data and check if Data objects are empty
		Map<TableKey, Data> unk_map = unknown_means_data.get_data();
		for (Entry<TableKey, Data> entry : unk_map.entrySet()) {
			ArrayList<Double> d = entry.getValue().get_data();
			if (d.isEmpty()) {
				new ErrorDialog<DataStore>("File Import Error", "Unable to load selected files. Please check that the correct files were selected.").show_dialog();
				return false;
			}
		}
		
		return true;
	}
	
	@Override
	public boolean on_export(String file_path, int export_type) {
		
		// Instantiate exporting class 
		DataExporter exporter = new DataExporter(this);
		
		// Export data as CSV 
		if (export_type == SystemThemes.CSV_FULL_REPORT || export_type == SystemThemes.CSV_MODEL_DATA) {
			String output;
			if (export_type == SystemThemes.CSV_FULL_REPORT)
				output = this.get_detailed_report();
			else
				output = this.get_model_string();
			return exporter.export_csv(output, file_path, export_type);
		}
		
		// Export the different graphs
		else if (export_type == SystemThemes.PDF_CALIBRATION_GRAPHS) {
			return exporter.export_calibration_graphs(file_path, export_type);
		}
		else if (export_type == SystemThemes.PDF_RESPONSE_GRAPHS) {
			return exporter.export_response_graphs(file_path, export_type);
		}
		return false;
	}
	
	@Override
	public boolean add_component_filepath(String path, String label) {
		
		// Parser will all direct I/O operations triggered from DataStore
		CSVParser parser = new CSVParser();
		BufferedReader reader;
		
		try {
			// Try to open the file at path, otherwise fail
			reader = new BufferedReader(new FileReader(path));
		} catch (FileNotFoundException e1) {
			return false;
		}
		
		if (label.equals("xrf")) {
			try {
				// Parse the file and set to DataStore private variables
				xrf_data = parser.parse_data(reader);
				xrf_in_use = xrf_data.get(0).name();
				xrf_path = path;
			} catch (FileNotFoundException | IndexOutOfBoundsException e) {
				new ErrorDialog<DataStore>("Invalid File", "Unable to parse selected file. Please check that this is a valid file.").show_dialog();
				return false;
			}
			return true;
		}
		else if (label.equals("means")) {
			try {
				// Haven't imported standards data yet - can't parse means data
				if (standards_data == null) {
					try {
						reader.close();
					} catch (IOException e) {
					}
					return false;
				}
								
				// Specific parser for means files because of specialized needs
				MeansCSVParser means_parser = new MeansCSVParser(this.get_STDlist(), this.get_unknown_list());
				
				HashMap<String, DataTable> tables = new HashMap<String, DataTable>();
				
				ArrayList<String> means_table = parser.get_table_names(reader);
				try {
					tables = means_parser.tables_from_csv(means_table.get(0), new BufferedReader(new FileReader(path)));
				} catch (Exception e) {
					return false;
				}
				
				// Assign tables to private variables
				this.standards_means_data = tables.get("standards");
				this.unknown_means_data = tables.get("unknowns");
				
				// Trigger data analysis phase
				create_element_correlations();
				
				reader = new BufferedReader(new FileReader(path));
				means_data = parser.parse_data(reader);
				
				means_in_use = means_data.get(0).name();
				means_path = path;
			} catch (FileNotFoundException | IndexOutOfBoundsException e) {
				new ErrorDialog<DataStore>("Invalid File", "Unable to parse selected file. Please check that this is a valid file.").show_dialog();
				return false;
			}
			return true;
		}
		else if (label.equals("standards")) {
			try {
				// Parse the file and set to DataStore private variables
				standards_data = parser.parse_data(reader);
				standards_in_use = standards_data.get(0).name();
				standards_path = path;
			} catch (FileNotFoundException | IndexOutOfBoundsException e) {
				new ErrorDialog<DataStore>("Invalid File", "Unable to parse selected file. Please check that this is a valid file.").show_dialog();
				return false;
			}
			return true;
		}
		
		try {
			// Close the reader before ending
			reader.close();
		}
		catch (IOException e) {
		}
		
		return false;
	}
	
	public ArrayList<DataTable> all_tables(String label) {
		
		// Return the desired tables of data
		if (label.equals("xrf"))
			return xrf_data;
		else if (label.equals("means"))
			return means_data;
		else if (label.equals("standards"))
			return standards_data;
		
		return null;
	}
	
	// Assign what tables will be used for analysis from NewDialog
	public void set_tables_in_use(String xrf, String standards, String means) {
		xrf_in_use = xrf;
		standards_in_use = standards;
		means_in_use = means;
	}
	
	// Search for desired table in table listings
	private DataTable get_table(String name, ArrayList<DataTable> table_list) {
		for (DataTable dt : table_list) {
			if (dt.name().equals(name))
				return dt;
		}
		return null;
	}
	
	@Override
	public boolean save_to_filepath(String path) {
		set_save_path(path);
		return true;
	}
	
	// Assign save path for use with SaveDialog
	public void set_save_path(String path) {
		this.save_path = path;
	}
	
	@Override
	public String get_path() {
		return this.save_path;
	}
	
	@Override
	public boolean path_assigned() {
		return !this.save_path.isEmpty();
	}
	
	@Override
	public boolean check_valid_target() {
		
		// Create new file object to check if the file at the save path exists
		File save_file = new File(this.save_path);
		String parent_path = save_file.getParent();
		
		// Check if the parent path exists
		if (parent_path == null) {
			return false;
		}
		else {
			// Return the path of the parent file 
			File parent_dir = new File(parent_path);
			return parent_dir.exists();
		}
	}
	
	private void internal_refresh() {
		for (ElementCorrelationInfo elem_corr: correlations.values()) {
			// Trigger refresh across the entire correlation set
			elem_corr.refresh();
		}
	}
	
	private HashMap<String, Double> calculate_coords(Element elem, Boolean stand_points) {
				
		ArrayList<Double> means;

		ArrayList<String> names;
		
		ArrayList<String> source;
		
		ArrayList<Double> data_in_use;

		// This needs to parse to see if the strings are contained
		if (stand_points) {

			if (standards_means_data.contains_data(new TableKey(elem.name()))) {
				
				DataTable standards = get_table(standards_in_use, standards_data);
				
				means = standards_means_data.get_data(new TableKey(elem.name())).get_data();
				names = standards.get_info(new TableKey("Sample Names"));
				Data temp_stds = standards.get_data(new TableKey(elem.name()));
				
				if (temp_stds == null) {
					data_in_use = new ArrayList<Double>();
				} else {
					data_in_use = temp_stds.get_data();

				}
			} else {
				return null;
			}
			// Get listing of standards and unknowns from means file
			
			TableKey source_key = new TableKey("Sample Names");
			source = standards_means_data.get_info(source_key);
			
		} else {

			if (unknown_means_data.contains_data(new TableKey(elem.name()))) {
				
				DataTable xrf = get_table(xrf_in_use, xrf_data);
				
				means = unknown_means_data.get_data(new TableKey(elem.name())).get_data();
				names = xrf.get_info(new TableKey("Sample Names"));
				Data temp_xrf = xrf.get_data(new TableKey(elem.name()));
				
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
			
			TableKey source_key = new TableKey("Sample Names");
			source = unknown_means_data.get_info(source_key);
			
		}
		
		HashMap<String, Double> coords = new HashMap<String, Double>();
		
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
			if (data_in_use.get(pos) == null || data_in_use.get(pos).isNaN()) {
				continue;
			}
			else {
				double elem_standard = data_in_use.get(pos);
				if (elem_standard != 0) {
					coords.put(source_id, elem_cps / elem_standard);
				}
			}
		}
		
		return coords;
	}
	
	private PointSet<DataStore> create_pointset(Element y_elem, Element x_elem, Boolean standards) {
		
		// Create basic parameters for PointSet creation
		String x_axis = x_elem.name();
		String y_axis = y_elem.name();
		String title = x_axis + " vs. " + y_axis;
		Color color = new Color(1, 1, 1);
		boolean render = true;
		
		// Point objects will be stored in an ArrayList
		ArrayList<Point> points = new ArrayList<Point>();
		
		// Calculate coordinates and store in a hashmap according to source id
		HashMap<String, Double> x_coords = calculate_coords(x_elem, standards);
		HashMap<String, Double> y_coords = calculate_coords(y_elem, standards);
		
		// if either of the hashmaps have not been initialized, exit
		if (x_coords == null || y_coords == null) {
			return null;
		}
		
		// Combine coordinates into points ArrayList
		for (String sample : x_coords.keySet()) {
			if (y_coords.get(sample) != null && x_coords.get(sample) != null) {
				Point point = new Point(x_coords.get(sample), y_coords.get(sample));
				points.add(point);
			}
		}
		
		// Create point set from coordinates
		PointSet<DataStore> set = new PointSet<DataStore>(points, color, x_axis, y_axis, title, render);
		
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
				
				PointSet<DataStore> standards = create_pointset(x_elem, y_elem, true);
				PointSet<DataStore> unknowns = create_pointset(x_elem, y_elem, false);

				if (standards != null) {

					standards.set_color(SystemThemes.HIGHLIGHT);
					unknowns.set_color(SystemThemes.LOWLIGHT);
					
					ElementPair<DataStore> pair = new ElementPair<DataStore>(x_elem, y_elem, standards, unknowns);
					
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
	
	// Return correlation according to specific element from DataStore 
	public ElementCorrelationInfo get_correlations(Element elem) {
		return this.correlations.get(elem);
	}
	
	public HashMap<Element, ElementCorrelationInfo> get_correlation_map() {
		return this.correlations;
	}
	
	// Used for loading example data
	public boolean import_test_data(String xrf, String stds, String means) {
		
		TestSuiteReader testreader = new TestSuiteReader();
		
		BufferedReader reader = testreader.get_resources_input(xrf);
		boolean xrf_loaded = example_add_component_filepath(reader, xrf, "xrf");
		
		reader = testreader.get_resources_input(stds);
		boolean standards_loaded = example_add_component_filepath(reader, stds, "standards");
		
		reader = testreader.get_resources_input(means);
		boolean means_loaded = example_add_component_filepath(reader, means, "means");
		
		return xrf_loaded && standards_loaded && means_loaded;
	}
	
	public boolean example_add_component_filepath(BufferedReader reader, String path, String label) {
		CSVParser parser = new CSVParser();

		if (label.equals("xrf")) {
			try {
				xrf_data = parser.parse_data(reader);
				xrf_in_use = xrf_data.get(0).name();
				xrf_path = path;
			} catch (FileNotFoundException e) {
				return false;
			}
			return true;
		}
		else if (label.equals("means")) {
			try {
				// Haven't imported standards data yet - can't parse means data
				if (standards_data == null) {
					return false;
				}
				
				MeansCSVParser means_parser = new MeansCSVParser(this.get_STDlist(), this.get_unknown_list());
				
				// This is just for the "example" data
				
				HashMap<String, DataTable> tables = new HashMap<String, DataTable>();
				
				ArrayList<String> means_table = parser.get_table_names(reader);
				
				TestSuiteReader test_reader = new TestSuiteReader();
				reader = test_reader.get_resources_input(path);
				tables = means_parser.tables_from_csv(means_table.get(0), reader);
				
				this.standards_means_data = tables.get("standards");
				this.unknown_means_data = tables.get("unknowns");
				
				create_element_correlations();
				
				test_reader = new TestSuiteReader();
				reader = test_reader.get_resources_input(path);
				means_data = parser.parse_data(reader);
				
				means_in_use = means_data.get(0).name();
				means_path = path;
			} catch (FileNotFoundException e) {
				return false;
			}
			return true;
		}
		else if (label.equals("standards")) {
			try {
				standards_data = parser.parse_data(reader);
				standards_in_use = standards_data.get(0).name();
				standards_path = path;
			} catch (FileNotFoundException e) {
				return false;
			}
			return true;
		}
		
		return false;
	}
		
	
	// Returns the response data for the correlation graphs and model calculations
	public Double get_std_response_value(String sample, Element elem) {
		
		Double top = get_mean_value(sample, elem);		
		Double bottom = this.get_raw_std_elem(sample, elem);
		
		if (top == null || bottom == null) {
			return null;
		}
		
		return top/bottom;
	}
	
	// Returns the response data for the correlation graphs and model calculations
	public Double get_unknown_response_value(String sample, Element elem) {
		
		Double top = get_mean_value(sample, elem);
		Double bottom = this.get_raw_unknown_elem(sample, elem);
		
		if (top == null || bottom == null) {
			return null;
		}
		
		return top/bottom;
	}
	
	// Returns the raw data from the means file for a given sample and element
	public Double get_mean_value(String sample, Element elem) {
		DataTable means = get_table(means_in_use, means_data);
		int pos = means.get_info(new TableKey("Sample Names")).indexOf(sample);
		if (pos >= 0) {
			return means.get_data(elem).get_data(pos);
		}
		return null;
	}
	
	// Assign new element to graph in model panel and notify other panels
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
	
	// Allows the UI to access the information currently relevant to 
	// the top left panel of the interface
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
		DataTable standards = get_table(standards_in_use, standards_data);
		
		return standards.get_info(new TableKey("Sample Names"));
	}
	
	// Returns raw data from the 'standards' file which was fed in
	public Double get_raw_std_elem(String standard, Element elem) {
		DataTable standards = get_table(standards_in_use, standards_data);
		
		Data elem_data = standards.get_data(elem);
		if (elem_data == null) {
			return null;
		}
		
		// Integer object so it can be tested for null
		Integer pos = standards.get_info(new TableKey("Sample Names")).indexOf(standard);
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
	
	// Set number of elements to display
	public void set_elem_num(Integer num) {
		this.elem_num = num;
		
		notify_update();
	}
	
	// Get number of elements to display
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
	
	// Triggered on CSV output
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
		sb.append('\n');
		for (String s : this.get_STDlist()) {
			for (Element report_elem : Element.values()) {
				sb.append(reports.get(report_elem).get_row(s));
			}
			sb.append('\n');
		}
		sb.append('\n');
		sb.append('\n');
		for (String s : this.get_unknown_list()) {
			for (Element report_elem : Element.values()) {
				sb.append(reports.get(report_elem).get_row(s));
			}
			sb.append('\n');
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
	
	// This return the Weighted Mean value if available, or a reverse calculated value as a place holder if not, and
	// if that cannot be calculated then 0.0 is returned
	public Double get_current_WM(String std) {
		Double result = this.get_WM_data().get(std).get("WM");
		if (result == null || result.isNaN()) {
			if (this.get_current_actual(std) == null || this.get_current_actual(std).isNaN()) {
				return 0.0;
			} else {
				if (this.get_STDlist().contains(std)) {
					if (this.get_std_response_value(std, this.model_data_element) == null) {
						return 0.0;
					} else {
						return get_current_actual(std) * this.get_std_response_value(std, this.model_data_element);
					}
				} else if (this.get_unknown_list().contains(std) && this.get_unknown_response_value(std, this.model_data_element) != null) {
					return get_current_actual(std) * this.get_unknown_response_value(std, this.model_data_element);
				} else {
					return 0.0;
				}
			}
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
		
        switch (item) {
            case "WM":  
            	return this.get_current_WM(std);
            case "Std Dev":
            	return this.get_current_stdev(std);
            default: 
            	return this.get_WM_data().get(std).get(item);
        }
	}
	
	public Double get_current_model(String std) {
		if (this.get_WM_data().get(std) == null) {
			return this.get_current_actual(std);
		}
		Double result = this.get_WM_data().get(std).get("Model_Value");
		if (result == null || result.isNaN()) {
			return this.get_current_actual(std);
		}
		return result;
	}
	
	public Double get_current_stdev(String std) {
		Double d = this.get_WM_data().get(std).get("Std Dev");
		if (d.isNaN() || d == null) {
			return -1.0;
		}
		return d;
	}
	
	public ArrayList<Pair> get_rsqrd_assoc_list(Element elem) {
		ArrayList<Pair> pairs = new ArrayList<Pair>();
		int elem_num = this.elem_num;
		
		ElementCorrelationInfo elem_corr = this.correlations.get(elem);
		HashMap<Element, CorrelationInfo> all_corr = elem_corr.get_all_corr();
		
		// Listing of all elements
		ArrayList<Element> elements = new ArrayList<Element>();
		
		for (Element elem_to_add : SystemThemes.use_for_r2) {
			elements.add(elem_to_add);
		}
		
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
		DataTable xrf = get_table(xrf_in_use, xrf_data);
		
		Data elem_data = xrf.get_data(elem);
		if (elem_data == null) {
			return null;
		}

		// Integer object so it can be tested for null
		Integer pos = xrf.get_info(new TableKey("Sample Names")).indexOf(sample);
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
		DataTable xrf = get_table(xrf_in_use, xrf_data);
		
		return xrf.get_info(new TableKey("Sample Names"));
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
	
	@Override
	public void notify_update() {
		//on changes to data
		this.internal_refresh();
		super.notify_update();
	}
	
	// Provides the output systems with methods for getting information as strings
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
	
	@Override
	public boolean init_from_file(String path) {
		set_save_path(path);
		return true;
	}
}
