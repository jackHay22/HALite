package system_utils;

import java.util.HashMap;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.apache.commons.math3.stat.regression.SimpleRegression;

import ui_framework.Refreshable;
import ui_graphlib.Point;
import ui_graphlib.PointSet;
import ui_stdlib.SystemThemes;

import java.util.ArrayList;

public class ElementCorrelationInfo implements Refreshable {
	private Element element;
	private HashMap<Element, CorrelationInfo> all_correlations;
	private ArrayList<CorrelationInfo> selected_elements;
	private DataStore data_store;
	private HashMap<Element, Double> SEs;
	private HashMap<String, Double> WMs;
	private EquationPlot Equation;
	private PointSet model_points;
	
	public ElementCorrelationInfo(Element element, HashMap<Element, CorrelationInfo> all_correlations) {
		this.element = element;
		this.all_correlations = all_correlations;
		this.selected_elements = new ArrayList<CorrelationInfo>();
		this.SEs = new HashMap<Element, Double>();
		this.WMs = new HashMap<String, Double>();
	}
	
	public void compute_model() {
		SimpleRegression reg_obj = new SimpleRegression(true);
		ArrayList<Point> point_list = new ArrayList<Point>();
		
		for (String std : data_store.get_STDlist()) {
			Double x = data_store.get_raw_std_elem(std, element);
			Double y = WMs.get(std);
			//y = data_store.get_mean_value(std, element)/y;
			if (x != null && y != null) {
				point_list.add(new Point(x, y));
				reg_obj.addData(x, y);
			}
		}
		
		model_points = new PointSet(point_list, SystemThemes.HIGHLIGHT, "Actual", "Model", element.toString() + " Model", true);
		
		double x_0 = reg_obj.getIntercept();
		double x_1 = reg_obj.getSlope();
		double r_2 = reg_obj.getRSquare();
		
		this.Equation = new EquationPlot(r_2, 1, x_0, x_1);
		
	}
	
	public EquationPlot get_equation() {
		return Equation;
	}
	
	public PointSet get_model_plot(){
		return model_points;
	}
	
	public HashMap<String, PointSet> get_pointsets() {
		HashMap<String, PointSet> pts = new HashMap<String, PointSet>();
		pts.put("standard", model_points);
		return pts;
	}
	
	public double get_WM(String std) {
		return WMs.get(std);
	}
	
	public double get_corr_result(Element elem, String std) {
		return get_corr(elem).get_corr_result(std);
	}
	
	public Element get_element() {
		return this.element;
	}
	
	public double get_r2(Element element) {
		return all_correlations.get(element).get_r2();
	}
	
	public CorrelationInfo get_corr(Element y) {
		return this.all_correlations.get(y);
	}
	
	public HashMap<Element, CorrelationInfo> get_all_corr() {
		return this.all_correlations;
	}
	
	public ArrayList<CorrelationInfo> get_selected() {
		return this.selected_elements;
	}
	
	public void add_selected(Element secondary) {
		CorrelationInfo corr = this.all_correlations.get(secondary);
		corr.toggle();
		this.selected_elements.add(corr);
	}
	
	public void remove_selected(Element secondary) {
		CorrelationInfo corr = this.all_correlations.get(secondary);
		corr.toggle();
		this.selected_elements.remove(corr);
	}
	
	public boolean is_selected(Element secondary) {
		CorrelationInfo corr = this.all_correlations.get(secondary);
		return corr.in_use();
	}

	private double computeSE(ArrayList<Double> elem_values) {
		DescriptiveStatistics stats = new DescriptiveStatistics();
		for (double d : elem_values) {
			stats.addValue(d);
		}
		double divisor = Math.sqrt(elem_values.size());
		return stats.getStandardDeviation()/divisor;
	}
	
	private void computeSEs() {
		for (CorrelationInfo info : selected_elements) {
			SEs.put(info.get_secondary(), computeSE(info.get_corr_results()));
		}
	}
	
	private double getSE(Element elem) {
		return SEs.get(elem);
	}
	
	private double getSEInverseSum() {
		double sum = 0;
		for (double d : this.SEs.values()) {
			sum += 1/d;
		}
		return sum;
	}
	
	private void computeWMs() {
		for (String std : data_store.get_STDlist()) {
			Double calculation = computeWM(std);
			if (calculation != null) {
				WMs.put(std, calculation);
			}
		}
	}
	
	private Double computeWM(String std) {
		double dividend = 0;
		
		for (CorrelationInfo elem_info : this.selected_elements) {
			if (elem_info.get_corr_result(std) != null) {
				dividend += (elem_info.get_corr_result(std) * this.getSE(elem_info.get_secondary()));
			}
		}
		Double elementCPS = data_store.get_mean_value(std, this.element);
		if (elementCPS != null) {
			return (elementCPS)/(dividend/this.getSEInverseSum());	
		}
		return null;
	}
	
	@Override
	public void refresh() {
		// TODO Auto-generated method stub
		for (CorrelationInfo corr: all_correlations.values()) {
			corr.refresh();
		}
		if (this.selected_elements.size() != 0) {
			computeSEs();
			computeWMs();
			compute_model();
		} else {
			model_points = std_vs_std();
		}
	}

	private PointSet std_vs_std() {
		SimpleRegression reg_obj = new SimpleRegression(true);
		ArrayList<Point> point_list = new ArrayList<Point>();
		
		for (String std : data_store.get_STDlist()) {
			if (data_store.get_raw_std_elem(std, element) != null) {
				double x = data_store.get_raw_std_elem(std, element);
				double y = data_store.get_raw_std_elem(std, element);
				point_list.add(new Point(x, y));
				reg_obj.addData(x, y);
			}
		}
		
		model_points = new PointSet(point_list, SystemThemes.HIGHLIGHT, "Actual", "Actual", element.toString() + " No elem pairs", true);
		double x_0 = reg_obj.getIntercept();
		double x_1 = reg_obj.getSlope();
		double r_2 = reg_obj.getRSquare();
		
		this.Equation = new EquationPlot(r_2, 1, x_0, x_1);
		return model_points;
	}
	
	@Override
	public void set_datastore(DataStore datastore) {
		// TODO Auto-generated method stub
		this.data_store = datastore;
		for (CorrelationInfo corr: all_correlations.values()) {
			corr.set_datastore(datastore);
		}
	}

	@Override
	public void add_refreshable(Refreshable refreshable_component) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void on_start() {
		// TODO Auto-generated method stub
		
	}
	
	// More to come
	
}