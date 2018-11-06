package ui_framework;

import system_utils.DataStore;

public interface Refreshable {
	void refresh();
	void set_datastore(DataStore datastore);
	void add_refreshable(Refreshable refreshable_component);
	void on_start();
}
