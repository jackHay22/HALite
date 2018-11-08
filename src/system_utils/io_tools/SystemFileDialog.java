package system_utils.io_tools;

import java.awt.FileDialog;
import java.io.File;

import ui_framework.DataBackend;
import ui_framework.SystemWindow;
import ui_stdlib.dialogwindows.SystemDialog;

public class SystemFileDialog<Backend extends DataBackend> {
	private FileDialog system_file_dialog;
	
	public SystemFileDialog(SystemDialog dialog_parent, String title) {
		system_file_dialog = new FileDialog(dialog_parent, title);
	}
	
	public SystemFileDialog(SystemWindow<Backend> window_parent, String title) {
		system_file_dialog = new FileDialog(window_parent, title);
	}
	
	public boolean init_backend_on_path(Backend databackend) {
		//returns read_status
		String path = get_path(FileDialog.LOAD);
		
		if (path != null) {
			return databackend.init_from_file(path);
		} else {
			return false;
		}
	}
	
	public boolean add_component_path(Backend databackend, String label) {
		//returns read_status
		String component = get_path(FileDialog.LOAD);
		
		if (component != null) {
			return databackend.add_component_filepath(component, label);
		} else {
			return false;
		}
	}
	
	public boolean save_on_path(Backend databackend) {
		//returns read_status
		String component = get_path(FileDialog.SAVE);
		
		if (component != null) {
			return databackend.save_to_filepath(component);
		} else {
			return false;
		}
	}
	
	private String get_path(int mode) {
		system_file_dialog.setMode(mode);
		system_file_dialog.setVisible(true);
		
		File[] path = system_file_dialog.getFiles();
		
		if (path.length == 0) {
			return null;
		}
		return path[0].getAbsolutePath();
	}
}