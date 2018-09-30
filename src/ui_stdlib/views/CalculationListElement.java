package ui_stdlib.views;

import system_utils.DataStore;
import ui_framework.Refreshable;

@SuppressWarnings("serial")
public class CalculationListElement extends ui_framework.SystemPanel {
	private DataStore datastore;
	private boolean backend_loaded = false;
	
	public CalculationListElement() {
		super();
	}
	@Override
	public void refresh() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void set_datastore(DataStore datastore) {
		this.datastore = datastore;
		this.backend_loaded = true;
	}

	@Override
	public void add_refreshable(Refreshable refreshable_component) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void on_start() {
		// TODO Auto-generated method stub
		
	}

}
