package system_utils;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class CSVParser {
	
	
	public CSVParser() {
		
	}
	
	private ArrayList<String[]> get_raw_table_data(String path_name) throws FileNotFoundException {
		String current_line = "";
		String delimiter = ",";

		BufferedReader reader = new BufferedReader(new FileReader(path_name));
		ArrayList<String[]> raw_data = new ArrayList<String[]>();
		
		// First, read in CSV file row by row
		try {
			// if there are two line-breaks immediately after the other, exit because the table is finished
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

		return raw_data;
	}
	
	public HashMap<String, ArrayList<Float>> xrf_data_from_csv(String path_name) throws FileNotFoundException {
		
		// Empty mapping that will hold all column data for imported CSV data
		HashMap<String, ArrayList<Float>> xrf_table = new HashMap<String, ArrayList<Float>>();
		
		ArrayList<String[]> raw_data = this.get_raw_table_data(path_name);
				
		String[] column_names = raw_data.get(0);
		
		// Transpose the raw data to get ArrayLists consisting of elements in the columns
		for (int i = 0; i < raw_data.get(0).length; i++) {
			
			String current_column = column_names[i];
			ArrayList<Float> column_data = new ArrayList<Float>();
			
			for (int j = 0; j < raw_data.size(); j++) {
				Float entry = Float.parseFloat(raw_data.get(j)[i]);
				column_data.add(entry);
			}
			
			xrf_table.put(current_column, column_data);
		}
		
		return xrf_table;
		
	}
}
