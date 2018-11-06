package ui_framework;

public abstract class DataBackend extends StateResult {
	protected SystemWindow window_parent;
	protected DataBackend(SystemWindow window_parent) {
		this.window_parent = window_parent;
	}
	
	protected void notify_update() {
		window_parent.refresh();
	}
	
	protected void set_window_parent(SystemWindow window_parent) {
		this.window_parent = window_parent;
	}
}
