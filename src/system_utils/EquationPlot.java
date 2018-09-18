package system_utils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileReader;
import ui_graphlib.PointSet;
import ui_graphlib.Point;

import java.util.HashMap; 
import java.util.ArrayList;

public class EquationPlot {
	private int degree;
	private ArrayList<Double> coefficients;
	private double r2;

	public EquationPlot(double r2, int degree, double... coefficients) {
		this.coefficients = new ArrayList<Double>(degree + 1);
		int i = 0;
		for (double coeff : coefficients) {
			this.coefficients.set(i, coeff);
			i++;
		}
		this.r2 = r2;
	}
	
	public ArrayList<Point> get_line(Point bottom_left, Point top_right) {
		// This does trust that the method user knows what they're doing with this method
		
		ArrayList<Point> points = new ArrayList<Point>();
		
		// We give out either the endpoints of a line or a group of points which will represent a curve.
		if (degree == 1) {
			// Generate the end points of the line
			
		} else {
			// Generate the large number of points which will be plotted to represent a line
			
		}
		
		return points;
		
	}
	
}