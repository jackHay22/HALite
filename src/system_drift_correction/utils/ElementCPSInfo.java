package system_drift_correction.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

import system_drift_correction.DriftCorrectionDS;
import system_formulas.Formulas;
import system_utils.Element;
import ui_framework.Refreshable;
import ui_graphlib.Point;
import ui_graphlib.PointSet;
import ui_stdlib.SystemThemes;

// This class holds the information from all samples from one element
// for the drift correction
public class ElementCPSInfo implements Refreshable<DriftCorrectionDS> {
	
	// These hold the element name corresponding to the data
	private Element element;
	// all the raw data point sets from the input file
	private HashMap<String, PointSet<DriftCorrectionDS>> all_point_sets;
	// and the points after the drift correction has been applied 
	private HashMap<String, PointSet<DriftCorrectionDS>> corrected_point_sets;

	// These are the drift correction points and all associated data
	private PointSet<DriftCorrectionDS> drift_points;
	// This object holds the drift point data corresponding to this element
	private ElementDriftInfo drift_info;
	
	// This gives each object access to its parent datastore
	private DriftCorrectionDS datastore;

	// This boolean allows us to know if all the data has been read into the 
	// structure
	public boolean creation_complete = false;
	
	// This boolean allows us to tell datastore if the object has data
	// to export
	public boolean has_corrected_points = false;
	
	// Initialize the object
	public ElementCPSInfo(Element elem) {
		this.element = elem;
		this.all_point_sets = new HashMap<String, PointSet<DriftCorrectionDS>>();
		this.corrected_point_sets = new HashMap<String, PointSet<DriftCorrectionDS>>();
		this.drift_points = new PointSet<DriftCorrectionDS>(new ArrayList<Point>(), SystemThemes.HIGHLIGHT, "time", element.toString(), "Drift data fit", true);
	}
	
	// This method allows the databackend and csv reader to set (sample_name, point)
	// pairs in the private hashmap
	public void set_pair(String name, Point pt) {
		if (name.contains("DRIFT") ) {
			drift_points.add_point(pt);
		} else {
			PointSet<DriftCorrectionDS> points = all_point_sets.get(name);
			if (points != null) {
				points.add_point(pt);
			} else {
				points = new PointSet<DriftCorrectionDS>(new ArrayList<Point>(), SystemThemes.MAIN, "time", element.toString(), name, false);
				points.add_point(pt);
				all_point_sets.put(name, points);
			}
		}
	}
	
	// This method is called to apply the drift correction function 
	// to the points in all_point_sets
	private void correct_info() {
		for (Map.Entry<String, PointSet<DriftCorrectionDS>> set : this.all_point_sets.entrySet()) {
			
			// We create a new pointsets to avoid altering the original data for
			// correction with different equation later
			PointSet<DriftCorrectionDS> pts = new PointSet<DriftCorrectionDS>(set.getValue().get_points(), SystemThemes.MAIN, "time", element.toString(), "sample", false);
			this.corrected_point_sets.put(set.getKey(), pts);
		}
		this.has_corrected_points = true;
		drift_info.correct_map(this.corrected_point_sets);
	}
	
	// This method is used by the databackend to access a map containing all
	// the means of the sample points of this element
	public HashMap<String, Double> get_means() {
		HashMap<String, Double> means = new HashMap<String, Double>();
		for (Map.Entry<String, PointSet<DriftCorrectionDS>> set : this.corrected_point_sets.entrySet()) {
			ArrayList<Double> y_values = set.getValue().get_y_vals();
			means.put(set.getKey(), Formulas.mean_of_array(y_values));
		}
		return means;
	}
	
	// Returns the element of this object
	public Element get_element() {
		return this.element;
	}
	
	// Gives the mean of the data sample points for the sample 's' for this element
	public Double get_mean(String s) {
		ArrayList<Double> y_vals = this.corrected_point_sets.get(s).get_y_vals();
		
		return Formulas.mean_of_array(y_vals);
	}
	
	// Returns an array of the points of a given sample sorted by time value
	public ArrayList<Point> get_sorted_points(String s) {
		ArrayList<Point> points = this.corrected_point_sets.get(s).get_points();
		ArrayList<Point> sorted_points = new ArrayList<Point>();
		
		for (Point p : points) {
			if (sorted_points.size() == 0) {
				sorted_points.add(p);
			} else {
				for (int i = 0; i < sorted_points.size(); i++) {
					Point e = sorted_points.get(i);
					if (p.get_x() < e.get_x()) {
						sorted_points.add(i, p);
						break;
					} else if (i == sorted_points.size() - 1) {
						sorted_points.add(sorted_points.size(), p);
						break;
					}
				}
			}
		}
		return sorted_points;
	}
	
	
	
	// Gives access to the drift info object for this element
	public ElementDriftInfo get_drift_info() {
		return this.drift_info;
	}
	
	// Returns an array of only the time values corresponding to 
	// a given sample, sorted.
	public ArrayList<Double> get_sorted_times(String sample) {
		ArrayList<Double> sorted_points = new ArrayList<Double>();
		
		if (this.corrected_point_sets.get(sample) != null) {
			ArrayList<Point> points = this.corrected_point_sets.get(sample).get_points();
			
			for (Point p : points) {
				if (sorted_points.size() == 0) {
					sorted_points.add(p.get_x());
				} else {
					for (int i = 0; i < sorted_points.size(); i++) {
						Double e = sorted_points.get(i);
						if (p.get_x() < e) {
							sorted_points.add(i, p.get_x());
							break;
						} else if (i == sorted_points.size() - 1) {
							sorted_points.add(sorted_points.size(), p.get_x());
							break;
						}
					}
				}
			}
		}
		return sorted_points;
		
	}
	
	// Returns a hashmap containing the mean, std dev, and % std err for 
	// a given sample, for this element.
	public HashMap<String, Double> get_stats(String s) {
		HashMap<String, Double> all_data = new HashMap<String, Double>();
		ArrayList<Double> y_vals = this.corrected_point_sets.get(s).get_y_vals();
		
		all_data.put("mean", Formulas.mean_of_array(y_vals));
		
		DescriptiveStatistics stats = new DescriptiveStatistics();
		for (Double val : y_vals) {
			stats.addValue(val);
		}
		
		all_data.put("std dev", stats.getStandardDeviation());
		
		Double see = -1.0;
		
		if (y_vals != null && all_data.get("std dev") != null) {
			see = all_data.get("std dev")*100/Formulas.mean_of_array(y_vals);
		}
		all_data.put("%SEE", see);
		
		return all_data;
		
	}
	
	@Override
	// In this refresh call, we attempt to correct out current info if possible
	public void refresh() {
		if (drift_points != null && this.drift_points.get_points().size() != 0) {
			if (!creation_complete) {
				this.drift_info = new ElementDriftInfo(drift_points, this.element);
				this.drift_info.set_datastore(datastore);
				creation_complete = true;
			}
			this.drift_info.refresh();
			this.correct_info();
		}
	}

	@Override
	public void set_datastore(DriftCorrectionDS datastore) {
		// TODO Auto-generated method stub
		this.datastore = datastore;
	}

	@Override
	public void add_refreshable(Refreshable<DriftCorrectionDS> refreshable_component) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void on_start() {
		// TODO Auto-generated method stub
		
	}
	
}
