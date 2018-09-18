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
	}
	
	public void import_xrf_data(String path_name, String table_name) throws FileNotFoundException {
		CSVParser parser = new CSVParser();
		this.xrf_data = parser.xrf_data_from_csv(path_name, table_name);
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
