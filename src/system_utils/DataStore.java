package system_utils;

import java.util.HashMap;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public class DataStore {
	private ui_framework.SystemWindow window_parent;
	private HashMap<TableKey, ArrayList<Float>> xrf_data;
	private HashMap<TableKey, ArrayList<Float>> calibration_standards_data;
	private HashMap<TableKey, ArrayList<Float>> validation_standards_data;

	public DataStore(ui_framework.SystemWindow window_parent) {
		this.window_parent = window_parent;
		this.xrf_data = new HashMap<TableKey, ArrayList<Float>>();
		this.calibration_standards_data = new HashMap<TableKey, ArrayList<Float>>();
		this.validation_standards_data = new HashMap<TableKey, ArrayList<Float>>();
	}
	
	public void import_data(ArrayList<String> xrf, ArrayList<String> calibration, 
							ArrayList<String> validation) throws FileNotFoundException {
		CSVParser parser = new CSVParser();
		
		// Collect all imported data sets
		this.xrf_data = parser.data_from_csv(xrf.get(0), xrf.get(1));
		this.calibration_standards_data = parser.data_from_csv(calibration.get(0), calibration.get(1));
		this.validation_standards_data = parser.data_from_csv(validation.get(0), validation.get(1));
		
	}
	
	public ArrayList<Float> get_xrf_data(int row, int col) {
		return new ArrayList<Float>();
	}
	
	public void add_update_notify(ui_framework.SystemWindow window_parent) {
		
	}
	public void notify_update() {
		//on changes to data
		this.window_parent.refresh();
	}
}
