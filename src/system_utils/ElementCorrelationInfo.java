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
import java.util.Arrays;
import java.util.Collections;

public class ElementCorrelationInfo implements Refreshable<DataStore>, Serializable {
	private static final long serialVersionUID = 4;
	private Element element;
	// Holds the correlation info for all elements related to this element
	private HashMap<Element, CorrelationInfo> all_correlations;
	// Holds all elements to be used within the model
	private ArrayList<CorrelationInfo> selected_elements;
	
	private DataStore data_store;
	
	// These hold the relevant statistical and model data for this element
	private HashMap<Element, Double> SEs;
	private HashMap<String, Double> std_WMs;
	private HashMap<String, Double> std_models;
	private HashMap<String, Double> standards_std_devs;
	private HashMap<String, Double> unknown_WMs;
	private HashMap<String, Double> unknown_models;
	private HashMap<String, Double> unknown_std_dev;
	
	// This holds info about the pairs selected by the client to not use in
	// the model
	private HashMap<String, ArrayList<Element>> pairs_to_avoid;
	
	// This is the equation used for the final model.
	private EquationPlot Equation;
	
	// These are the points for the model graph for this element in the form
	// of (actual, model) pairs
	private PointSet<DataStore> model_points;
	
	// This is the order of the pairs displayed on the top left panel for the given primary element
	private ArrayList<Pair> pairs_in_assoc_set;
	
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
		this.pairs_in_assoc_set = new ArrayList<Pair>();
		create_new_r2_pairs();
	}
	
	private void create_new_r2_pairs() {
		ArrayList<Pair> pairs = new ArrayList<Pair>();
		
		// Listing of all elements
		ArrayList<Element> elements = new ArrayList<Element>(Arrays.asList(Element.values()));
		
		// Remove element we are currently inside
		elements.remove(this.element);
		
		for (int i = 0; i < elements.size(); i++) {
			CorrelationInfo corr = all_correlations.get(elements.get(i));
			if (corr == null) {
				continue;
			}
			Pair curr_pair = new Pair(elements.get(i), corr.get_r2());
			pairs.add(curr_pair);
		}
		
		Collections.sort(pairs, new PairComparison());
		
		this.pairs_in_assoc_set = pairs;
		
	}
	
	private void update_r2_pairs() {
		for (Pair p: this.pairs_in_assoc_set) {
			p.set_r2(this.get_corr(p.get_elem()).get_r2());
		}
	}
	
	public void swap_displayed_pairs(Element currently_visable, Element to_show) {
		if (to_show.equals(currently_visable)) {
			return;
		}
		int first_pos = -1;
		int to_swap = -1;
		for (int i = 0; ; i++ ) {
			if (first_pos == -1 && (this.pairs_in_assoc_set.get(i).get_elem().equals(currently_visable))) {
				first_pos = i;
			} else if (to_swap == -1 && this.pairs_in_assoc_set.get(i).get_elem().equals(to_show)) {
				to_swap = i;
			} else if (first_pos != to_swap) {
				break;
			}
		}
		Collections.swap(this.pairs_in_assoc_set, first_pos, to_swap);
		return;
	}
	
	public ArrayList<Pair> get_top_n_r2_pairs(Integer n) {
		// Remove all except elements with n highest r2 value
		ArrayList<Pair> n_pairs = new ArrayList<Pair>();
		int j;
		if (pairs_in_assoc_set.size() - n > 0) {
			j = pairs_in_assoc_set.size() - n;
		} else {
			j = 0;
		}
		for (; j < pairs_in_assoc_set.size(); j++) {
			n_pairs.add(0, pairs_in_assoc_set.get(j));
		}
		return n_pairs;
	}
	
	// Tells the system whether or not to use a pair in the model
	public boolean not_in_model(String s, Element e) {
		ArrayList<Element> elems = this.pairs_to_avoid.get(s);
		return ((elems != null) && elems.indexOf(e) != -1);
	}
	
	// Creates the "full model report" for this element
	public ElementReport create_report() {
		return new ElementReport(element, selected_elements, pairs_to_avoid, this.get_WM_panel_data());
	}
	
	// Tells the model to toggle the "use" status of this pair 
	// from the bottom left panel of the application
	public void toggle_pair_for_model(String s, Element e) {
		ArrayList<Element> elems = pairs_to_avoid.get(s);
		// If there is already a list for this sample
		// simply toggle the pair
		if (elems != null) {
			int i = elems.indexOf(e);
			if (i == -1) {
				elems.add(e);
				this.pairs_to_avoid.put(s, elems);
			} else {
				elems.remove(i);
			}
		// If no list exists, create is and add the offending element
		} else {
			elems = new ArrayList<Element>();
			elems.add(0, e);
			this.pairs_to_avoid.put(s, elems);
		}
	}
	
	// Place the value for the standards in a hashmap to be easily
	// parsed on the front end
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
	
	// Does the same as the above for the unknown values
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
	
	// Computes the best fit equation for the model points
	public void compute_graph_model() {
		SimpleRegression reg_obj = new SimpleRegression(true);
		ArrayList<Point> point_list = new ArrayList<Point>();
		
		// Adds all of the points to be used into the model
		for (String std : data_store.get_STDlist()) {
			Double x = data_store.get_raw_std_elem(std, element);
			Double y = this.std_models.get(std);
			
			if (y.isNaN() || y == null) {
				y = x;
			}
			if (x != null && y != null) {
				point_list.add(new Point(x, y));
				reg_obj.addData(x, y);
			}
		}
		
		model_points = new PointSet<DataStore>(point_list, SystemThemes.HIGHLIGHT, "Actual", "Model", element.toString() + " Model", true);
		
		double x_0 = reg_obj.getIntercept();
		double x_1 = reg_obj.getSlope();
		double r_2 = reg_obj.getRSquare();
		
		this.Equation = new EquationPlot(r_2, 1, x_0, x_1);
		
	}
	
	public EquationPlot get_equation() {
		return Equation;
	}
	
	public PointSet<DataStore> get_model_plot(){
		return model_points;
	}
	
	// Returns the pointsets for the model graph to use
	public HashMap<String, PointSet<DataStore>> get_pointsets() {
		HashMap<String, PointSet<DataStore>> pts = new HashMap<String, PointSet<DataStore>>();
		pts.put("standard", model_points);
		return pts;
	}
	
	// These public "get" methods, in general, 
	// are returning information to be used either 
	// during output or in the UI
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

	public boolean is_selected(Element secondary) {
		CorrelationInfo corr = this.all_correlations.get(secondary);
		return corr.in_use();
	}
	
	// These set methods are used by datastore to pass along changes
	// requested by the UI
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
	
	private Double get_unknown_eq_val(String sample, Element elem) {
		return this.all_correlations.get(elem).get_unknown_corr(sample);
	}
	
	// This map is used to return all data necessary to be displayed within the interface 
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
				Double d = get_unknown_eq_val(s, corr.get_secondary());
				if (d == null) {
					d = -1.0;
				}
				inner_map.put(corr.get_secondary().toString(), d);
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
		// Uses the Formulas lib in this project
		Double SE = Formulas.standard_error(x_list, y_list);
		return SE;
	}
	
	// Clears the hashmap and recomputes the SEs for the given model
	private void computeSEs() {
		// The map must be cleared since some elements may have been 
		// removed from the model before this call to refresh
		SEs.clear();
		for (CorrelationInfo info : selected_elements) {
			SEs.put(info.get_secondary(), computeSE(info.get_corr_results_for_SE()));
		}
	}

	private double getSE(Element elem) {
		return SEs.get(elem);
	}
	
	// Computes the weighted means of the unknown samples
	private void compute_unknown_WMs() {
		// Cleared in case model elements have been changed 
		unknown_WMs.clear();
		for (String sample : data_store.get_unknown_list()) {
			Double calculated = compute_unknown_WM(sample);
			if (calculated != null) {
				unknown_WMs.put(sample, calculated);
			}
		}
	}
	
	// Computes the wms for the standard samples
	private void compute_std_WMs() {
		std_WMs.clear();
		for (String std : data_store.get_STDlist()) {
			Double calculation = compute_std_WM(std);
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
	private Double compute_std_WM(String std) {
		double dividend = 0;
		DescriptiveStatistics std_dev = new DescriptiveStatistics();
		Double std_error_sum = 0.0;
		ArrayList<Element> elems_to_avoid = this.pairs_to_avoid.get(std);
		if (elems_to_avoid == null) {
			elems_to_avoid = new ArrayList<Element>();
		}
		// This loops the the elements selected for the model
		for (CorrelationInfo elem_info : this.selected_elements) {
			// Checks that the element has not been marked to avoid in the model
			if (elems_to_avoid.indexOf(elem_info.get_secondary()) == -1) {
				Double response = elem_info.get_corr_result(std);
				// Gets the response value
				if (response != null) {
					std_dev.addValue(response);
					// Weights the elements based on their standard error 
					dividend += (response * 1/this.getSE(elem_info.get_secondary()));
					std_error_sum += 1/this.getSE(elem_info.get_secondary());
				}
			}
		}
		// Saves the std dev to use later
		Double stdev = std_dev.getStandardDeviation();
		this.standards_std_devs.put(std, stdev);
		return (dividend/std_error_sum);	
		
	}
	
	// Applies the model creates to one unknown sample
	private Double compute_unknown_model(String sample) {
		Double sample_CPS = data_store.get_mean_value(sample, this.element);
		if (sample_CPS != null) {
			return (sample_CPS)/(this.unknown_WMs.get(sample));
		}
		return null;
	}
	
	// Applies the model to all unknown values for this element
	private void compute_unknown_models() {
		this.unknown_models.clear();
		for (String s : data_store.get_unknown_list()) {
			Double d = compute_unknown_model(s);
			this.unknown_models.put(s, d);
		}
	}
	
	// Applies the model to a single standard value
	private Double compute_std_model(String std) {
		Double elementCPS = data_store.get_mean_value(std, this.element);
		if (elementCPS != null) {
			return (elementCPS)/(std_WMs.get(std));	
		}
		return null;
	}
	
	// Applies the model to all unknown samples
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
		this.update_r2_pairs();
		// Computes the model only if there are elements selected
		if (this.selected_elements.size() != 0) {
			computeSEs();
			compute_std_WMs();
			compute_std_models();
			compute_graph_model();
			compute_unknown_WMs();
			compute_unknown_models();
		} else {
			model_points = std_vs_std();
		}
	}

	// This is called only if there are no elements chosen for a given model
	private PointSet<DataStore> std_vs_std() {
		// We simply create a false model where all actual values are
		// plotted against themselves
		ArrayList<Point> point_list = new ArrayList<Point>();
		
		for (String std : data_store.get_STDlist()) {
			if (data_store.get_raw_std_elem(std, element) != null) {
				double x = data_store.get_raw_std_elem(std, element);
				point_list.add(new Point(x, x));
			}
		}
		
		model_points = new PointSet<DataStore>(point_list, SystemThemes.HIGHLIGHT, "Actual", "Actual", element.toString() + " No elem pairs", true);
		
		this.Equation = new EquationPlot(1, 1, 0, 1);
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
	public void add_refreshable(Refreshable<DataStore> refreshable_component) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void on_start() {
		// TODO Auto-generated method stub
		
	}
	
}