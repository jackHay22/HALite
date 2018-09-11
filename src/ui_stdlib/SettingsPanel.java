package ui_stdlib;

import java.util.ArrayList;

import system_utils.DataStore;
import ui_framework.Refreshable;

@SuppressWarnings("serial")
public class SettingsPanel extends ui_framework.SystemPanel{
	private ArrayList<Refreshable> refreshable_objects;
	private DataStore data_store;
	
	public SettingsPanel() {
		super();
		refreshable_objects = new ArrayList<Refreshable>();
	}
	
	@Override
	public void refresh() {
		for (int i = 0; i < this.refreshable_objects.size(); i++) {
			this.refreshable_objects.get(i).refresh();
		}
		//TODO: refresh self
		System.out.println("refreshing settings panel");
	}

	@Override
	public void set_datastore(DataStore datastore) {
		this.data_store = datastore;
	}

	@Override
	public void add_refreshable(Refreshable refreshable_window) {
		refreshable_objects.add(refreshable_window);
		
	}

}
