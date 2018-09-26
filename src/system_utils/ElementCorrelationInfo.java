package system_utils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileReader;
import ui_graphlib.PointSet;
import ui_graphlib.Point;
import java.util.HashMap; 
import java.util.ArrayList;

public class ElementCorrelationInfo {
	private Element element;
	private HashMap<Element, CorrelationInfo> all_correlations;
	private ArrayList<CorrelationInfo> selected_elements;
	
	public ElementCorrelationInfo(Element element, HashMap<Element, CorrelationInfo> all_correlations) {
		this.element = element;
		this.all_correlations = all_correlations;
		this.selected_elements = new ArrayList<CorrelationInfo>();
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
	
	public void add_selected(Element secondary) {
		CorrelationInfo corr = this.all_correlations.get(secondary);
		this.selected_elements.add(corr);
	}
	
	public void remove_selected(Element secondary) {
		this.selected_elements.remove(secondary);
	}
	
	// More to come
	
}