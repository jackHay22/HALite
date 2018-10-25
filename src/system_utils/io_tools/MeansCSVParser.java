package system_utils.io_tools;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import system_utils.DataTable;
import system_utils.TableKey;

public class MeansCSVParser extends CSVParser {

	
	
	
	public MeansCSVParser() {
		super();
	}
	
	public DataTable data_from_csv(String table_name, BufferedReader reader) throws FileNotFoundException {
		
		// Empty mapping that will hold all column data for imported CSV data
		DataTable table = new DataTable();
		
		ArrayList<String[]> raw_data = this.get_raw_table_data(table_name, reader);
				
		String[] column_names = raw_data.get(0);
		
		// Transpose the raw data and add to DataTable
		for (int i = 0; i < raw_data.get(0).length; i ++) {
			
			// Skip empty columns
			if (column_names[i].isEmpty()) {
				continue;
			}
					
			// THE COLUMN NAMES ARE THE ELEM NAMES!!
			//col_name(String col_title)
			//System.out.println("Pair: ");
			System.out.println("." + column_names[i] + ".");
			System.out.println("." + col_name(column_names[i]) + ".");
			
			TableKey current_column_name = new TableKey(col_name(column_names[i]));
			//TableKey current_column_name = new TableKey(column_names[i].replaceAll("\\s+",""));
			
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
