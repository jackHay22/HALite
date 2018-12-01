package ui_graphlib;

import java.awt.GridBagConstraints;
import system_utils.EquationPlot;
import ui_framework.DataBackend;
import ui_framework.Refreshable;

@SuppressWarnings("serial")
public class BaseGraph<Backend extends DataBackend> extends ui_framework.SystemPanel<Backend> {

	
	protected Backend data_store;
	protected GraphPanel<Backend> graph;
	protected GridBagConstraints constraints;
	protected EquationPlot eqn;
	
	// This return the drawable panel to be converted into an image
	public DrawablePanel<Backend> get_points_panel() {
		return this.graph.get_points_panel();
	}
	
	public BaseGraph() {
		super();
	}
	
	@Override
	public void refresh() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void set_datastore(Backend datastore) {
		// TODO Auto-generated method stub
		this.data_store = datastore;
	}

	@Override
	public void add_refreshable(Refreshable<Backend> refreshable_component) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void on_start() {
		// TODO Auto-generated method stub
		
	}

}
