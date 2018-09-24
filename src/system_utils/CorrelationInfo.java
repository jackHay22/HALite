package system_utils;

import java.io.FileNotFoundException;
import org.apache.commons.math3.stat.regression.SimpleRegression;
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
		
		return new EquationPlot(r_2,1, x_0, x_1);
	}
	
	public void toggle() {
		this.use_in_wm = !this.use_in_wm;
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
	
	// More to come
	
	public EquationPlot get_equation() {
		return equation;
	}
	
}