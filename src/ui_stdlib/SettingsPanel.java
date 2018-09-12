package ui_stdlib;

import java.awt.BorderLayout;

import system_utils.DataStore;
import ui_framework.Refreshable;

@SuppressWarnings("serial")
public class SettingsPanel extends ui_framework.SystemPanel {
	ListingSet<RSquaredListElement> r_sqrd_list;
	private DataStore data_store;
	
	public SettingsPanel() {
		super();
		r_sqrd_list = new ListingSet<RSquaredListElement>(RSquaredListElement.class);
		setLayout(new BorderLayout());
		this.add(r_sqrd_list, BorderLayout.WEST);
		this.setVisible(true);
		r_sqrd_list.setVisible(true);
		//TODO: remove testing construction
		add_new_element();
		add_new_element();
		add_new_element();
		add_new_element();
		add_new_element();
		add_new_element();
		add_new_element();
	}
	
	@Override
	public void refresh() {
		r_sqrd_list.refresh();
		//TODO: refresh self
	}
	
	public void add_new_element() {
		try {
			r_sqrd_list.display_new_element();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		r_sqrd_list.refresh();
	}

	@Override
	public void set_datastore(DataStore datastore) {
		this.data_store = datastore;
		r_sqrd_list.set_datastore(datastore);
	}

	@Override
	public void add_refreshable(Refreshable refreshable_window) {
	}

}
