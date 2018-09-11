package ui_stdlib;

import java.util.ArrayList;

import system_utils.DataStore;
import ui_framework.Refreshable;

@SuppressWarnings("serial")
public class SettingsPanel extends ui_framework.SystemPanel{
	private ArrayList<Refreshable> refreshable_objects;
	public SettingsPanel() {
		super();
		refreshable_objects = new ArrayList<Refreshable>();
	}
	
	@Override
	public void refresh() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void set_datastore(DataStore datastore) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void add_refreshable(Refreshable refreshable_window) {
		refreshable_objects.add(refreshable_window);
		
	}

}
