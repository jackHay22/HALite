package ui_graphlib;

import java.io.Serializable;

public class Point implements Serializable {
	private static final long serialVersionUID = -4524503828324923726L;
	private boolean enabled = true;
	private double graph_x;
	private double graph_y;
	private int draw_x;
	private int draw_y;
	
	public Point(double x, double y) {
		this.graph_x = x;
		this.graph_y = y;
	}
	
	public Point(double x, double y, int d_x, int d_y) {
		this.graph_x = x;
		this.graph_y = y;
		this.draw_x = d_x;
		this.draw_y = d_y;
	}
	
	public void toggle() {
		this.enabled = ! this.enabled;
	}
	
	public boolean in_use() {
		return enabled;
	}
	
	public double get_x() {
		return graph_x;
	}
	public double get_y() {
		return graph_y;
	}
	
	public void set_draw_values(int x, int y) {
		this.draw_x = x;
		this.draw_y = y;
	}

	public int get_draw_x() {
		return draw_x;
	}
	
	public int get_draw_y() {
		return draw_y;
	}
	
}
