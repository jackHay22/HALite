package system_utils.io_tools;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import system_utils.DataTable;
import system_utils.Element;
import system_utils.TableKey;

public class CSVParser {
	
	public CSVParser() {
		
	}
	
	protected ArrayList<String[]> get_raw_table_data(String table_name, BufferedReader reader) throws FileNotFoundException {
		String current_line = "";
		String delimiter = ",";
		
		ArrayList<String[]> raw_data = new ArrayList<String[]>();
		
		//BufferedReader reader = new BufferedReader(new FileReader(path_name));
		
		// First, read in CSV file row by row
		try {
			Boolean found_data = true;
			// Parse CSV data from beginning of found data to ending marker
			while ((current_line = reader.readLine()) != null) {
				// Get data from the current row
				String[] row_data = current_line.split(delimiter);
				//
				// Fill out "row_data" if there are empty spots
				// Or handle empty spots elsewhere
				//
				//
				
				// Found beginning of desired table, skip this line (and comments)
				if (row_data[0].length() > 0) {
					if (row_data[0].charAt(0) == '#' && row_data[0].substring(1).equals(table_name)) {
						found_data = true;
						continue;
					}
					
					// Found end of desired table
					if (row_data[0].charAt(0) == '#' && row_data[0].substring(1).toUpperCase().equals("END")) {
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
			}
		} catch (FileNotFoundException e) {
	        e.printStackTrace();
	    } catch (IOException e) {
	        e.printStackTrace();
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
	
	// This method will help parse the weird column names
	public String col_name(String col_title) {
		String chosen = col_title;
		ArrayList<String> headers = new ArrayList<String>();
		headers.add("Name");
		headers.add("Time/Date");
		headers.add("Calibration values");
		headers.add("TotalBeamCPS");
		if (!(headers.contains(col_title) || headers.contains(col_title.replaceAll("[^A-Za-z]","").replaceAll("\\s+","")))) {
		
			col_title = col_title.replaceAll("[^A-Za-z]","").replaceAll("\\s+","");
			chosen = col_title;
			
			for (Element elem : Element.values()) {
				// Some parsing work in here
				if (col_title.indexOf(elem.toString()) == 0) {
					if (chosen == col_title || chosen.length() < elem.toString().length()) {
						chosen = elem.toString();
					}
				}
			}
		}
		return chosen.replaceAll("\\s+","");
	}
	
	public ArrayList<String> get_table_names(BufferedReader reader) throws FileNotFoundException {
		ArrayList<String> table_names = new ArrayList<String>();
		String current_line = "";
		String delimiter = ",";
		
		try {
			// Parse CSV data from beginning of found data to ending marker
			while ((current_line = reader.readLine()) != null) {
				// Get data from the current row
				String[] row_data = current_line.split(delimiter);
				
				if (row_data.length == 0 || row_data[0].length() == 0) {
					continue;
				}
				
				// Found beginning of desired table, skip this line (and comments)
				if (row_data[0].charAt(0) == '#' && !row_data[0].substring(1).isEmpty() && !row_data[0].substring(1).equals("END")) {
					table_names.add(row_data[0].substring(1));
					continue;
				}
				
				// Found end of desired table
				if (row_data[0].charAt(0) == '#' && row_data[0].substring(1).equals("END")) {
					continue;
				}
				
				// If beginning of row contains '#', ignore as comment
				if (row_data[0].charAt(0) == '#') {
					continue;
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
		
		return table_names;
	}
	
	private DataTable data_from_csv(BufferedReader reader, String table_name) throws FileNotFoundException {
		
		// Empty mapping that will hold all column data for imported CSV data
		DataTable table = new DataTable(table_name);
		
		ArrayList<String[]> raw_data = this.get_raw_table_data(table_name, reader);
		
		String[] column_names = raw_data.get(0);
		
		// If it is the first column we know these are the sample names
		
		ArrayList<String> name_data = new ArrayList<String>();
		
		for (int j = 1; j < raw_data.size(); j++) {
			String entry = raw_data.get(j)[0];
			name_data.add(entry);
		}
		
		table.put_info(new TableKey("Sample Names"), name_data);
		
		// Transpose the raw data and add to DataTable
		for (int i = 1; i < raw_data.get(0).length; i++) {
			
			// Skip empty columns
			if (column_names[i].isEmpty()) {
				continue;
			}
					
			TableKey current_column_name = new TableKey(col_name(column_names[i]));
			
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
		}
		
		return table;
	}
	
	public ArrayList<DataTable> parse_data(BufferedReader reader) throws FileNotFoundException {
		
		//BufferedReader reader = new BufferedReader(new FileReader(path));
		
		String current_line = "";
		String delimiter = ",";
		
		ArrayList<DataTable> tables = new ArrayList<DataTable>();
		
		// Parse CSV data from beginning of found data to ending marker
		try {
			while ((current_line = reader.readLine()) != null) {
				String[] row_data = current_line.split(delimiter);
				
				if (row_data.length == 0 || row_data[0].length() == 0) {
					continue;
				}
				
				// Found beginning of a table, skip this line (and comments)
				if (row_data[0].charAt(0) == '#' && !row_data[0].substring(1).isEmpty() && !row_data[0].substring(1).equals("END")) {
					
					// Create new table 
					DataTable table = data_from_csv(reader, row_data[0].substring(1));
					tables.add(table);
					continue;
				}
				
				// Found end of desired table
				if (row_data[0].charAt(0) == '#' && row_data[0].substring(1).equals("END")) {
					continue;
				}
				
				// If beginning of row contains '#', ignore as comment
				if (row_data[0].charAt(0) == '#') {
					continue;
				}
				
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return tables;
	}
}
