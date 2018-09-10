package ui_graphlib;

import java.util.ArrayList;

@SuppressWarnings("serial")
public class GraphPanel extends ui_framework.SystemPanel {
	//extends SystemPanel 
	
	private int width;
	private int length;
	private ArrayList<PointSet> point_sets;
	
	public GraphPanel() {
		//super();
	}
	
	private void draw_graph() {
		set_labels();
		plot_points();
	}
	
	private void plot_points() {
		
	}
	
	private void set_labels() {
		
	}
	
	@Override
	public void refresh() {
		// TODO Auto-generated method stub
		draw_graph();
	}

}