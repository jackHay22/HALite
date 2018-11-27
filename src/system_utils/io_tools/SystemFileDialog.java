package system_utils.io_tools;

import java.awt.FileDialog;
import java.io.File;
import ui_framework.DataBackend;
import ui_framework.SystemWindow;
import ui_stdlib.SystemFileFilter;
import ui_stdlib.dialogwindows.SystemDialog;

public class SystemFileDialog<Backend extends DataBackend> {
	private FileDialog system_file_dialog;
	private String last_path;
	private String ext_type;
	
	public SystemFileDialog(SystemDialog dialog_parent, String title, String ext_type) {
		system_file_dialog = new FileDialog(dialog_parent, title);
		system_file_dialog.setFilenameFilter(new SystemFileFilter(ext_type));
		this.ext_type = "." + ext_type;
	}
	
	public SystemFileDialog(SystemWindow<Backend> window_parent, String title, String ext_type) {
		system_file_dialog = new FileDialog(window_parent, title);
		system_file_dialog.setFilenameFilter(new SystemFileFilter(ext_type));
		this.ext_type = "." + ext_type;
	}
	
	public boolean init_backend_on_path(Backend databackend) {
		//returns read_status
		last_path = get_path(FileDialog.LOAD);
		
		if (last_path != null) {
			return databackend.init_from_file(last_path);
		} else {
			return false;
		}
	}
	
	public String last_path() {
		return last_path;
	}
	
	public String remove_ext(String in) {
		if (in != null && in.endsWith(ext_type)) {
			return in.replace(ext_type, "");
		} else {
			return in;
		}
	}
	
	public boolean add_component_path(Backend databackend, String label) {
		//returns read_status
		last_path = get_path(FileDialog.LOAD);
		
		if (last_path != null) {
			return databackend.add_component_filepath(last_path, label);
		} else {
			return false;
		}
	}
	
	public boolean save_on_path(Backend databackend) {
		//returns read_status
		last_path = remove_ext(get_path(FileDialog.SAVE));
		
		if (last_path != null) {
			return databackend.save_to_filepath(last_path);
		} else {
			return false;
		}
	}
	
	public boolean export_on_path(Backend databackend, int type) {
		//returns read_status
		last_path = remove_ext(get_path(FileDialog.SAVE));
		
		if (last_path != null) {
			return databackend.on_export(last_path, type);
		} else {
			return false;
		}
	}
	
	private String get_path(int mode) {
		system_file_dialog.setMode(mode);
		system_file_dialog.setFile("Untitled.ds");
		system_file_dialog.setVisible(true);
		
		File[] path = system_file_dialog.getFiles();
		
		if (path.length == 0) {
			return null;
		}
		return path[0].getAbsolutePath();
	}
}
