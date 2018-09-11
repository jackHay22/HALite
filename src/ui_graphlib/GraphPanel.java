package ui_graphlib;

import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import system_utils.DataStore;
import ui_framework.Refreshable;

@SuppressWarnings("serial")
public class GraphPanel extends ui_framework.SystemPanel implements DrawableManager {
	//extends SystemPanel 
	
	private ArrayList<PointSet> point_sets;
	private DrawablePanel points_panel;
	
	public GraphPanel() {
		super();
		
		this.points_panel = new DrawablePanel(this);
		// Place Drawable Panel as a JPanel
		
		
		
	}
	
	private void draw_graph() {
		
		set_labels();
		
	}
	
	private void place_point(Point p, Graphics2D g) {
		
	}
	
	private void plot_points(Graphics2D g) {
		for (int i = 0; i <= point_sets.size(); i++) {
			if (point_sets.get(i).do_render()) {
				ArrayList<Point> points = point_sets.get(i).get_points();
				for (int point_index = 0; point_index < points.size(); i++) {
					place_point(points.get(point_index), g);
				}
			}
		}
	}
	
	private void set_labels() {
		
	}
	
	@Override
	public void refresh() {
		// TODO Auto-generated method stub
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
		// TODO Auto-generated method stub
		plot_points(g);
	}

	@Override
	public void handle_mouse_event(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

}