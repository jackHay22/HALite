package system_utils;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.awt.Color;
import ui_graphlib.PointSet;
import ui_graphlib.Point;

public class DataStore {
	private ui_framework.SystemWindow window_parent;
	
	private DataTable xrf_data;
	private DataTable standards_data;
	private DataTable means_data;
	
	private HashMap<Element, ElementCorrelationInfo> correlations;
	
	private int elem_num;

	public DataStore(ui_framework.SystemWindow window_parent) {
		this.window_parent = window_parent;
		
		this.xrf_data = new DataTable();
		this.standards_data = new DataTable();
		this.means_data = new DataTable();
		
		this.correlations = new HashMap<Element, ElementCorrelationInfo>();
		
		this.elem_num = 5;
	}
	
	private ArrayList<Double> calculate_coords(Element elem, Boolean stand_points) {
		
		// Get element CPS data
		ArrayList<Double> means = means_data.get_data(new TableKey(elem.name())).get_data();
		
		// Get listing of standards and unknowns from means file
		TableKey source_key = new TableKey("sourcefile");
		ArrayList<String> source = means_data.get_info(source_key);
		
		ArrayList<String> names;
		
		if (stand_points) {
			// Get listing of standards from standards file
			TableKey value_key = new TableKey("Calibration values");
			names = standards_data.get_info(value_key);
		}
		else {
			// Get listing of standards from standards file
			TableKey xrf_key = new TableKey("Name");
			names = xrf_data.get_info(xrf_key);
		}

		ArrayList<Double> standards = standards_data.get_data(new TableKey(elem.name())).get_data();
		ArrayList<Double> xrf = xrf_data.get_data(new TableKey(elem.name())).get_data();
		
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
				if (standards.get(pos) == null) {
					continue;
				}
				else {
					double elem_standard = standards.get(pos);
					coords.add(elem_cps / elem_standard);
				}
			}
			else {
				if (xrf.get(pos) == null) {
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
	
	private PointSet create_pointset(Element x_elem, Element y_elem, Boolean standards) {
		String x_axis = x_elem.name();
		String y_axis = y_elem.name();
		String title = x_axis + " vs. " + y_axis;
		Color color = new Color(1, 1, 1);
		boolean render = true;
		
		ArrayList<Point> points = new ArrayList<Point>();
		
		ArrayList<Double> x_coords = calculate_coords(x_elem, standards);
		ArrayList<Double> y_coords = calculate_coords(y_elem, standards);
		
		// Combine coordinates into points ArrayList
		for (int i = 0; i < x_coords.size(); i++) {
			if (x_coords.get(i) != null && y_coords.get(i) != null) {
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
			
			for (int j = 0; i < elements.size(); j++) {
				Element y_elem = elements.get(j);
				
				PointSet standards = create_pointset(x_elem, y_elem, true);
				PointSet unknowns = create_pointset(x_elem, y_elem, false);
				
				ElementPair pair = new ElementPair(x_elem, y_elem, standards, unknowns);
				
				CorrelationInfo corr_info = new CorrelationInfo(pair);
				
				elem_corr.put(y_elem, corr_info);
			}
			
			// Create new element correlation info object
			ElementCorrelationInfo elem_info = new ElementCorrelationInfo(x_elem, elem_corr);
			
			// Save correlations to object
			this.correlations.put(x_elem, elem_info);
		}
	}
	
	public void import_data(ArrayList<String> xrf, ArrayList<String> calibration, 
							ArrayList<String> means) throws FileNotFoundException {
		CSVParser parser = new CSVParser();
		
		// Collect all imported data sets
		this.xrf_data = parser.data_from_csv(xrf.get(0), xrf.get(1));
		this.standards_data = parser.data_from_csv(calibration.get(0), calibration.get(1));
		this.means_data = parser.data_from_csv(means.get(0), means.get(1));
		
		create_element_correlations();
		
	}
	
	public CorrelationInfo get_elem_correlation_info(Element x, Element y) {
		ElementCorrelationInfo x_correlations = this.correlations.get(x);
		
		return x_correlations.get_corr(y);
	}
	
	public void set_elem_num(Integer num) {
		this.elem_num = num;
	}
	
	public ArrayList<Pair> get_rsqrd_assoc_list(Element elem) {
		ArrayList<Pair> pairs = new ArrayList<Pair>();
		int elem_num = this.elem_num;
		
		ElementCorrelationInfo elem_corr = this.correlations.get(elem);
		HashMap<Element, CorrelationInfo> all_corr = elem_corr.get_all_corr();
		
		// Listing of all elements
		ArrayList<Element> elements = new ArrayList<Element>(Arrays.asList(Element.values()));
		
		for (int i = 0; i < elements.size(); i++) {
			CorrelationInfo corr = all_corr.get(elements.get(i));
			Pair curr_pair = new Pair(elements.get(i), corr.get_r2());
			
			pairs.add(curr_pair);
		}
		
		Collections.sort(pairs, new PairComparison());
		
		
		
		return pairs;
	}
	
	public void set_selected_rsqrd_assocs(Element primary, Element secondary) {
		
	}
	
	public void remove_selected_rsqrd_assocs(Element primary, Element secondary) {
		
	}
	
	public void add_update_notify(ui_framework.SystemWindow window_parent) {
		
	}
	public void notify_update() {
		//on changes to data
		this.window_parent.refresh();
	}
}
