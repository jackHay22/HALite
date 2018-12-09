package ui_graphlib;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import system_utils.CorrelationInfo;
import system_utils.DataStore;
import ui_framework.Refreshable;
import ui_stdlib.SystemThemes;

@SuppressWarnings("serial")
public class CorrelationGraph extends ModelGraph {
	//extends ModelGraph 
	
	// Holds the CorrelationInfo object which contains the 
	// data to be displayed on the graph.
	private CorrelationInfo data_to_plot;
	
	public CorrelationGraph() {
		super();
	}
	
	@Override
	public void refresh() {
		// Once data store has these we can proceed
		this.data_to_plot = data_store.get_correlation_info();
		// These are the hash keys of the pointsets to plot in the graph
		ArrayList<String> keys = new ArrayList<String>();
		keys.add("standard");
		keys.add("unknown");
		this.refresh_data(keys);
	}
	
	@Override
	public void set_datastore(DataStore datastore) {
		super.set_datastore(datastore);
	}

	// Retrieves the data to be plotted from datastore, and 
	// sets the associated fields within the graph and panels
	private void refresh_data(ArrayList<String> keys) {
		if (data_to_plot != null) {
			this.data_sets = data_to_plot.get_data();
			this.eqn = data_to_plot.get_equation();
			// Tells us to look for pointsets with the hash keys from this
			// array list
			this.add_point_sets(keys);
			this.graph.refresh();
			set_labels();
			super.set_vals();
			this.graph.refresh();
			this.revalidate();
		}
	}
	
	// Sets the labels on the graph panel
	private void set_labels() {
		String s = data_to_plot.get_primary().name();
		this.graph.set_y_label(s);
		s = data_to_plot.get_secondary().name();
		this.graph.set_x_label(s);
		s = "r²: " + SystemThemes.get_display_number(data_to_plot.get_r2(), "#.00000") + "    ||    " + data_to_plot.get_equation().get_str_rep();
		this.graph.set_r2_eqn_label(s);
		s = "Correlation Graph";
		this.graph.set_title(s);
	}
	
	@Override
	public void add_refreshable(Refreshable<DataStore> refreshable_window) {
		// TODO Auto-generated method stub
		super.add_refreshable(refreshable_window);
	}
	
	protected void set_constraints() {
		super.set_constraints();
		super.add_toggle_button();
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