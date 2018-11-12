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

public class ElementCPSInfo implements Refreshable<DriftCorrectionDS> {
	
	private Element element;
	private HashMap<String, PointSet<DriftCorrectionDS>> all_point_sets;
	private HashMap<String, PointSet<DriftCorrectionDS>> corrected_point_sets;

	
	private PointSet<DriftCorrectionDS> drift_points;
	private ElementDriftInfo drift_info;
	
	private DriftCorrectionDS datastore;

	public boolean creation_complete = false;
	public boolean has_corrected_points = false;
	
	public ElementCPSInfo(Element elem) {
		this.element = elem;
		this.all_point_sets = new HashMap<String, PointSet<DriftCorrectionDS>>();
		this.corrected_point_sets = new HashMap<String, PointSet<DriftCorrectionDS>>();
		this.drift_points = new PointSet<DriftCorrectionDS>(new ArrayList<Point>(), SystemThemes.HIGHLIGHT, "time", element.toString(), "Drift data fit", true);
	}
	
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

	private void correct_info() {
		for (Map.Entry<String, PointSet<DriftCorrectionDS>> set : this.all_point_sets.entrySet()) {
			this.corrected_point_sets.put(set.getKey(), set.getValue());
		}
		this.has_corrected_points = true;
		drift_info.correct_map(this.corrected_point_sets);
	}
	
	public HashMap<String, Double> get_means() {
		HashMap<String, Double> means = new HashMap<String, Double>();
		for (Map.Entry<String, PointSet<DriftCorrectionDS>> set : this.corrected_point_sets.entrySet()) {
			ArrayList<Double> y_values = set.getValue().get_y_vals();
			means.put(set.getKey(), Formulas.mean_of_array(y_values));
		}
		return means;
	}
	
	public Element get_element() {
		return this.element;
	}
	
	public Double get_mean(String s) {
		ArrayList<Double> y_vals = this.corrected_point_sets.get(s).get_y_vals();
		
		return Formulas.mean_of_array(y_vals);
	}
	
	public ArrayList<Point> get_sorted_points(String s) {
		ArrayList<Point> points = this.corrected_point_sets.get(s).get_points();
		ArrayList<Point> sorted_points = new ArrayList<Point>();
		
		for (Point p : points) {
			if (sorted_points.size() == 0) {
				sorted_points.add(p);
			} else {
				for (int i = 0; i < sorted_points.size(); i++) {
					Point e = sorted_points.get(i);
					if (p.get_x() > e.get_x()) {
						sorted_points.add(i + 1, p);
					}
				}
			}
		}
		
		return sorted_points;
		
	}
	
	public ElementDriftInfo get_drift_info() {
		return this.drift_info;
	}
	
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
						if (p.get_x() > e) {
							sorted_points.add(i + 1, p.get_x());
						}
					}
				}
			}
		}
		return sorted_points;
		
	}
	
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
		if (y_vals != null && all_data.get("std_dev") != null) {
			see = all_data.get("std_dev")*100/Math.pow(y_vals.size(), 0.5);
		}
		all_data.put("%SEE", see);
		
		return all_data;
		
	}
	
	@Override
	public void refresh() {
		// TODO Auto-generated method stub
		if (drift_points != null && this.drift_points.get_points().size() != 0) {
			if (!creation_complete) {
				this.drift_info = new ElementDriftInfo(drift_points);
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
