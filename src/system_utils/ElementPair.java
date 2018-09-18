package system_utils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileReader;
import ui_graphlib.PointSet;

import java.util.HashMap; 
import java.util.ArrayList;

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
	
}
