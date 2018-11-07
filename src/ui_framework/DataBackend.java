package ui_framework;

public abstract class DataBackend extends StateResult {
	protected SystemWindow<DataBackend> window_parent;
	
	protected DataBackend(SystemWindow<DataBackend> window_parent) {
		this.window_parent = window_parent;
	}
	
	public void notify_update() {
		window_parent.refresh();
	}
	
	protected void set_window_parent(SystemWindow<DataBackend> window_parent) {
		this.window_parent = window_parent;
	}
	
	public boolean init_from_file(String file_path) {
		//ds subclasses override (return read status
		return false;
	}
	
	public boolean add_component_filepath(String path) {
		//ds subclasses override (return read status
		return false;
	}
	
	public boolean save_to_filepath(String path) {
		//ds subclasses override (return read status
		return false;
	}
}
