package ui_graphlib;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.JButton;
import system_utils.DataStore;
import system_utils.ElementCorrelationInfo;
import ui_framework.Refreshable;
import ui_stdlib.SystemThemes;

@SuppressWarnings("serial")
public class ModelGraph extends BaseGraph<DataStore> {
	//extends SystemPanel 
	private ElementCorrelationInfo data_to_plot;
	protected HashMap<String, PointSet<DataStore>> data_sets;

	private Point line_min;
	private Point line_max;
	
	private double x_ratio;
	private double y_ratio;
	
	private int draw_width;
	private int draw_height;

	private double bottom_buffer_x;
	private double bottom_buffer_y;

	protected JButton toggle_unknowns;
	
	public ModelGraph() {
		super();
		this.setLayout(new GridBagLayout());
		this.constraints = SystemThemes.get_grid_constraints();
		this.graph = new GraphPanel<DataStore>(450, 250);
		this.graph.setBackground(SystemThemes.BACKGROUND);
		toggle_unknowns = new JButton("Toggle Unknowns");
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
		// Pulls the relevant data for the new points to display
		set_data_to_plot(this.data_store.get_model_data_corr());
		ArrayList<String> keys = new ArrayList<String>();
		keys.add("standard");
		refresh_data(keys);
	}
	
	protected void set_data_to_plot(ElementCorrelationInfo info) {
		this.data_to_plot = info;
	}
	
	private void refresh_data(ArrayList<String> keys) {
		if (data_to_plot != null) {
			this.data_sets = data_to_plot.get_pointsets();
			this.eqn = data_to_plot.get_equation();
			this.add_point_sets(keys);
			this.graph.refresh();
			set_labels();
			set_vals();
			this.graph.refresh();
			this.revalidate();
		}
	}
	
	protected void add_point_sets(ArrayList<String> keys) {
		ArrayList<PointSet<DataStore>> point_sets = new ArrayList<PointSet<DataStore>>();
		for (String key : keys) {
			point_sets.add(data_sets.get(key));
		}
		this.graph.set_point_sets(point_sets);
	}
	
	protected void set_vals() {
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
	public void add_refreshable(Refreshable<DataStore> refreshable_window) {
		// TODO Auto-generated method stub
		
	}
	
	protected void set_constraints() {
		this.constraints.gridx = 0;
		this.constraints.gridy = 0;
		constraints.weighty = 1;
		constraints.gridwidth = 3;
		constraints.fill = GridBagConstraints.BOTH;
		this.add(this.graph, constraints);
		constraints.weighty = 0;
		constraints.gridwidth = 1;
		this.constraints.gridx = 1;
		this.constraints.gridy = 1;
		constraints.weightx = 0;
	}
	
	protected void add_toggle_button() {
		this.add(toggle_unknowns, constraints);
	}
	
	private void set_labels() {
		PointSet<DataStore> models = data_to_plot.get_model_plot();
		String s = models.get_y_label();
		this.graph.set_y_label(s);
		s = models.get_x_label();
		this.graph.set_x_label(s);
		s = "r²: " + SystemThemes.get_display_number(data_to_plot.get_equation().get_r2(), "#.00000") + "    ||    " + data_to_plot.get_equation().get_str_rep();
		this.graph.set_r2_eqn_label(s);
		s = models.get_title();
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