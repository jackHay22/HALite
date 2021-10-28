package system_utils;

import org.apache.commons.math3.stat.regression.SimpleRegression;

import ui_framework.Refreshable;
import ui_graphlib.PointSet;
import ui_graphlib.Point;

import java.util.HashMap;
import java.io.Serializable;
import java.util.ArrayList;

public class CorrelationInfo implements Refreshable<DataStore>, Serializable {
	private static final long serialVersionUID = 5;
	private ElementPair<DataStore> data_to_plot;
	private Element secondary_element;
	private EquationPlot equation;
	private boolean use_in_wm;
	private DataStore data_store;
	private HashMap<String, Double> STD_corr_results = new HashMap<String, Double>();
	private HashMap<String, Double> unknown_corr_results = new HashMap<String, Double>();
	
	public CorrelationInfo(ElementPair<DataStore> elements) {
		this.data_to_plot = elements;
		this.secondary_element = elements.get_second();
		
		init();
	}
	
	private void init() {
		PointSet<DataStore> points_to_fit = data_to_plot.get_standards();
		// Create the EquationPlot object of degree 1 with fit and r2 value to match
		this.equation = compute_fit(points_to_fit);
		this.use_in_wm = false;
	}
	
	// Computes the best fit model for the standard model
	private EquationPlot compute_fit(PointSet<DataStore> point_set) {
		SimpleRegression reg_obj = new SimpleRegression(true);
		ArrayList<Point> points = point_set.get_points();
		
		// This loops over the relevant points adding them to the regression
		// object which will be used to create the model involved
		for (int i = 0; i < points.size(); i++) {
			Point point = points.get(i);
			
			if (point.in_use()) {
				Double x = point.get_x();
				Double y = point.get_y();
				if ((x != null && y != null) && !(x.isNaN() || y.isNaN())){
					reg_obj.addData(x, y);
				} else {
				}
			}
		}
		// Get relevant info from the regression object
		double x_0 = reg_obj.getIntercept();
		double x_1 = reg_obj.getSlope();
		double r_2 = reg_obj.getRSquare();
		
		EquationPlot new_eqn = new EquationPlot(r_2, 1, x_0, x_1);
		new_eqn.set_rmse( Math.sqrt(reg_obj.getMeanSquareError()));
		
		return new_eqn;
	}
	
	public void remove_n_outliers(int n) {
		if (n <= 0) {
			return;
		}
		ArrayList<Point> points = data_to_plot.get_standards().get_points();
		int highest_index = -1;
		double highest_value = -10.0;
		for (int i = 0; i < points.size(); i++) {
			Point point = points.get(i);
			
			if (point.in_use()) {
				double pred_value = this.equation.get_y(point.get_x());
				double diff = Math.abs(pred_value - point.get_y());
				if (highest_value < diff && diff > this.equation.get_rmse()*2.5) {
					highest_index = i;
					highest_value = diff;
				}
			}
		}
		if (highest_index >= 0) {
			points.get(highest_index).toggle();
			System.out.print("Remove: ");
			System.out.println(this.secondary_element);
		}
		// Recompute fits 
		PointSet<DataStore> points_to_fit = data_to_plot.get_standards();
		// Create the EquationPlot object of degree 1 with fit and r2 value to match
		this.equation = compute_fit(points_to_fit);
		
		// Make resursive call
		this.remove_n_outliers(n-1);
	}
	
	public void toggle_highest() {
		ArrayList<Point> points = data_to_plot.get_standards().get_points();
		Point highest = new Point(-1.0, -1.0);
		for (int i = 0; i < points.size(); i++) {
			if (points.get(i).get_x() > highest.get_x()) {
				highest = points.get(i);
			}
		}
		highest.toggle();
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
	
	// This will return the relevant information about the 
	// points to be plotted to the correlation graph
	public HashMap<String, PointSet<DataStore>> get_data() {
		HashMap<String, PointSet<DataStore>> data = new HashMap<String, PointSet<DataStore>>();
		
		PointSet<DataStore> standards = data_to_plot.get_standards();
		PointSet<DataStore> unknowns = data_to_plot.get_unknowns();
		
		data.put("standard", standards);
		data.put("unknown", unknowns);
		
		return data;
	}
	
	// Applies the correlation equation to the data to be used later
	private void STD_corrs() {
		STD_corr_results.clear();
		for (String std : data_store.get_STDlist()) {
			// Gets the response value from datastore
			Double data = data_store.get_std_response_value(std, this.get_secondary());
			if (data != null) {
				// Puts the value through the equation
				Double res = equation.get_y(data);
				STD_corr_results.put(std, res);
			}
		}
	}
	
	// Same as above but for unknown samples
	private void unknown_corrs() {
		unknown_corr_results.clear();
		for (String sample : data_store.get_unknown_list() ) {
			Double data = data_store.get_unknown_response_value(sample, this.get_secondary());
			if (data != null) {
				Double res = equation.get_y(data);
				unknown_corr_results.put(sample, res);
			}
		}
	}
	
	// Returns all values for the unknown correlation values in an arraylist
	public ArrayList<Double> get_unknown_corrs() {
		ArrayList<Double> values = new ArrayList<Double>();
		for (Double val : unknown_corr_results.values()) {
			values.add(val);
		}
		return values;
	}
	
	// Returns the standard correlation results to be used in the computation
	// of the SEs for the WM values later
	public ArrayList<DoublePair> get_corr_results_for_SE() {
		ArrayList<DoublePair> values = new ArrayList<DoublePair>();
		
		for (String std : this.data_store.get_STDlist()) {
			Double x_val = this.data_store.get_std_response_value(std, this.get_primary());
			Double sec_x_val = this.data_store.get_std_response_value(std, this.get_secondary());
			if ( x_val != null && sec_x_val != null) {
				Double y_val = this.equation.get_y(sec_x_val);
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
		PointSet<DataStore> points_to_fit = data_to_plot.get_standards();
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
	public void add_refreshable(Refreshable<DataStore> refreshable_component) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void on_start() {
		// TODO Auto-generated method stub
		
	}
	
}