package ui_graphlib;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.math.*;
import system_utils.DataStore;
import ui_framework.Refreshable;
import ui_stdlib.PanelHeader;
import ui_stdlib.SystemThemes;
import ui_stdlib.VerticalPanel;

@SuppressWarnings("serial")
public class GraphPanel extends ui_framework.SystemPanel implements DrawableManager, MouseListener {
	//extends SystemPanel 
	
	private ArrayList<PointSet> point_sets;
	private DrawablePanel points_panel;
	private PanelHeader header_panel;
	private PanelHeader r_sqrd_panel;
	private VerticalPanel y_label;
	private PanelHeader x_label;
	
	private GridBagConstraints constraints;
	private DataStore data_store;
	
	
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
	
	private Point left_point;
	private Point right_point;
	private boolean do_place_line;
	
	public GraphPanel(int width, int height) {
		super();
		this.points_panel = new DrawablePanel(this, 450, 250);
		
		setLayout(new GridBagLayout());
		this.constraints = SystemThemes.get_grid_constraints();
		this.header_panel = new PanelHeader("TITLE", SystemThemes.MAIN);
		this.r_sqrd_panel = new PanelHeader("EQUATION HERE", SystemThemes.MAIN);
		this.y_label = new VerticalPanel("test", SystemThemes.MAIN);
		this.x_label = new PanelHeader("x label", SystemThemes.MAIN);
		
		this.draw_width = width;
		this.draw_height = height;
		set_fake_vals();
	}

	public double get_x_r() {
		return x_ratio;
	}
	
	public double get_y_r() {
		return y_ratio;
	}
	
	public int get_width() {
		return draw_width;
	}
	
	public int get_height() {
		return draw_height;
	}
	
	public Point get_left_bottom() {
		Point p = new Point(min_x, min_y);
		return p;
	}
	
	public Point get_right_top() {
		Point p = new Point(max_x, max_y);
		return p;
	}
	
	public void set_endpoints(Point start, Point end) {
		left_point = start;
		right_point = end;
		do_place_line = true;
		points_panel.refresh();
	}
	
	private void set_fake_vals() {

		ArrayList<Point> lst = new ArrayList<Point>();
		
		for (int i = 10; i <= 31; i++) {
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

			if ((i == 0) && points.size() > 0) {
				min_x = (int)Math.floor(points.get(0).get_x());
				max_x = min_x;
				min_y = (int)Math.floor(points.get(0).get_y());
				max_y = min_y;
			}
			
			for (int j = 0; j < points.size(); j++) {
				if (points.get(j).get_x() < min_x) {
					min_x = (int)Math.floor(points.get(j).get_x());
				}
				if (points.get(j).get_y() < min_y) {
					min_y = (int)Math.floor(points.get(j).get_y());
				}
				if (points.get(j).get_x() > max_x) {
					max_x = (int)Math.ceil(points.get(j).get_x());
				}
				if (points.get(j).get_y() > max_y) {
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
	
	private void place_line(Graphics2D g) {
		g.drawLine((int)this.left_point.get_x(), (int)this.left_point.get_y(), (int)this.right_point.get_x(), (int)this.right_point.get_x());
	}
	
	private void place_point(Point p, Graphics2D g) {
		double point_x = p.get_x();
		double draw_x = (point_x - bottom_buffer_x)*x_ratio;
		
		double point_y = p.get_y();
		double draw_y = draw_height - (point_y - bottom_buffer_y)*y_ratio;

		p.set_draw_values((int)draw_x, (int)draw_y);
		
		//TODO: draw x and draw y aren't numbers
		// Place the actual point with coords (draw_x, draw_y)
		g.setColor(SystemThemes.BACKGROUND);
		if (p.in_use()) {
			g.setColor(SystemThemes.HIGHLIGHT);
		}
		g.drawOval((int)draw_x, (int)draw_y, 6,6);
		
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
		// TODO: Jack needs to set this up
	}
	
	private void point_selected(MouseEvent e) {
		int x = e.getX();
		int y = e.getY();
		Point closest = find_closest_point(x,y);
		double distance_to_point = distance(closest.get_draw_x(), closest.get_draw_y(), x, y);
		// If the mouse click was within 4% of the screen diagonal from the point
		
		if (distance_to_point < distance(draw_width, draw_height, 0, 0)*0.06) {
			closest.toggle();
		}

	}
	
	private Point find_closest_point(int x, int y) {
		
		// This is only initialized to silence warnings
		Point closest = new Point(0,0);
		
		// This will always be larger than the distance btwn the mouse click and a point
		double distance = draw_width + draw_height;

		// Sort through all the points on the graph and find the closest one
		for (int i = 0; i < point_sets.size(); i++) {
			if (point_sets.get(i).do_render()) {
				ArrayList<Point> points = point_sets.get(i).get_points();
				for (int point_index = 0; point_index < points.size(); point_index++) {
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
		this.draw_graph();
		this.points_panel.refresh();
		this.revalidate();
	}

	@Override
	public void set_datastore(DataStore datastore) {
		// TODO Auto-generated method stub
		this.data_store = datastore;
	}

	@Override
	public void add_refreshable(Refreshable refreshable_window) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void draw_components(Graphics2D g) {
		plot_points(g);
		if (do_place_line) {
			place_line(g);
		}
	}

	@Override
	public void handle_mouse_event(MouseEvent e) {
		point_selected(e);
		data_store.notify_update();
	}

	@Override
	public void on_start() {
		this.constraints.anchor = GridBagConstraints.NORTH;
		this.constraints.gridwidth = 2;
		this.add(this.header_panel, constraints);

		this.constraints.gridy = 1;
		this.constraints.gridx = 1;
		this.constraints.gridwidth = 1;
		this.add(this.r_sqrd_panel, constraints);
		
		this.constraints.weighty = 1;
		this.constraints.gridy = 2;
		this.constraints.weightx = 0.2;
		this.constraints.gridx = 0;
		this.add(this.y_label, constraints);
		this.constraints.weightx = 0.8;
		this.constraints.gridx = 1;
		this.add(points_panel, constraints);
		
		this.constraints.gridy = 3;
		this.constraints.anchor = GridBagConstraints.SOUTH;
		this.add(this.x_label, constraints);
		
		this.points_panel.on_start();
		this.header_panel.on_start();
		this.r_sqrd_panel.on_start();
		this.y_label.on_start();
		this.x_label.on_start();
		
		points_panel.setVisible(true);
		this.header_panel.setVisible(true);
		this.r_sqrd_panel.setVisible(true);
		this.y_label.setVisible(true);
		this.x_label.setVisible(true);
		this.setVisible(true);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
}