package ui_graphlib;

import java.util.ArrayList;

import system_utils.DataStore;

import java.awt.Color;
import java.io.Serializable;

import ui_framework.Refreshable;

public class PointSet implements Refreshable, Serializable {
	
	private ArrayList<Point> points;
	
	private double min_x;
	private double max_x;
	private double min_y;
	private double max_y;
	
	private String x_axis;
	private String y_axis;
	private String title;
	
	private Color color;
	
	private boolean render;
	
	public PointSet(ArrayList<Point> points, Color color, String x_axis, String y_axis, String title_label, boolean do_render) {
		this.points = points;
		this.color = color;
		this.x_axis = x_axis;
		this.y_axis = y_axis;
		this.title = title_label;
		this.render = do_render;
	}
	
	private void edge_values() {
		// Finds the smallest and largest x and y values to be used to create an
		// appropriate scale for plotting the points
		int min_x_index = 0;
		int max_x_index = 0;
		int min_y_index = 0;
		int max_y_index = 0;
		
		for(int i = 0; i < points.size(); i++) {
			if (points.get(min_x_index).get_x() > points.get(i).get_x()) {
				min_x_index = i;
			}
			if (points.get(max_x_index).get_x() < points.get(i).get_x()) {
				max_x_index = i;
			}
			if (points.get(min_y_index).get_y() > points.get(i).get_y()) {
				min_y_index = i;
			}
			if (points.get(max_y_index).get_y() < points.get(i).get_y()) {
				max_y_index = i;
			}
		}
		min_x = points.get(min_x_index).get_x();
		max_x = points.get(max_x_index).get_x();
		min_y = points.get(min_y_index).get_y();
		max_y = points.get(max_y_index).get_y();
	}

	public double get_min_x() {
		return min_x;
	}
	
	public double get_min_y() {
		return min_y;
	}
	
	public double get_max_x() {
		return max_x;
	}
	
	public double get_max_y() {
		return max_y;
	}
	
	public Color get_color() {
		return this.color;
	}
	
	public void set_color(Color c) {
		this.color = c;
	}
	
	public String get_title() {
		return this.title;
	}
	
	public String get_x_label() {
		return this.x_axis;
	}
	
	public String get_y_label() {
		return this.y_axis;
	}
	
	@Override
	public void refresh() {
		// TODO Auto-generated method stub
		
		edge_values();
		
	}
	
	public boolean do_render() {
		return render;
	}
	
	public void toggle_render() {
		this.render = !this.render;
	}

	public ArrayList<Point> get_points() {
		return points;
	}
	
	@Override
	public void set_datastore(DataStore datastore) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void add_refreshable(Refreshable refreshable_component) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void on_start() {
		refresh();
	}
	
}
