package system_utils;

import java.awt.FileDialog;
import java.io.File;
import java.util.ArrayList;
import ui_stdlib.dialogwindows.NewDialog;

public class FileChooser extends ui_framework.StateResult {
	FileDialog file_dialog;
	
	public String[] xrf;
	public String[] standards;
	public String[] means;
	
	public FileChooser(NewDialog main_window) {
		this.file_dialog = new FileDialog(main_window, "Choose data files.");
		xrf = new String[2];
		standards = new String[2];
		means = new String[2];
		
		this.file_dialog.setMultipleMode(false);
	
	}
	
	public void select_multiple(Boolean mult) {
		this.file_dialog.setMultipleMode(mult);
	}
	
	public String import_files(String[] target, String label) {
		
		this.file_dialog.setVisible(true);
		File[] path = this.file_dialog.getFiles();
		
		String new_path;
		try {
			new_path = path[0].toString();
		}
		catch (ArrayIndexOutOfBoundsException e) {
			new_path = "";
		}
		
		target[0] = new_path;
		target[1] = label;
		return new_path;
	}
	
	public String[] get_xrf() {
		return xrf;
	}
	
	public String[] get_standards() {
		return standards;
	}
	
	public String[] get_means() {
		return means;
	}
}
