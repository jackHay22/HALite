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
		
		this.file_dialog.setMultipleMode(false);
	
	}
	
	public void select_multiple(Boolean mult) {
		this.file_dialog.setMultipleMode(mult);
	}
	
	public void import_files() {
		
		// Import in order: xrf, standards, means
		
		this.file_dialog.setVisible(true);
		File[] path_xrf_file = this.file_dialog.getFiles();
		String xrf_path = path_xrf_file[0].toString(); //new File(file_dialog.getFile()).getAbsolutePath();
		this.xrf.add(xrf_path);
		this.xrf.add("XRF_DATA_RUN_229");
		
		this.file_dialog.setVisible(true);
		File[] path_standards_file = this.file_dialog.getFiles();
		String standards_path = path_standards_file[0].toString(); //new File(file_dialog.getFile()).getAbsolutePath();
		this.standards.add(standards_path);
		this.standards.add("standards");
		
		this.file_dialog.setVisible(true);
		File[] path_means_file = this.file_dialog.getFiles();
		String means_path = path_means_file[0].toString(); //new File(file_dialog.getFile()).getAbsolutePath();
		this.means.add(means_path);
		this.means.add("means");
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
