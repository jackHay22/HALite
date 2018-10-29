package system_utils;

import ui_graphlib.PointSet;

import java.util.HashMap; 

public class ElementPair {
	private Element x_element;
	private Element y_element;
	private PointSet standards;
	private PointSet unknowns;

	public ElementPair(Element x_element, Element y_element, PointSet standards, PointSet unknowns) {
		this.x_element = x_element;
		this.y_element = y_element;
		this.standards = standards;
		this.unknowns = unknowns;
	}
	
	public PointSet get_standards() {
		return this.standards;
	}
	
	public PointSet get_unknowns() {
		return this.unknowns;
	}
	
	public Element get_main() {
		return this.x_element;
	}
	
	public Element get_second() {
		return this.y_element;
	}
	
	public HashMap<String, PointSet> get_points() {
		HashMap<String, PointSet> points = new HashMap<String, PointSet>();
		points.put("Standards", this.standards);
		points.put("Unknowns", this.unknowns);
		return points;
	}
	
	public String to_string() {
		String formatted = "[";
		
		formatted += "x_element=" + this.x_element.name();
		formatted += ", ";
		formatted += "y_element=" + this.y_element.name();
		formatted += ", ";
		
		formatted += "standards=" + this.standards.to_string();
		formatted += ", ";
		formatted += "unknowns=" + this.unknowns.to_string();
		formatted += "]";
		
		return formatted;
	}
	
}
