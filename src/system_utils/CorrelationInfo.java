package system_utils;

import org.apache.commons.math3.stat.regression.SimpleRegression;

import ui_framework.Refreshable;
import ui_graphlib.PointSet;
import ui_graphlib.Point;

import java.util.HashMap; 
import java.util.ArrayList;

public class CorrelationInfo implements Refreshable {
	private ElementPair data_to_plot;
	private Element secondary_element;
	private EquationPlot equation;
	private boolean use_in_wm;
	private DataStore data_store;
	private HashMap<String, Double> STD_corr_results = new HashMap<String, Double>();
	private HashMap<String, Double> unknown_corr_results = new HashMap<String, Double>();
	
	public CorrelationInfo(ElementPair elements) {
		// Create the EquationPlot object of degree 1 with fit and r2 value to match
		
		this.data_to_plot = elements;
		this.secondary_element = elements.get_second();
		
		init();
	}
	
	private void init() {
		PointSet points_to_fit = data_to_plot.get_standards();
		this.equation = compute_fit(points_to_fit);
		this.use_in_wm = false;
	}
	
	private EquationPlot compute_fit(PointSet point_set) {
		SimpleRegression reg_obj = new SimpleRegression(true);
		ArrayList<Point> points = point_set.get_points();
		
		for (int i = 0; i < points.size(); i++) {
			Point point = points.get(i);
			if (point.in_use()) {
				double x = point.get_x();
				double y = point.get_y();
				reg_obj.addData(x, y);
			}
		}
		// Get relevant info from the regression object
		double x_0 = reg_obj.getIntercept();
		double x_1 = reg_obj.getSlope();
		double r_2 = reg_obj.getRSquare();
		
		return new EquationPlot(r_2, 1, x_0, x_1);
	}
	
	public void toggle() {
		this.use_in_wm = !this.use_in_wm;
	}
	
	public boolean in_use() {
		return use_in_wm;
	}
	
	public Element get_secondary() {
		return this.secondary_element;
	}
	
	public Element get_primary() {
		return data_to_plot.get_main();
	}
	
	public double get_r2() {
		return equation.get_r2();
	}
	
	public HashMap<String, PointSet> get_data() {
		HashMap<String, PointSet> data = new HashMap<String, PointSet>();
		
		PointSet standards = data_to_plot.get_standards();
		PointSet unknowns = data_to_plot.get_unknowns();
		
		data.put("standard", standards);
		data.put("unknown", unknowns);
		
		return data;
	}
	
	private void STD_corrs() {
		for (String std : data_store.get_STDlist()) {
			Double data = data_store.get_raw_std_elem(std, this.get_primary());
			if (data != null) {
				Double res = equation.get_y(data);
				STD_corr_results.put(std, res);
			}
		}
	}
	
	private void unknown_corrs() {
		for (String sample : data_store.get_unknown_list() ) {
			Double data = data_store.get_raw_unknown_elem(sample, this.get_primary());
			if (data != null) {
				Double res = equation.get_y(data);
				unknown_corr_results.put(sample, res);
			}
		}
	}
	
	public ArrayList<Double> get_unknown_corrs() {
		ArrayList<Double> values = new ArrayList<Double>();
		for (Double val : unknown_corr_results.values()) {
			values.add(val);
		}
		return values;
	}
	
	public ArrayList<DoublePair> get_corr_results_for_SE() {
		ArrayList<DoublePair> values = new ArrayList<DoublePair>();
		
		for (String std : this.data_store.get_STDlist()) {
			Double x_val = this.data_store.get_std_response_value(std, this.get_secondary());
			if ( x_val != null) {
				Double y_val = this.equation.get_y(x_val);
				DoublePair pair = new DoublePair(x_val, y_val);
				
				values.add(pair);
			}
		}
		return values;
	}
	
	public Double get_unknown_corr(String sample) {
		return unknown_corr_results.get(sample);
	}
	
	public Double get_corr_result(String std) {
		return STD_corr_results.get(std);
	}
	
	public EquationPlot get_equation() {
		return equation;
	}

	@Override
	public void refresh() {
		// TODO Auto-generated method stub
		PointSet points_to_fit = data_to_plot.get_standards();
		this.equation = compute_fit(points_to_fit);
		STD_corrs();
		unknown_corrs();
	}

	@Override
	public void set_datastore(DataStore datastore) {
		// TODO Auto-generated method stub
		this.data_store = datastore;
	}

	@Override
	public void add_refreshable(Refreshable refreshable_component) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void on_start() {
		// TODO Auto-generated method stub
		
	}
	
}