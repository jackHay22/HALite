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
}
