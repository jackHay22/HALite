package system_utils;

import java.awt.FileDialog;
import java.io.File;
import java.util.ArrayList;
import system_utils.io_tools.MultiFileSelector;

public class FileChooser extends ui_framework.StateResult {
	FileDialog file_dialog;
	
	public ArrayList<String> xrf;
	public ArrayList<String> standards;
	public ArrayList<String> means;
	
	public FileChooser(MultiFileSelector main_window) {
		this.file_dialog = new FileDialog(main_window, "Choose data files.");
		this.xrf = new ArrayList<String>();
		this.standards = new ArrayList<String>();
		this.means = new ArrayList<String>();
		
		this.file_dialog.setMultipleMode(false);
	
	}
	
	public void select_multiple(Boolean mult) {
		this.file_dialog.setMultipleMode(mult);
	}
	
	public String import_files(ArrayList<String> target, String label) {
		
		this.file_dialog.setVisible(true);
		File[] path = this.file_dialog.getFiles();
		String new_path = path[0].toString(); //new File(file_dialog.getFile()).getAbsolutePath();
		target.add(new_path);
		target.add(label);
		return new_path;
	}
	
	public ArrayList<String> get_xrf() {
		return this.xrf;
	}
	
	public ArrayList<String> get_standards() {
		return this.standards;
	}
	
	public ArrayList<String> get_means() {
		return this.means;
	}
}
