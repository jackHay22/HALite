package system_utils;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class CSVParser {
	
	public CSVParser() {
		
	}
	
	private ArrayList<String[]> get_raw_table_data(String path_name, String table_name) throws FileNotFoundException {
		String current_line = "";
		String delimiter = ",";
		
		BufferedReader reader = new BufferedReader(new FileReader(path_name));
		ArrayList<String[]> raw_data = new ArrayList<String[]>();
		
		// First, read in CSV file row by row
		try {
			Boolean found_data = false;
			// Parse CSV data from beginning of found data to ending marker
			while ((current_line = reader.readLine()) != null) {
				// Get data from the current row
				String[] row_data = current_line.split(delimiter);
				
				// Found beginning of desired table, skip this line (and comments) 
				if (row_data[0].charAt(0) == '#' && row_data[0].substring(1).equals(table_name)) {
					found_data = true;
					continue;
				}
				
				// Found end of desired table
				if (row_data[0].charAt(0) == '#' && row_data[0].substring(1).equals("END")) {
					break;
				}
				
				// If beginning of row contains '#', ignore as comment
				if (row_data[0].charAt(0) == '#') {
					continue;
				}
				
				// Found desired data
				if (found_data) {

					// Add this array to the table
					raw_data.add(row_data);
	
				}
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
	
	public static boolean isNumeric(String strNum) {
	    try {
	        @SuppressWarnings("unused")
			double d = Double.parseDouble(strNum);
	    } catch (NumberFormatException | NullPointerException nfe) {
	        return false;
	    }
	    return true;
	}
	
	public DataTable data_from_csv(String path_name, String table_name) throws FileNotFoundException {
		
		// Empty mapping that will hold all column data for imported CSV data
		DataTable table = new DataTable();
		
		ArrayList<String[]> raw_data = this.get_raw_table_data(path_name, table_name);
				
		String[] column_names = raw_data.get(0);
		
		// Transpose the raw data and add to DataTable
		for (int i = 0; i < raw_data.get(0).length; i ++) {
			
			// Skip empty columns
			if (column_names[i].isEmpty()) {
				continue;
			}
					
			TableKey current_column_name = new TableKey(column_names[i].replaceAll("\\s+",""));
			
			// Check if column contains Doubles
			if (isNumeric(raw_data.get(1)[i])) {
				ArrayList<Double> column_data = new ArrayList<Double>();
				
				for (int j = 1; j < raw_data.size(); j++) {
					
					Double entry;
					
					if (raw_data.get(j)[i].equals("")) {
						entry = null;
					}
					else {
						entry = Double.parseDouble(raw_data.get(j)[i]);
					}
					column_data.add(entry);
				}
				
				table.put_data(current_column_name, column_data);
			}
			else {
				ArrayList<String> column_data = new ArrayList<String>();
				
				for (int j = 1; j < raw_data.size(); j++) {
					String entry = raw_data.get(j)[i];
					column_data.add(entry);
				}
				
				table.put_info(current_column_name, column_data);
			}	
		}
		
		return table;
		
	}
}
