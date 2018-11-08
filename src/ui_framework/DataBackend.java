package ui_framework;

public abstract class DataBackend {
	protected SystemWindow<DataBackend> window_parent;
	
	@SuppressWarnings("unchecked")
	protected <T extends DataBackend> DataBackend(SystemWindow<T> window_parent) {
		this.window_parent = (SystemWindow<DataBackend>) window_parent;
	}
	
	public void notify_update() {
		window_parent.refresh();
	}
	
	@SuppressWarnings("unchecked")
	protected <T extends DataBackend> void set_window_parent(SystemWindow<T> window_parent) {
		this.window_parent = (SystemWindow<DataBackend>) window_parent;
	}
	
	public boolean init_from_file(String file_path) {
		//ds subclasses override (return read status)
		return false;
	}
	
	public boolean add_component_filepath(String path, String label) {
		//ds subclasses override (return read status
		return false;
	}
	
	public boolean save_to_filepath(String path) {
		//ds subclasses override (return read status
		return false;
	}
}
