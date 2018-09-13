package ui_graphlib;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.math.*;
import system_utils.DataStore;
import ui_framework.Refreshable;

@SuppressWarnings("serial")
public class GraphPanel extends ui_framework.SystemPanel implements DrawableManager {
	//extends SystemPanel 
	
	private ArrayList<PointSet> point_sets;
	private DrawablePanel points_panel;
	
	private int draw_width;
	private int draw_height;
	private double x_ratio;
	private double y_ratio;
	
	private int min_x = 0;
	private int max_x = 0;
	
	private int min_y = 0;
	private int max_y = 0;

	private double bottom_buffer_x;
	private double top_buffer_x;
	private double bottom_buffer_y;
	private double top_buffer_y;
	
	
	public GraphPanel() {
		super();
		this.setPreferredSize(new Dimension(450, 250));
		this.points_panel = new DrawablePanel(this, 450, 250);
		this.add(points_panel);
		set_fake_vals();
	}
	
	private void set_fake_vals() {

		ArrayList<Point> lst = new ArrayList<Point>();
		
		for (int i = 20; i <= 100; i++) {
			Point temp = new Point(i,i);
			lst.add(temp);
		}
		
		PointSet set = new PointSet(lst, Color.red, "x_axis", "y_axis", "x vs y", true);
		
		point_sets = new ArrayList<PointSet>();
		
		point_sets.add(set);
		
	}
	
	private void draw_graph() {
		set_labels();
		set_ratio();
	}
	
	private void set_ratio() {
		
		for (int i = 0; i < point_sets.size(); i++) {

			ArrayList<Point> points = point_sets.get(i).get_points();
			
			for (int j = 0; j < points.size(); j++) {
				if (points.get(j).get_x() < min_x) {
					min_x = (int)Math.floor(points.get(j).get_x());
				}
				if (points.get(j).get_y() < min_y) {
					min_y = (int)Math.floor(points.get(j).get_y());
				}
				if (points.get(j).get_x() < max_x) {
					max_x = (int)Math.ceil(points.get(j).get_x());
				}
				if (points.get(j).get_y() < max_y) {
					max_y = (int)Math.ceil(points.get(j).get_y());
				}
			}
		}

		// These are used to create a buffer around the points placed on the graphs
		bottom_buffer_x = bottom_buffer(min_x, max_x);
		top_buffer_x = top_buffer(min_x, max_x);
		bottom_buffer_y = bottom_buffer(min_y, max_y);
		top_buffer_y = top_buffer(min_y, max_y);
		
		x_ratio = draw_width/(top_buffer_x - bottom_buffer_x);
		y_ratio = draw_height/(top_buffer_y - bottom_buffer_y);
		
	}
	
	// The buffer is meant to be 5% on either side, thus / by 20
	private double bottom_buffer(int min, int max) {
		return min - (max - min)/20;
	}

	private double top_buffer(int min, int max) {
		return max + (max - min)/20;
	}
	
	private void place_point(Point p, Graphics2D g) {
		double point_x = p.get_x();
		double draw_x = (point_x - bottom_buffer_x)*x_ratio;
		
		double point_y = p.get_y();
		double draw_y = draw_height - (point_y - bottom_buffer_y)*y_ratio;

		//TODO: draw x and draw y aren't numbers
		// Place the actual point with coords (draw_x, draw_y)
		g.drawOval((int)draw_x, (int)draw_y, 3, 3);
		
	}
	
	private void plot_points(Graphics2D g) {
		for (int i = 0; i < point_sets.size(); i++) {
			if (point_sets.get(i).do_render()) {
				ArrayList<Point> points = point_sets.get(i).get_points();
				for (int point_index = 0; point_index < points.size(); point_index++) {
					place_point(points.get(point_index), g);
				}
			}
		}
	}
	
	private void set_labels() {
		
	}
	
	private void point_selected(MouseEvent e) {
		int x = e.getX();
		int y = e.getY();
		Point closest = find_closest_point(x,y);
		double distance_to_point = distance(closest.get_draw_x(), closest.get_draw_y(), x, y);
		
		// If the mouse click was within 4% of the screen diagonal from the point
		if (distance_to_point < distance(draw_width, draw_height, 0, 0)*.04) {
			// Here we should call back to the datastore and let it know this point
			// will be toggled - then refresh everything
			closest.toggle();
		}
	}
	
	private Point find_closest_point(int x, int y) {
		
		// This is only initialized to silence warnings
		Point closest = new Point(0,0);
		
		// This will always be larger than the distance btwn the mouse click and a point
		double distance = draw_width + draw_height;
		
		// Sort through all the points on the graph and find the closest one
		for (int i = 0; i <= point_sets.size(); i++) {
			if (point_sets.get(i).do_render()) {
				ArrayList<Point> points = point_sets.get(i).get_points();
				for (int point_index = 0; point_index < points.size(); i++) {
					Point point = points.get(point_index);
					double new_d = distance(point.get_draw_x(), point.get_draw_y(), x, y);
					if (new_d < distance) {
						closest = point;
						distance = new_d;
					}
				}
			}
		}
		return closest;
	}
	
	private double distance(int x_1, int y_1, int x_2, int y_2) {
		return Math.sqrt(Math.pow((x_1 - x_2), 2) + Math.pow((y_1 - y_2), 2));
	}
	
	@Override
	public void refresh() {
		draw_graph();
		points_panel.refresh();
	}

	@Override
	public void set_datastore(DataStore datastore) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void add_refreshable(Refreshable refreshable_window) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void draw_components(Graphics2D g) {
		plot_points(g);
	}

	@Override
	public void handle_mouse_event(MouseEvent e) {
		point_selected(e);
	}

	@Override
	public void on_start() {
		points_panel.on_start();
		this.setVisible(true);
		points_panel.setVisible(true);
	}
}