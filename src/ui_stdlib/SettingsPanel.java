package ui_stdlib;

import system_utils.DataStore;
import ui_framework.Refreshable;

@SuppressWarnings("serial")
public class SettingsPanel extends ui_framework.SystemPanel {
	ListingSet<RSquaredListElement> r_sqrd_list;
	private DataStore data_store;
	
	public SettingsPanel() {
		super();
		r_sqrd_list = new ListingSet<RSquaredListElement>(RSquaredListElement.class);
		this.add(r_sqrd_list);
		this.setVisible(true);
		try {
			r_sqrd_list.display_new_element();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		r_sqrd_list.refresh();
		r_sqrd_list.setVisible(true);
	}
	
	@Override
	public void refresh() {
		r_sqrd_list.refresh();
		//TODO: refresh self
	}

	@Override
	public void set_datastore(DataStore datastore) {
		this.data_store = datastore;
	}

	@Override
	public void add_refreshable(Refreshable refreshable_window) {
	}

}
