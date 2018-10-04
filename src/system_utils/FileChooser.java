package system_utils;

import java.awt.FileDialog;
import java.io.File;
import java.util.ArrayList;

import ui_framework.SystemWindow;

public class FileChooser {
	FileDialog file_dialog;
	
	ArrayList<String> xrf;
	ArrayList<String> standards;
	ArrayList<String> means;
	
	public FileChooser(SystemWindow main_window) {
		this.file_dialog = new FileDialog(main_window, "Choose data files.");
		this.xrf = new ArrayList<String>();
		this.standards = new ArrayList<String>();
		this.means = new ArrayList<String>();
		
		this.file_dialog.setMultipleMode(true);
	
	}
	
	public void select_multiple(Boolean mult) {
		this.file_dialog.setMultipleMode(mult);
	}
	
	public void import_files() {
		this.file_dialog.setVisible(true);
		
		File[] data_files = this.file_dialog.getFiles();
		
		for (File file : data_files) {
			String formatted = file.getName().toLowerCase();
			
			if (formatted.contains("xrf")) {
				this.xrf.add(file.toString());
				this.xrf.add("XRF_DATA_RUN_229");
			}
			else if (formatted.contains("standards")) {
				this.standards.add(file.toString());
				this.standards.add("standards");
			}
			else if (formatted.contains("means")) {
				this.means.add(file.toString());
				this.means.add("means");
			}
		}
	}
	
	public void import_single_file() {
		this.file_dialog.setVisible(true);
		
		String data_file = this.file_dialog.getFile();
		String formatted = data_file.toLowerCase();
		
		if (formatted.contains("xrf")) {
			this.xrf.add(data_file.toString());
			this.xrf.add("XRF_DATA_RUN_229");
		}
		else if (formatted.contains("standards")) {
			this.standards.add(data_file.toString());
			this.standards.add("standards");
		}
		else if (formatted.contains("means")) {
			this.means.add(data_file.toString());
			this.means.add("means");
		}
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
