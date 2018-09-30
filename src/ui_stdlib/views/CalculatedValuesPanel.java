package ui_stdlib.views;

import system_utils.DataStore;
import ui_framework.Refreshable;
import ui_stdlib.components.ListingSet;

@SuppressWarnings("serial")
public class CalculatedValuesPanel extends ui_framework.SystemPanel {
	private ListingSet<CalculationListElement> calculated_elements_list;
	private DataStore datastore;
	private boolean backend_loaded = false;
	
	public CalculatedValuesPanel() {
		super();
		calculated_elements_list = new ListingSet<CalculationListElement>(CalculationListElement.class);
	}
	
	@Override
	public void refresh() {
		calculated_elements_list.refresh();
	}

	@Override
	public void set_datastore(DataStore datastore) {
		this.datastore = datastore;
		calculated_elements_list.set_datastore(datastore);
		this.backend_loaded = true;
	}
	
	public void add_new_element() {
		if (this.backend_loaded) {
			try {
				calculated_elements_list.display_new_element();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
			this.datastore.notify_update();
		}
	}

	@Override
	public void add_refreshable(Refreshable refreshable_component) {
	}

	@Override
	public void on_start() {
		calculated_elements_list.on_start();
		calculated_elements_list.setVisible(true);
	}

}
