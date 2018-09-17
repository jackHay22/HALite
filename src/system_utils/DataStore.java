package system_utils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileReader;

import java.util.HashMap; 
import java.util.ArrayList;

public class DataStore {
	private ui_framework.SystemWindow window_parent;
	private HashMap<String, ArrayList<Float>> xrf_data;

	public DataStore(ui_framework.SystemWindow window_parent) {
		this.window_parent = window_parent;
		this.xrf_data = new HashMap<String, ArrayList<Float>>();
	}
	
	public void set_data_from_csv(String path_name) throws FileNotFoundException {
		String current_line = "";
		String delimiter = ",";
		
		BufferedReader reader = new BufferedReader(new FileReader(path_name));
		
		ArrayList<String[]> raw_data = new ArrayList<String[]>();
		
		try {
			while ((current_line = reader.readLine()) != null) {
				// Get data from the current row
				String[] row_data = current_line.split(delimiter);
				
				// Add this array to the table
				raw_data.add(row_data);
				
				System.out.println("Current row data: " + row_data);
			}
		} catch (FileNotFoundException e) {
	        e.printStackTrace();
	    } catch (IOException e) {
	        e.printStackTrace();
	    } finally {
	        if (reader != null) {
	            try {
	                reader.close();
	            } catch (IOException e) {
	                e.printStackTrace();
	            }
	        }
	    }
		
		for (int i = 0; i < raw_data.get(0).length; i++) {
			
		}
		
	}
	
	public ArrayList<Float> get_from_corr_matrix(int row, int col) {
		// Select appropriate data from data
		return this.xrf_data.get(row);
	}
	
	public void add_update_notify(ui_framework.SystemWindow window_parent) {
		
	}
	public void notify_update() {
		//on changes to data
		this.window_parent.refresh();
	}
}
