package ui_graphlib;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.math.*;
import java.text.DecimalFormat;

import system_utils.DataStore;
import system_utils.ElementCorrelationInfo;
import system_utils.EquationPlot;
import ui_framework.Refreshable;
import ui_stdlib.SystemThemes;
import ui_stdlib.components.ImageRadioButton;
import system_utils.CorrelationInfo;;

@SuppressWarnings("serial")
public class CorrelationGraph extends ui_framework.SystemPanel {
	//extends SystemPanel 
	private DataStore data_store;
	private GraphPanel graph;
	private CorrelationInfo data_to_plot;
	private HashMap<String, PointSet> data_sets;
	private EquationPlot eqn;

	private Point line_min;
	private Point line_max;
	
	private double x_ratio;
	private double y_ratio;
	
	private int draw_width;
	private int draw_height;

	private double bottom_buffer_x;
	private double bottom_buffer_y;
	
	private ImageRadioButton toggle_unknowns;
	private GridBagConstraints constraints;
	
	public CorrelationGraph() {
		super();
		this.setLayout(new GridBagLayout());
		this.constraints = SystemThemes.get_grid_constraints();
		this.graph = new GraphPanel(450, 250);
		this.graph.setBackground(SystemThemes.BACKGROUND);
		toggle_unknowns = new ImageRadioButton("/buttons/blank_button.png");
	}
	
	private void set_line_endpoints() {
		double x = 0;
		double y = g(for_y(f_inv(0)));

		if (y < 0) {
			y = 0;
			x = f(for_x(g_inv(0)));

		}
		line_min = new Point(x, draw_height - y);
		
		x = draw_width;
		y = g(for_y(f_inv(x)));
		if (y > draw_height) {
			y = draw_height;
			x = f(for_x(g_inv(y)));
		}
		line_max = new Point(x, draw_height - y);
		
		graph.set_endpoints(line_min, line_max);

	}
	
	private double f_inv(double x) {
		return x/x_ratio + bottom_buffer_x;
	}
	
	private double f(double x) {
		return (x - bottom_buffer_x) * x_ratio;
	}
	
	private double g_inv(double y) {
		return y/y_ratio + bottom_buffer_y;
	}
	
	private double g(double y) {
		return (y - bottom_buffer_y) * y_ratio;
	}
	
	private double for_y(double x) {
		// This applies the algo to place points to the entire equation of the correlation model
		return eqn.get_y(x); 
	}
	
	private double for_x(double y) {
		return eqn.get_linear_x(y);
	}
	
	@Override
	public void refresh() {
		// Once data store has these we can proceed
		data_to_plot = data_store.get_correlation_info();
		data_sets = data_to_plot.get_data();
		this.eqn = data_to_plot.get_equation();
		ArrayList<PointSet> point_sets = new ArrayList<PointSet>();
		point_sets.add(data_sets.get("standard"));
		point_sets.add(data_sets.get("unknown"));
		this.graph.set_point_sets(point_sets);
		this.graph.refresh();
		set_labels();
		set_vals();
		this.graph.refresh();
		this.revalidate();
	}
	
	private void set_vals() {
		this.draw_width = graph.get_width();
		this.draw_height = graph.get_height();
		this.x_ratio = graph.get_x_r();
		this.y_ratio = graph.get_y_r();
		this.bottom_buffer_x = graph.get_bbx();
		this.bottom_buffer_y = graph.get_bby();
		set_line_endpoints();
	}
	
	public void toggle_unknowns() {
		data_sets.get("unknown").toggle_render();
		data_store.notify_update();
	}
	
	@Override
	public void set_datastore(DataStore datastore) {
		this.data_store = datastore;
		this.graph.set_datastore(datastore);
	}

	@Override
	public void add_refreshable(Refreshable refreshable_window) {
		// TODO Auto-generated method stub
		
	}
	
	private void set_constraints() {
		this.constraints.gridx = 0;
		this.constraints.gridy = 0;
		constraints.weighty = 1;
		constraints.gridwidth = 2;
		constraints.fill = GridBagConstraints.BOTH;
		this.add(this.graph, constraints);
		constraints.weighty = 0;
		constraints.gridwidth = 1;
		this.constraints.gridx = 1;
		this.constraints.gridy = 1;
		//constraints.anchor = GridBagConstraints.EAST;
		this.add(toggle_unknowns, constraints);
		
	}

	private String get_display_number(Double val) {
		DecimalFormat df = new DecimalFormat("#.000");
		return df.format(val);
	}
	
	private void set_labels() {
		String s = data_to_plot.get_primary().name();
		this.graph.set_y_label(s);
		s = data_to_plot.get_secondary().name();
		this.graph.set_x_label(s);
		s = "r^2: " + get_display_number(data_to_plot.get_r2()) + "    ||    " + data_to_plot.get_equation().get_str_rep();
		this.graph.set_r2_eqn_label(s);
		s = "Correlation Graph";
		this.graph.set_title(s);
	}
	
	@Override
	public void on_start() {
		this.graph.on_start();
		set_constraints();
		toggle_unknowns.addActionListener(new ActionListener() {
		    @Override
		    public void actionPerformed(ActionEvent e) {
		        toggle_unknowns();
		    }
		});
		this.refresh();
	}

}