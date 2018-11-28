package ui_framework;

import java.io.Serializable;
import ui_stdlib.CrashReporter;

public abstract class DataBackend implements Serializable {
	private static final long serialVersionUID = 6413502423031756914L;
	transient protected SystemWindow<DataBackend> window_parent;
	
	@SuppressWarnings("unchecked")
	protected <T extends DataBackend> DataBackend(SystemWindow<T> window_parent) {
		this.window_parent = (SystemWindow<DataBackend>) window_parent;
	}
	
	public void notify_update() {
		try {
			window_parent.refresh();
		} catch (Exception e) {
			//prompt user to save stack to log file on an unexpected system error
			CrashReporter.report_crash(window_parent, e);
			
			//system exit
			System.exit(1);
		}
		
	}
	
	@SuppressWarnings("unchecked")
	public <T extends DataBackend> void set_window(SystemWindow<T> window_parent) {
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
	
	public boolean path_assigned() {
		return false;
	}
	
	public boolean check_valid_target() {
		return false;
	}
	
	public String get_path() {
		return null;
	}
	
	public boolean on_export(String file_path, int export_type) {
		return false;
	}
}
