package ui_graphlib;

import java.util.ArrayList;
import system_utils.CorrelationInfo;
import system_utils.DataStore;
import ui_framework.Refreshable;
import ui_stdlib.SystemThemes;

@SuppressWarnings("serial")
public class CorrelationGraph extends ModelGraph {
	//extends SystemPanel 
	
	private CorrelationInfo data_to_plot;
	
	public CorrelationGraph() {
		super();
	}
	@Override
	public void refresh() {
		// Once data store has these we can proceed
		this.data_to_plot = data_store.get_correlation_info();
		ArrayList<String> keys = new ArrayList<String>();
		keys.add("standard");
		keys.add("unknown");
		this.refresh_data(keys);
	}
	
	@Override
	public void set_datastore(DataStore datastore) {
		super.set_datastore(datastore);
	}

	private void refresh_data(ArrayList<String> keys) {
		if (data_to_plot != null) {
			this.data_sets = data_to_plot.get_data();
			this.eqn = data_to_plot.get_equation();
			this.add_point_sets(keys);
			this.graph.refresh();
			set_labels();
			super.set_vals();
			this.graph.refresh();
			this.revalidate();
		}
	}
	
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
		super.on_start();
	}

}