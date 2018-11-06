package ui_graphlib;

import javax.swing.JPanel;

import system_utils.DataStore;
import ui_framework.DataBackend;
import ui_framework.Refreshable;
import ui_framework.SystemPanel;

@SuppressWarnings("serial")
public class ExportPanel extends SystemPanel {
	private DataStore data_store;
	
	@Override
	public void refresh() {
		// TODO Auto-generated method stub
		
	}

	//@Override
	public void set_datastore(DataStore datastore) {
		this.data_store = datastore;
	}

	@Override
	public void add_refreshable(Refreshable refreshable_component) {
		
	}

	@Override
	public void on_start() {
		
	}

	@Override
	public void set_datastore(DataBackend datastore) {
		// TODO Auto-generated method stub
		
	}

}
