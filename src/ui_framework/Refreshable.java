package ui_framework;

public interface Refreshable <Backend extends DataBackend> { 
	void refresh();
	void set_datastore(Backend datastore);
	void add_refreshable(Refreshable<Backend> refreshable_component);
	void on_start();
}
