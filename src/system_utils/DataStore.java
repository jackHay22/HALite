package system_utils;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.awt.Color;
import ui_graphlib.PointSet;
import ui_graphlib.Point;

public class DataStore {
	private ui_framework.SystemWindow window_parent;
	private DataTable xrf_data;
	private DataTable standards_data;
	private DataTable means_data;

	public DataStore(ui_framework.SystemWindow window_parent) {
		this.window_parent = window_parent;
		this.xrf_data = new DataTable();
		this.standards_data = new DataTable();
		this.means_data = new DataTable();
	}
	
	private ArrayList<Double> calculate_coords(Element elem) {
		
		// Get listing of standards and unknowns from means file
		TableKey source_key = new TableKey("sourcefile");
		ArrayList<String> source = means_data.get_info(source_key);
		
		// Get listing of standards from standards file
		TableKey value_key = new TableKey("Calibration values");
		ArrayList<String> standards_names = standards_data.get_info(value_key);
		
		// Get listing of standards from standards file
		TableKey xrf_key = new TableKey("Name");
		ArrayList<String> xrf_names = standards_data.get_info(xrf_key);
		
		// Get element CPS and calibration values for an element
		ArrayList<Double> means = means_data.get_data(new TableKey(elem.name())).get_data();
		ArrayList<Double> standards = standards_data.get_data(new TableKey(elem.name())).get_data();
		ArrayList<Double> xrf = xrf_data.get_data(new TableKey(elem.name())).get_data();
		
		ArrayList<Double> coords = new ArrayList<Double>();
		
		// Define starting positions for unknowns/standards
		int unknowns_index = 0;
		int standards_index = 0;
		
		// Get table values from means and standards and calculate coordinates
		for (int i = standards_index; i < standards.size(); i++) {
			String source_id = source.get(i);
			
			int standards_pos = standards_names.indexOf(source_id);
			
			double elem_cps = means.get(i);
			double elem_standard = standards.get(standards_pos);
			
			coords.add(elem_cps / elem_standard);
			
		}
		
		return coords;
	}
	
	private PointSet create_pointset(Element x_elem, Element y_elem) {
		String x_axis = x_elem.name();
		String y_axis = y_elem.name();
		String title = x_axis + " vs. " + y_axis;
		Color color = new Color(1, 1, 1);
		boolean render = true;
		
		ArrayList<Point> points = new ArrayList<Point>();
		
		ArrayList<Double> x_coords = calculate_coords(x_elem);
		ArrayList<Double> y_coords = calculate_coords(y_elem);
		
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
	
	public void import_data(ArrayList<String> xrf, ArrayList<String> calibration, 
							ArrayList<String> means) throws FileNotFoundException {
		CSVParser parser = new CSVParser();
		
		// Collect all imported data sets
		this.xrf_data = parser.data_from_csv(xrf.get(0), xrf.get(1));
		this.standards_data = parser.data_from_csv(calibration.get(0), calibration.get(1));
		this.means_data = parser.data_from_csv(means.get(0), means.get(1));
		
		
	}
	
	public void add_update_notify(ui_framework.SystemWindow window_parent) {
		
	}
	public void notify_update() {
		//on changes to data
		this.window_parent.refresh();
	}
}
