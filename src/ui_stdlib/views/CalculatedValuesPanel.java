package ui_stdlib.views;

import system_utils.DataStore;
import ui_framework.Refreshable;
import ui_stdlib.components.ListingSet;

public class CalculatedValuesPanel extends ui_framework.SystemPanel {
	private ListingSet<RSquaredListElement> r_sqrd_list;
	public CalculatedValuesPanel() {
		super();
		r_sqrd_list = new ListingSet<RSquaredListElement>(RSquaredListElement.class);
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
	public void add_refreshable(Refreshable refreshable_component) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void on_start() {
		// TODO Auto-generated method stub
		
	}

}
