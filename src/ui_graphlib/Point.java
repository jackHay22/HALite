package ui_graphlib;

public class Point {
	
	private boolean enabled = true;
	private double graph_x;
	private double graph_y;
	
	public Point(double x, double y) {
		this.graph_x = x;
		this.graph_y = y;
	}
	
	
	private void toggle() {
		this.enabled = ! this.enabled;
	}
}
