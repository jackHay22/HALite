package system_utils;

import java.util.HashMap;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.apache.commons.math3.stat.regression.SimpleRegression;

import system_formulas.Formulas;
import ui_framework.Refreshable;
import ui_graphlib.Point;
import ui_graphlib.PointSet;
import ui_stdlib.SystemThemes;

import java.io.Serializable;
import java.util.ArrayList;

public class ElementCorrelationInfo implements Refreshable, Serializable {
	private static final long serialVersionUID = 4;
	private Element element;
	private HashMap<Element, CorrelationInfo> all_correlations;
	private ArrayList<CorrelationInfo> selected_elements;
	private DataStore data_store;
	private HashMap<Element, Double> SEs;
	private HashMap<String, Double> std_WMs;
	private HashMap<String, Double> std_models;
	private HashMap<String, Double> standards_std_devs;
	private HashMap<String, Double> unknown_WMs;
	private HashMap<String, Double> unknown_models;
	private HashMap<String, Double> unknown_std_dev;
	private HashMap<String, ArrayList<Element>> pairs_to_avoid;
	private EquationPlot Equation;
	private PointSet model_points;
	
	public ElementCorrelationInfo(Element element, HashMap<Element, CorrelationInfo> all_correlations) {
		this.element = element;
		this.all_correlations = all_correlations;
		this.selected_elements = new ArrayList<CorrelationInfo>();
		this.SEs = new HashMap<Element, Double>();
		this.std_WMs = new HashMap<String, Double>();
		this.unknown_WMs = new HashMap<String, Double>();
		this.std_models = new HashMap<String, Double>();
		this.unknown_models = new HashMap<String, Double>();
		this.standards_std_devs = new HashMap<String, Double>();
		this.unknown_std_dev = new HashMap<String, Double>();
		this.pairs_to_avoid = new HashMap<String, ArrayList<Element>>();
	}
	
	public boolean not_in_model(String s, Element e) {
		ArrayList<Element> elems = this.pairs_to_avoid.get(s);
		return ((elems != null) && elems.indexOf(e) != -1);
	}
	
	public void toggle_pair_for_model(String s, Element e) {
		ArrayList<Element> elems = pairs_to_avoid.get(s);
		if (elems != null) {
			int i = elems.indexOf(e);
			if (i == -1) {
				elems.add(e);
				this.pairs_to_avoid.put(s, elems);
			} else {
				elems.remove(i);
			}
		} else {
			elems = new ArrayList<Element>();
			elems.add(0, e);
			this.pairs_to_avoid.put(s, elems);
		}
	}
	
	public HashMap<String, Double> get_standard_computed() {
		
		HashMap<String, Double> std_map = new HashMap<String, Double>();
		
		for (String s : data_store.get_STDlist()) {
			Double d = this.std_models.get(s);
			if (d != null) {
				std_map.put(s, d);
			} else {
				d = this.data_store.get_raw_std_elem(s, this.element);
				if (d != null) {
					std_map.put(s, d);
				}
			}
		}
		
		return std_map;
	}
	
	public HashMap<String, Double> get_unknown_computed() {
		
		HashMap<String, Double> unknown_map = new HashMap<String, Double>();
		
		for (String s : data_store.get_unknown_list()) {
			Double d = this.unknown_models.get(s);
			if (d != null) {
				unknown_map.put(s, d);
			} else {
				// Change to the default value
				Double xrf = this.data_store.get_raw_unknown_elem(s, this.element);
				if (xrf != null) {
					unknown_map.put(s, xrf);
				}
			}
		}
		
		return unknown_map;
	}
	
