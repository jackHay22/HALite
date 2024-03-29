package system_utils;

import java.io.Serializable;
import java.text.DecimalFormat;

import java.util.ArrayList;

import ui_stdlib.SystemThemes;

public class EquationPlot implements Serializable {
	private static final long serialVersionUID = 1;
	private int degree;
	private ArrayList<Double> coefficients;
	private double r2;
	private Double rmse;

	public EquationPlot(double r2, int degree, double... coefficients) {
		// Enter the coefficients from x_0 ... x_n
		this.coefficients = new ArrayList<Double>();
		for (double coeff : coefficients) {
			this.coefficients.add(coeff);
		}
		this.r2 = r2;
		this.degree = degree;
		this.set_rmse(null);
	}
	
	// Takes the inverse of the equation, but only if it is linear
	public double get_linear_x(double y) {
		if (degree == 1) {
			return (y - coefficients.get(0)) / coefficients.get(1);
		} else {
			return (y - coefficients.get(0));
		}
	}
	
	// This outputs the y value for the provided x value
	public double get_y(double x) {
		double y = 0;
		for (int i = degree; i >= 0; i--) {

			y += Math.pow(x, i)*coefficients.get(i);
		}
		return y;
	}
	
	public void set_r2(double new_r2) {
		this.r2 = new_r2;
	}
	
	public double get_r2() {
		return this.r2;
	}
	
	public int get_degree() {
		return degree;
	}
	
	public double get_coeff(int i) {
		return this.coefficients.get(i);
	}
	
	// Provides a string representation of the equation
	public String get_str_rep() {

        DecimalFormat df = new DecimalFormat("#.####");
		
		String s = df.format(coefficients.get(0));
		for (int i = degree; i > 0; i--) {
			s = s + "+ x" + SystemThemes.superscript(Integer.toString(i)) + "*" + df.format(coefficients.get(i));
		}
		return s;
	}

	public Double get_rmse() {
		return rmse;
	}

	public void set_rmse(Double rmse) {
		this.rmse = rmse;
	}
	
}