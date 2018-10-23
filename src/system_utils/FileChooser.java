package system_utils;

import java.awt.FileDialog;
import java.io.File;
import java.util.ArrayList;
import ui_stdlib.dialogwindows.NewDialog;

public class FileChooser extends ui_framework.StateResult {
	FileDialog file_dialog;
	public String xrf;
	public String standards;
	public String means;
	
	public ArrayList<String> standards_table;
	public ArrayList<String> xrf_table;
	public ArrayList<String> means_table;
	
	public FileChooser(NewDialog main_window) {
		this.file_dialog = new FileDialog(main_window, "Choose data files.");
		
		this.file_dialog.setMultipleMode(false);
		standards_table = new ArrayList<String>();
		xrf_table = new ArrayList<String>();
		means_table = new ArrayList<String>();
	}
	
	public void select_multiple(Boolean mult) {
		this.file_dialog.setMultipleMode(mult);
	}
	
	public String get_xrf() {
		return xrf;
	}
	
	public String get_standards() {
		return standards;
	}
	
	public String get_means() {
		return means;
	}
}
