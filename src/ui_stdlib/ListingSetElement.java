package ui_stdlib;

import javax.swing.JPanel;

@SuppressWarnings("serial")
public abstract class ListingSetElement extends JPanel implements ui_framework.Refreshable {
	protected system_utils.DataStore data_store;
	public ListingSetElement() {
		super();
	}
	public void set_datastore(system_utils.DataStore contents) {
		data_store = contents;
	}
}
