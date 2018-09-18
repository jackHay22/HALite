package system_utils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileReader;
import ui_graphlib.PointSet;
import ui_graphlib.Point;

import java.util.HashMap; 
import java.util.ArrayList;

public class CorrelationInfo {
	private ElementPair data_to_plot;
	private Element secondary_element;
	private EquationPlot equation;
	private boolean use_in_wm;
	
	public CorrelationInfo(ElementPair elements, EquationPlot equation) {
		this.data_to_plot = elements;
		this.secondary_element = elements.get_second();
		this.equation = equation;
		this.use_in_wm = false;
	}
	
	public void toggle() {
		this.use_in_wm = !this.use_in_wm;
	}
	
	public Element get_secondary() {
		return this.secondary_element;
	}
	
	// More to come
	
}