	public void compute_graph_model() {
		SimpleRegression reg_obj = new SimpleRegression(true);
		ArrayList<Point> point_list = new ArrayList<Point>();
		
		for (String std : data_store.get_STDlist()) {
			Double x = data_store.get_raw_std_elem(std, element);
			Double y = this.std_models.get(std);
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
		return std_WMs.get(std);
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
	
	public ArrayList<Element> get_selected_names() {
		ArrayList<Element> sel = new ArrayList<Element>();
		for (CorrelationInfo corr : get_selected()) {
			sel.add(corr.get_secondary());
		}
		return sel;
	}
	
	private Double get_corr_eq_val(String std, Element elem) {
		return this.all_correlations.get(elem).get_corr_result(std);
	}
	
	public HashMap<String, HashMap<String, Double>> get_WM_panel_data() {
		// Returns a hashmap, mapping each standard to the corresponding data for the row,
		// which is also in the form of a hashmap, which maps the column to the data
		HashMap<String, HashMap<String, Double>> outer_map = new HashMap<String, HashMap<String, Double>>();
		for (String s : data_store.get_STDlist()) {
			HashMap<String, Double> inner_map = new HashMap<String, Double>();
			for (CorrelationInfo corr : this.selected_elements) {
				Double d = get_corr_eq_val(s, corr.get_secondary());
				if (d == null) {
					d = -1.0;
				}
				inner_map.put(corr.get_secondary().toString(), d);
			}
			
			inner_map.put("WM", std_WMs.get(s));
			inner_map.put("Std Dev", this.standards_std_devs.get(s));
			
			inner_map.put("Model_Value", this.std_models.get(s));
			inner_map.put("Actual", data_store.get_raw_std_elem(s, element));
			
			outer_map.put(s, inner_map);
			
		}
		
		for (String s : data_store.get_unknown_list()) {
			HashMap<String, Double> inner_map = new HashMap<String, Double>();
			for (CorrelationInfo corr : this.selected_elements) {
				inner_map.put(corr.get_secondary().toString(), get_corr_eq_val(s, corr.get_secondary()));
			}
			
			inner_map.put("WM", unknown_WMs.get(s));
			inner_map.put("Std Dev", this.unknown_std_dev.get(s));
			
			inner_map.put("Model_Value", this.unknown_models.get(s));
			inner_map.put("Actual", data_store.get_raw_unknown_elem(s, element));
			
			outer_map.put(s, inner_map);
			
		}
		
		return outer_map;
	}

	private Double computeSE(ArrayList<DoublePair> elem_values) {
		ArrayList<Double> x_list = new ArrayList<Double>();
		ArrayList<Double> y_list = new ArrayList<Double>();
		
		for (DoublePair d : elem_values) {
			x_list.add(d.get_x());
			y_list.add(d.get_y());
		}
		Double SE = Formulas.standard_error(x_list, y_list);
		return SE;
	}
	
	private void computeSEs() {
		SEs.clear();
		for (CorrelationInfo info : selected_elements) {
			SEs.put(info.get_secondary(), computeSE(info.get_corr_results_for_SE()));
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
	
	private void compute_unknown_WMs() {
		unknown_WMs.clear();
		for (String sample : data_store.get_unknown_list()) {
			Double calculated = compute_unknown_WM(sample);
			if (calculated != null) {
				unknown_WMs.put(sample, calculated);
			}
		}
	}
	
	private void computeWMs() {
		std_WMs.clear();
		for (String std : data_store.get_STDlist()) {
			Double calculation = computeWM(std);
			if (calculation != null) {
				std_WMs.put(std, calculation);
			}
		}
	}
	
	
	// This applies the model created using the standards to the unknown data
	private Double compute_unknown_WM(String sample) {
		double dividend = 0;
		DescriptiveStatistics std_dev = new DescriptiveStatistics();
		Double std_error_sum = 0.0;
		ArrayList<Element> elems_to_avoid = this.pairs_to_avoid.get(sample);
		if (elems_to_avoid == null) {
			elems_to_avoid = new ArrayList<Element>();
		}
		for (CorrelationInfo elem_info : this.selected_elements) {
			if (elems_to_avoid.indexOf(elem_info.get_secondary()) == -1) {
				Double response = elem_info.get_unknown_corr(sample);
				if (response != null) {
					std_dev.addValue(response);
					dividend += (response * 1/this.getSE(elem_info.get_secondary()));
					std_error_sum += 1/this.getSE(elem_info.get_secondary());
				}
			}
		}
		Double stdev = std_dev.getStandardDeviation();
		this.unknown_std_dev.put(sample, stdev);
		return (dividend)/std_error_sum;
	}
	
	// This applies the model to the standards to be displayed in the bottom left panel 
	private Double computeWM(String std) {
		double dividend = 0;
		DescriptiveStatistics std_dev = new DescriptiveStatistics();
		Double std_error_sum = 0.0;
		ArrayList<Element> elems_to_avoid = this.pairs_to_avoid.get(std);
		if (elems_to_avoid == null) {
			elems_to_avoid = new ArrayList<Element>();
		}
		// Read this over for correctness
		for (CorrelationInfo elem_info : this.selected_elements) {
			if (elems_to_avoid.indexOf(elem_info.get_secondary()) == -1) {
				Double response = elem_info.get_corr_result(std);
				if (response != null) {
					std_dev.addValue(response);
					dividend += (response * 1/this.getSE(elem_info.get_secondary()));
					std_error_sum += 1/this.getSE(elem_info.get_secondary());
				}
			}
		}
		Double stdev = std_dev.getStandardDeviation();
		this.standards_std_devs.put(std, stdev);
		return (dividend/std_error_sum);	
		
	}
	
	private Double compute_unknown_model(String sample) {
		
		Double sample_CPS = data_store.get_mean_value(sample, this.element);
		if (sample_CPS != null) {
			// Same as the previous comment			
			return (sample_CPS)/(this.unknown_WMs.get(sample));
		}
		return null;
	}
	
	private void compute_unknown_models() {
		this.unknown_models.clear();
		for (String s : data_store.get_unknown_list()) {
			Double d = compute_unknown_model(s);
			this.unknown_models.put(s, d);
		}
		
		
	}
	
	private Double compute_std_model(String std) {
		Double elementCPS = data_store.get_mean_value(std, this.element);
		if (elementCPS != null) {
			return (elementCPS)/(std_WMs.get(std));	
		}
		return null;
	}
	
	private void compute_std_models() {
		this.std_models.clear();
		for (String std: data_store.get_STDlist()) {
			Double d = compute_std_model(std);
			this.std_models.put(std, d);
		}
	}
	
	@Override
	public void refresh() {
		// TODO Auto-generated method stub
		for (CorrelationInfo corr: all_correlations.values()) {
			if (corr != null) {
				corr.refresh();
			}
		}
		if (this.selected_elements.size() != 0) {
			computeSEs();
			computeWMs();
			compute_std_models();
			compute_graph_model();
			compute_unknown_WMs();
			compute_unknown_models();
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
			if (corr != null) {
				corr.set_datastore(datastore);
			}
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