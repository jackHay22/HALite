package system_utils.io_tools;

import java.awt.FileDialog;
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
		String path = get_path();
		
		if (path != null) {
			return databackend.init_from_file(path);
		} else {
			return false;
		}
	}
	
	public boolean add_component_path(Backend databackend) {
		//returns read_status
		String component = get_path();
		
		if (component != null) {
			return databackend.add_component_filepath(component);
		} else {
			return false;
		}
	}
	
	private String get_path() {
		system_file_dialog.setMode(FileDialog.LOAD);
		system_file_dialog.setVisible(true);
		
		return system_file_dialog.getFile();
	}
}
