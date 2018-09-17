package ui_framework;

public interface Refreshable {
	void refresh();
	void set_datastore(system_utils.DataStore datastore);
	void add_refreshable(Refreshable refreshable_component);
	void on_start();
}
