package system_utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;   // Import the FileWriter class
import java.io.IOException;  // Import the IOException class to handle errors
import java.io.ObjectOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.lang.IndexOutOfBoundsException;
import java.io.FileReader;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.Map;
import java.util.Map.Entry;

import system_graph_search.*;
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
	private Element r2_list_element = Element.Dy;
	
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
	
	public void elim_highest_sensitivity() {

		if (primary == null || secondary == null) {
			return;
		}
		
		ElementCorrelationInfo elem_corr = correlations.get(primary);
		
		for (CorrelationInfo corr : elem_corr.get_all_corr().values()) {
			if (corr == null) {
				System.out.println(corr);
				continue;
			}
			corr.toggle_highest();	
		}
		notify_update();
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
				new ErrorDialog<DataStore>("File Import Error", "Unable to load selected files. Please check that the correct files were selected. Error in standards/means relationship.").show_dialog();
				return false;
			}
		}
		
		// Iterate through unknowns data and check if Data objects are empty
		Map<TableKey, Data> unk_map = unknown_means_data.get_data();
		for (Entry<TableKey, Data> entry : unk_map.entrySet()) {
//			System.out.println(entry.getValue().get_data().toString());
			ArrayList<Double> d = entry.getValue().get_data();
			if (d.isEmpty()) {
				new ErrorDialog<DataStore>("File Import Error", "Unable to load selected files. Please check that the correct files were selected. Error in XRF/means relationship.").show_dialog();
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
	
	public void remove_n_outliers(int n) {
		if (this.r2_list_element != null)
			this.correlations.get(this.r2_list_element).remove_n_outliers(n);
		this.notify_update();
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
			if (data_in_use.get(pos) == null || data_in_use.get(pos).isNaN() || data_in_use.get(pos) <= 0.0) {
				continue;
			}
			else {
				double elem_standard = data_in_use.get(pos);
				if (elem_standard < 0.000001)
					System.out.println(Double.toString(elem_standard));
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
	
	private HashMap<Element, ArrayList<Element>> default_solns() {
		ArrayList<Element> list;
		HashMap<Element, ArrayList<Element>> solutions = new HashMap<Element, ArrayList<Element>>();
		list = new ArrayList<>(Arrays.asList(Element.Tb,Element.Er,Element.Y,Element.Sm,Element.Yb,Element.Gd,Element.Zr,Element.Hf,Element.Nd,Element.Ho,Element.Tm));
		solutions.put(Element.Dy, list);
		list = new ArrayList<>(Arrays.asList(Element.Al,Element.Si,Element.Mn,Element.Ga,Element.Y));
		solutions.put(Element.Ti, list);
		list = new ArrayList<>(Arrays.asList());
		solutions.put(Element.Si, list);
		list = new ArrayList<>(Arrays.asList());
		solutions.put(Element.Al, list);
		list = new ArrayList<>(Arrays.asList());
		solutions.put(Element.Fe, list);
		list = new ArrayList<>(Arrays.asList());
		solutions.put(Element.Mn, list);
		list = new ArrayList<>(Arrays.asList());
		solutions.put(Element.Mg, list);
		list = new ArrayList<>(Arrays.asList());
		solutions.put(Element.Ca, list);
		list = new ArrayList<>(Arrays.asList());
		solutions.put(Element.Na, list);
		list = new ArrayList<>(Arrays.asList());
		solutions.put(Element.K, list);
		list = new ArrayList<>(Arrays.asList());
		solutions.put(Element.P, list);
		list = new ArrayList<>(Arrays.asList(Element.Si));
		solutions.put(Element.As, list);
		list = new ArrayList<>(Arrays.asList(Element.Al,Element.Nd,Element.Sr,Element.Eu,Element.Pr,Element.Pb,Element.Ti,Element.Ce,Element.La));
		solutions.put(Element.Ba, list);
		list = new ArrayList<>(Arrays.asList(Element.Sm,Element.Pb,Element.Ba,Element.Al,Element.Fe,Element.Ti));
		solutions.put(Element.Bi, list);
		list = new ArrayList<>(Arrays.asList(Element.La,Element.Pr,Element.Nd,Element.Sm,Element.Ba,Element.Sr,Element.Gd,Element.Eu,Element.Yb));
		solutions.put(Element.Ce, list);
		list = new ArrayList<>(Arrays.asList(Element.Ga,Element.Mn,Element.V,Element.Ni,Element.Fe,Element.Si));
		solutions.put(Element.Cr, list);
		list = new ArrayList<>(Arrays.asList(Element.Ba,Element.Ga,Element.P,Element.U,Element.Fe,Element.Si));
		solutions.put(Element.Cs, list);
		list = new ArrayList<>(Arrays.asList(Element.Al,Element.W,Element.Ti,Element.Ga,Element.Fe,Element.Mn,Element.Zn,Element.Si,Element.V));
		solutions.put(Element.Cu, list);
		list = new ArrayList<>(Arrays.asList(Element.Dy,Element.Yb,Element.Tb,Element.Y,Element.Gd,Element.Zr,Element.Hf,Element.Tm,Element.Sm));
		solutions.put(Element.Er, list);
		list = new ArrayList<>(Arrays.asList(Element.Sm,Element.Gd,Element.Nd,Element.Sr,Element.Pr,Element.Ba,Element.Al,Element.La,Element.Ce,Element.Dy));
		solutions.put(Element.Eu, list);
		list = new ArrayList<>(Arrays.asList());
		solutions.put(Element.Ag, list);
		list = new ArrayList<>(Arrays.asList(Element.Ni,Element.Mg,Element.Ca));
		solutions.put(Element.Cd, list);
		list = new ArrayList<>(Arrays.asList());
		solutions.put(Element.Se, list);
		list = new ArrayList<>(Arrays.asList(Element.V));
		solutions.put(Element.Ge, list);
		list = new ArrayList<>(Arrays.asList(Element.Fe,Element.Al,Element.W,Element.Ti,Element.Mn,Element.Si));
		solutions.put(Element.Co, list);
		list = new ArrayList<>(Arrays.asList(Element.Mn,Element.Ti,Element.Si,Element.Al,Element.Fe,Element.Na,Element.W,Element.Y,Element.Zn));
		solutions.put(Element.Ga, list);
		list = new ArrayList<>(Arrays.asList(Element.Dy,Element.Er,Element.Tb,Element.Hf,Element.Nd,Element.Eu,Element.Pr,Element.Zr,Element.Yb,Element.Sm,Element.Y));
		solutions.put(Element.Gd, list);
		list = new ArrayList<>(Arrays.asList(Element.Zr,Element.Nd,Element.Gd,Element.Yb,Element.Tb,Element.Y,Element.Dy,Element.Tm,Element.Ho,Element.Er));
		solutions.put(Element.Hf, list);
		list = new ArrayList<>(Arrays.asList(Element.Tb,Element.Y,Element.Dy,Element.Er,Element.Yb,Element.Sm,Element.Zr,Element.Hf,Element.Al,Element.Ti,Element.Tm));
		solutions.put(Element.Ho, list);
		list = new ArrayList<>(Arrays.asList(Element.Ce,Element.Pr,Element.Nd,Element.Sm,Element.Ba,Element.Eu,Element.Th,Element.Gd,Element.Yb));
		solutions.put(Element.La, list);
		list = new ArrayList<>(Arrays.asList(Element.Yb,Element.Dy,Element.Er,Element.Tm,Element.Hf,Element.Zr,Element.Ho,Element.Gd));
		solutions.put(Element.Lu, list);
		list = new ArrayList<>(Arrays.asList(Element.Si,Element.Fe,Element.Ce,Element.Ta,Element.Th,Element.La,Element.U));
		solutions.put(Element.Mo, list);
		list = new ArrayList<>(Arrays.asList(Element.Zn,Element.Zr,Element.Th,Element.Ta,Element.Y,Element.Na,Element.Ho,Element.Al,Element.Ce,Element.Tb,Element.Ti));
		solutions.put(Element.Nb, list);
		list = new ArrayList<>(Arrays.asList(Element.Pr,Element.Ce,Element.La,Element.Eu,Element.Sm,Element.Hf));
		solutions.put(Element.Nd, list);
		list = new ArrayList<>(Arrays.asList(Element.Zn,Element.Si));
		solutions.put(Element.Ni, list);
		list = new ArrayList<>(Arrays.asList(Element.Ba,Element.Al,Element.Pr,Element.Co,Element.Rb,Element.Ce,Element.Si,Element.La));
		solutions.put(Element.Pb, list);
		list = new ArrayList<>(Arrays.asList(Element.Nd,Element.Ce,Element.La,Element.Eu,Element.Sr,Element.Ba,Element.Na));
		solutions.put(Element.Pr, list);
		list = new ArrayList<>(Arrays.asList(Element.Si,Element.Ga,Element.Ba,Element.K,Element.Ce,Element.Fe));
		solutions.put(Element.Rb, list);
		list = new ArrayList<>(Arrays.asList(Element.Si,Element.Sr,Element.Gd,Element.Zn,Element.Fe,Element.Bi,Element.Na,Element.Y,Element.W));
		solutions.put(Element.Sb, list);
		list = new ArrayList<>(Arrays.asList(Element.Si,Element.Y,Element.Ga,Element.Zr,Element.Al,Element.W));
		solutions.put(Element.Sc, list);
		list = new ArrayList<>(Arrays.asList(Element.Nd,Element.Ce,Element.La,Element.Gd,Element.Yb,Element.Al,Element.Hf,Element.Eu,Element.Dy,Element.Pr));
		solutions.put(Element.Sm, list);
		list = new ArrayList<>(Arrays.asList(Element.Ba,Element.La,Element.Ce,Element.Na,Element.Nd));
		solutions.put(Element.Sn, list);
		list = new ArrayList<>(Arrays.asList(Element.Eu,Element.Sm,Element.Nd,Element.Gd,Element.Pr,Element.Al,Element.Ce,Element.Ba,Element.La));
		solutions.put(Element.Sr, list);
		list = new ArrayList<>(Arrays.asList(Element.Th,Element.La,Element.Ce,Element.Pr,Element.Hf,Element.Nd,Element.U,Element.Ba,Element.Zr,Element.Sm));
		solutions.put(Element.Ta, list);
		list = new ArrayList<>(Arrays.asList(Element.Er,Element.Gd,Element.Nd,Element.La,Element.Dy,Element.Zr,Element.Hf,Element.Ho,Element.Y,Element.Yb));
		solutions.put(Element.Tb, list);
		list = new ArrayList<>(Arrays.asList(Element.Zr,Element.Sm,Element.Yb,Element.Hf,Element.Tb,Element.Ce,Element.La,Element.Nd));
		solutions.put(Element.Th, list);
		list = new ArrayList<>(Arrays.asList(Element.Th,Element.Ta,Element.K,Element.U,Element.La,Element.Nd,Element.Hf));
		solutions.put(Element.Tl, list);
		list = new ArrayList<>(Arrays.asList(Element.Dy,Element.Y,Element.Yb,Element.Gd,Element.Er,Element.Zr,Element.Hf,Element.Nd));
		solutions.put(Element.Tm, list);
		list = new ArrayList<>(Arrays.asList(Element.Rb,Element.Tl,Element.Pb,Element.Si,Element.Fe,Element.Ti,Element.Nb,Element.Al));
		solutions.put(Element.U, list);
		list = new ArrayList<>(Arrays.asList(Element.Ga,Element.Si,Element.Fe,Element.Ti,Element.Ni,Element.W,Element.Cu,Element.Cr));
		solutions.put(Element.V, list);
		list = new ArrayList<>(Arrays.asList(Element.Si,Element.Al));
		solutions.put(Element.W, list);
		list = new ArrayList<>(Arrays.asList(Element.Hf,Element.Tm,Element.Dy,Element.Sm,Element.Zr,Element.Gd,Element.La,Element.Ho,Element.Lu,Element.Yb,Element.Er));
		solutions.put(Element.Y, list);
		list = new ArrayList<>(Arrays.asList(Element.Y,Element.Hf,Element.Zr,Element.Al,Element.Er,Element.Dy,Element.La,Element.Sm,Element.Eu,Element.Ho,Element.Tm));
		solutions.put(Element.Yb, list);
		list = new ArrayList<>(Arrays.asList(Element.Si,Element.Mn,Element.Al,Element.Ga,Element.Fe,Element.Na,Element.La,Element.Ce,Element.Nd));
		solutions.put(Element.Zn, list);
		list = new ArrayList<>(Arrays.asList(Element.Hf,Element.Y,Element.Al,Element.Dy,Element.Tm,Element.Ho,Element.Nb,Element.Lu,Element.Gd));
		solutions.put(Element.Zr, list);
		
		return solutions;
	}
	
	
	
	private double eqValue(EquationPlot e) {
		return (e.get_r2() - 0.005 * Math.abs(e.get_coeff(1) - 1));
	}
	
	public void try_in_order_for_primary(boolean use_subset) {
		try_in_order_for_element(this.r2_list_element, use_subset);
		this.notify_update();
		this.set_model_data_element(this.r2_list_element);
	}
	
	private void try_in_order_for_element(Element el, boolean use_subset) {
		
		HashSet<Element> subset = new HashSet<Element>();
		if (use_subset) {
			subset = new HashSet<Element>(Arrays.asList(Element.Si, Element.Ti, Element.Al, Element.Fe, Element.Mn, Element.Mg, Element.Ca, Element.Na, Element.K, Element.P, Element.Ni, Element.Cr, Element.Sc, Element.V, Element.Cu, Element.Zn, Element.Ba, Element.Rb, Element.Sr, Element.Y, Element.Zr, Element.Nb, Element.La, Element.Ce, Element.Nd, Element.Th, Element.Pb, Element.Dy, Element.W, Element.Sm));
		}
		
		ElementCorrelationInfo corr = this.correlations.get(el);
		
		if (corr == null) {
			System.out.println("NULL");
			return;
		}

		ArrayList<Element> elems_in_order = new ArrayList<Element>();
		ArrayList<Double> values = new ArrayList<Double>();
		
		
		
		for (Entry<Element, CorrelationInfo> inner_corr: corr.get_all_corr().entrySet()) {
			if (inner_corr.getKey() == el || inner_corr.getValue() == null) {
				continue;
			}
			
			boolean found = false;
			for (int i = 0; i < values.size(); i++) {
				if (inner_corr.getValue().get_r2() > values.get(i)) {
					values.add(i, inner_corr.getValue().get_r2());
					elems_in_order.add(i, inner_corr.getKey());
					found = true;
					break;
				}
			}
			if (!found) {
				values.add(inner_corr.getValue().get_r2());
				elems_in_order.add(inner_corr.getKey());
			}
			
		}
		
		Double last_r2 = -1.0;
		ArrayList<Element> elems = new ArrayList<>();
		
		
		
		for (Element e : elems_in_order) {
			if (use_subset && !subset.contains(e)) {
				continue;
			}

			
			ArrayList<Element> new_elems = (ArrayList<Element>) elems.clone();
			new_elems.add(e);
			
			corr.set_selected_elements(new_elems);
			corr.refresh();
			double cand = corr.get_equation().get_r2();
			
			if (cand > last_r2) {
				elems = new_elems;
				last_r2 = cand;
			}
		}
		
		corr.set_selected_elements(elems);
		corr.refresh();
	}
	
	public HashMap<Element, UndirectedGraph> make_graphs() {
		HashMap<Element, Double> minWeights = new HashMap<>();
		Double percentThreshold = 0.8;
		
		
		HashMap<Element, UndirectedGraph> graphs = new HashMap<>();
		
//		HashMap<Element, ArrayList<Element>> solns = default_solns();
//		
//		return graphs;
		
		// For each ElementCorrelationInfo in this.correlations we need to look at every other ElementCorrelationInfo
		// to make one initial graph with all elements and the edges between each node and the r2 values
		UndirectedGraph graph = new UndirectedGraph();
		ElementCorrelationInfo[] temp_corrs = this.correlations.values().toArray(new ElementCorrelationInfo[0]);
		
		for (int i = 0; i < temp_corrs.length; i++) {
			ElementCorrelationInfo ocorr = temp_corrs[i];
			WeightedVertex vertex = new WeightedVertex(ocorr.get_element().toString());
			graph.addVertex(vertex);
			minWeights.put(ocorr.get_element(), ocorr.get_top_percent_threshold(percentThreshold));
		}
		Double minEdgeWeight = 0.0;
		for (Double d : minWeights.values()) {
			if (!Double.isNaN(d)) {
				minEdgeWeight += d;
			}
		}
		
		minEdgeWeight = minEdgeWeight / minWeights.size();
		
		for (int i = 0; i < temp_corrs.length; i++) {
			ElementCorrelationInfo ocorr = temp_corrs[i];
			WeightedVertex wvo = graph.getVertex(ocorr.get_element().toString());
			for (int j = i+1; j < temp_corrs.length; j++) {
				ElementCorrelationInfo icorr = temp_corrs[j];
				if (ocorr.get_all_corr().get(icorr.get_element()) != null) {
					WeightedVertex wvi = graph.getVertex(icorr.get_element().toString());
					graph.addEdge(wvo, wvi);
					graph.getEdge(wvo, wvi).addProperty("weight", ocorr.get_all_corr().get(icorr.get_element()).get_r2());
				}
			
			}
		}
		
		try {
		      FileWriter myWriter = new FileWriter("halite_trials.csv");		      
				
			for (int seed = 1; seed < 31; seed += 33) {
				for (int it = 100; it <= 500; it += 500) {
					for (int expon = 2; expon <= 2; expon++) {
						for (double coeff = 0.5; coeff <= 4; coeff = coeff * 2) {
							if (!((coeff == 0.5 && it == 100) || (coeff == 4.0 && it == 500)) )
							{
								continue;
							}
							// Then, for each element we make a copy of the graph, set all node weights to be the r2 value for that 
							// node and the target element, remove the target element from the graph, and add to the graphs map
							for (int i = 0; i < temp_corrs.length; i++) {
								ElementCorrelationInfo ocorr = temp_corrs[i];
					
								UndirectedGraph elGraph = graph.copy();
								elGraph.removeVertex(elGraph.getVertex(ocorr.get_element().toString()));
								for (WeightedVertex wv: elGraph.getVertices()) {
									if (ocorr.get_corr(Element.valueOf(wv.getName())) != null) {
										wv.addProperty("weight", ocorr.get_corr(Element.valueOf(wv.getName())).get_r2());
									}
								}
								elGraph.removeWeightedEdges("weight", minEdgeWeight);
								elGraph.removeWeightedVertices("weight", minWeights.get(ocorr.get_element()));
								
								if (elGraph.getVertices().size() == 0) {
									continue;
								}
								
								long start = System.nanoTime();
								Set<Set<WeightedVertex>> subgraphs = new HashSet<>();
								subgraphs = CliqueAlgorithm.kPlexPLS(elGraph, seed, it, coeff, expon);
//								subgraphs = CliqueAlgorithm.bronKerboschPivoting(elGraph);
								
								long kPlexRuntime = System.nanoTime() - start;
								start = System.nanoTime();
								
								Set<WeightedVertex> best = new HashSet<>();
								double best_score = 0.0;
								
					//			for (Element e : solns.get(ocorr.get_element())) {
					//				best.add(graph.getVertex(e.toString()));
					//			}
								
								for (Set<WeightedVertex> sg: subgraphs) {
									
									ArrayList<Element> elems = new ArrayList<>();
									for (WeightedVertex v: sg) {
										elems.add(Element.valueOf(v.getName()));
									}
									ocorr.set_selected_elements(elems);
									ocorr.refresh();
									double cand = eqValue(ocorr.get_equation()) * Math.pow(1.5, sg.size());
									
					//				System.out.println(SystemThemes.get_display_number(best_score, "#.00000") + " " + SystemThemes.get_display_number(cand, "#.00000") + " " + ocorr.get_selected().size());
									if (cand > best_score && sg.size() > 0) {
										best = sg;
										best_score = cand;
									}
								}
								long choiceTime = System.nanoTime() - start;
								
								ArrayList<Element> elems = new ArrayList<>();
					//			System.out.print(ocorr.get_element().toString() + " " + SystemThemes.get_display_number(100 * best_score, "#.00000") + " " + Double.toString(best.size()) + " " + Integer.toString(subgraphs.size()) + " ");
								Integer k = 0;
						        while (!system_graph_search.CliqueAlgorithm.isKPlex(best, k)) {
						            k += 1;
						        }
						        String res = Double.toString(coeff) + Integer.toString(it) + Integer.toString(expon) + ",";
						        res += ocorr.get_element().toString() + ", ";
								for (WeightedVertex v: best) {
									elems.add(Element.valueOf(v.getName()));
									res += " " + v.getName();
								}
					
								ocorr.set_selected_elements(elems);
								ocorr.refresh();
								
								res += ", " + Double.toString(ocorr.get_equation().get_r2()) + ", " + Double.toString(ocorr.get_equation().get_coeff(1)); 
								res += ", " + Integer.toString(best.size()) + ", " + String.valueOf(kPlexRuntime/1000000) + ", " + String.valueOf(choiceTime/1000000);
								res += ", " + Integer.toString(k);
								res += ", " + Integer.toString(subgraphs.size());
								System.out.println(res);
								if (ocorr.get_element() == Element.Co) {
									
									System.out.println(elGraph.toString());									
								}
								

								myWriter.write(res);
								myWriter.write("\n");
					//			System.out.println();
					//			System.out.print(ocorr.get_element().toString() + " " + SystemThemes.get_display_number(ocorr.get_equation().get_r2(), "#.00000") + " " + Double.toString(best.size()) + " " + Integer.toString(subgraphs.size()) + " ");
								
								graphs.put(ocorr.get_element(), elGraph);
	
							}
						}
					}
				}
			}

		      myWriter.close();
	    } catch (IOException e) {
	      System.out.println("An error occurred.");
	      e.printStackTrace();
	    }
		System.out.println(minWeights);
		System.out.println(minEdgeWeight);
		// We could also run the algo right here, and set the selected_elements for each ElemenetCorrelationInfo,
		// allowing us to call the "export" method right away to get our results
		
		return graphs;
	}
	
	
	
	
	
	public void remove_outliers_for_element() {
		this.calculated_vals_updated = true;
		this.correlations.get(this.model_data_element).remove_outliers();
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
//		make_graphs();
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
	
	public ArrayList<Pair> get_all_rsqrd_assoc(Element elem) {
		return this.correlations.get(elem).get_top_n_r2_pairs(Element.values().length - 1);
	}
	
	public ArrayList<Pair> get_rsqrd_assoc_list(Element elem) {
		this.r2_list_element = elem;
		// This is a temporary fix to satisfy part of rick's request
		//		return this.correlations.get(elem).get_top_n_r2_pairs(this.elem_num);
		return this.correlations.get(elem).get_top_n_r2_pairs(Element.values().length - 1);
	}
	
	public void swap_out_elem_in_r2(Element primary, Element to_hide, Element to_show) {
		if (this.primary == primary) {
			this.secondary = to_show;
			this.correlations.get(primary).swap_displayed_pairs(to_hide, to_show);
		}
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
	
	@Override
	public boolean import_element_choices(String file_path) {
		//TODO import the element choices from path
		
		try (BufferedReader br = new BufferedReader(new FileReader(file_path))) {
		    String line;
		    while ((line = br.readLine()) != null) {
		        // process the line.
		    	line = line.replaceAll("[\\n ]", "");
		    	String[] elem_and_elems = line.split(":");
		    	Element main_elem = Element.valueOf(elem_and_elems[0]);
		    	ElementCorrelationInfo eci = correlations.get(main_elem);
		    	eci.clear_selection();
		    	for (String s : elem_and_elems[1].split(",")) {
		    		eci.add_selected(Element.valueOf(s));
		    	}
		    }
		} catch (Exception e) {
			return false;
		}
		notify_update();
		return true;
	}
	
	@Override
	public boolean export_element_choices(String file_path) {
		//TODO export the element choices to path
		// HashMap<Element, ElementCorrelationInfo> correlations
		String model_string = "";
		for (Element e : correlations.keySet()) {
			ElementCorrelationInfo eci = correlations.get(e);
			if (eci.get_selected_names().size() == 0) {
				continue;
			}
			model_string = model_string + e.toString() + ":";
			
			for (Element inner_e : eci.get_selected_names()) {
				model_string = model_string + inner_e.toString() + ",";
			}
			model_string = model_string.substring(0, model_string.length() - 1);
			model_string = model_string + "\n";
		}
		
		
		BufferedWriter out = null;

		try {
		    FileWriter fstream = new FileWriter(file_path, false); //true tells to append data.
		    out = new BufferedWriter(fstream);
		    out.write(model_string);
		}

		catch (IOException e) {
		    System.err.println("Error: " + e.getMessage());
		    return false;
		}

		finally {
		    if(out != null) {
		        try {
					out.close();
				} catch (IOException e1) {
					e1.printStackTrace();
					return false;
				}
		    }
		}
		return true;
	}
}
