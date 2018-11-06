package system_utils;

import ui_graphlib.PointSet;

import java.io.Serializable;
import java.util.HashMap;

import ui_framework.DataBackend; 

public class ElementPair<Backend extends DataBackend> implements Serializable {
	private static final long serialVersionUID = 6;
	private Element x_element;
	private Element y_element;
	private PointSet<Backend> standards;
	private PointSet<Backend> unknowns;

	public ElementPair(Element x_element, Element y_element, PointSet<Backend> standards, PointSet<Backend> unknowns) {
		this.x_element = x_element;
		this.y_element = y_element;
		this.standards = standards;
		this.unknowns = unknowns;
	}
	
	public PointSet<Backend> get_standards() {
		return this.standards;
	}
	
	public PointSet<Backend> get_unknowns() {
		return this.unknowns;
	}
	
	public Element get_main() {
		return this.x_element;
	}
	
	public Element get_second() {
		return this.y_element;
	}
	
	public HashMap<String, PointSet<Backend>> get_points() {
		HashMap<String, PointSet<Backend>> points = new HashMap<String, PointSet<Backend>>();
		points.put("Standards", this.standards);
		points.put("Unknowns", this.unknowns);
		return points;
	}
	
}
