package system_utils.io_tools;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;

import system_utils.DataTable;
import system_utils.TableKey;

public class MeansCSVParser extends CSVParser {

	ArrayList<String> std_names;
	ArrayList<String> unknown_names;
	
	
	public MeansCSVParser(ArrayList<String> std_names, ArrayList<String> unknown_names) {
		super();
		this.std_names = std_names;
		this.unknown_names = unknown_names;
	}
	
	public HashMap<String, ArrayList<Integer>> get_sample_indices(ArrayList<String[]> data) {
		
		int name_col = -1;
		
		HashMap<String, ArrayList<Integer>> names = new HashMap<String, ArrayList<Integer>>();
		ArrayList<Integer> std_index = new ArrayList<Integer>();
		ArrayList<Integer> unknown_index = new ArrayList<Integer>();
		
		for (String[] row : data) {
			
			for (int i = 0; i < row.length; i++) {
				String item = row[i];
				if (name_col == -1) {
					if (item.replaceAll("\\s+","").equals("sourcefile")) {
						name_col = i;
						break;
					}
				} else if (name_col == i) {
					item = item.replaceAll("\\s+","");
					Integer s_index = std_names.indexOf(item);
					if (s_index != -1) {
						std_index.add(data.indexOf(row));
					} else {
						Integer u_index = unknown_names.indexOf(item);
						if (u_index != -1) {
							unknown_index.add(data.indexOf(row));
						}
					}
					break;
				}
			}
		}
		
		names.put("standards", std_index);
		names.put("unknowns", unknown_index);
		return names;
		
	}
	
	public HashMap<String, DataTable> tables_from_csv(String table_name, BufferedReader reader) throws FileNotFoundException {

		// Empty mapping that will hold all column data for imported CSV data
		DataTable stds_table = new DataTable("standards");
		DataTable unknowns_table = new DataTable("standards");
		
		ArrayList<String[]> raw_data = this.get_raw_table_data(table_name, reader);
		
		HashMap<String, ArrayList<Integer>> name_locs = get_sample_indices(raw_data);
		ArrayList<Integer> std_row_indices = name_locs.get("standards");
		ArrayList<Integer> unknown_row_indices = name_locs.get("unknowns");
		
		String[] column_names = raw_data.get(0);
		
		// Transpose the raw data and add to DataTable
		for (int i = 0; i < raw_data.get(0).length; i ++) {
			
			// Skip empty columns
			if (column_names[i].isEmpty()) {
				continue;
			}
					
			// THE COLUMN NAMES ARE THE ELEM NAMES!!
			
			TableKey current_column_name = new TableKey(col_name(column_names[i]));
			
			// Check if column contains Doubles
			if (isNumeric(raw_data.get(1)[i])) {
				ArrayList<Double> stds_column_data = new ArrayList<Double>();
				ArrayList<Double> unknowns_column_data = new ArrayList<Double>();
				
				for (int j = 1; j < raw_data.size(); j++) {
					
					Double entry;
					
					if (raw_data.get(j)[i].equals("")) {
						entry = null;
					}
					else {
						entry = Double.parseDouble(raw_data.get(j)[i]);
					}
					if (std_row_indices.indexOf(j) != -1) {
						stds_column_data.add(entry);
					} else if (unknown_row_indices.indexOf(j) != -1) {
						unknowns_column_data.add(entry);
					}
				}
				stds_table.put_data(current_column_name, stds_column_data);
				unknowns_table.put_data(current_column_name, unknowns_column_data);
			} else {
				ArrayList<String> stds_column_data = new ArrayList<String>();
				ArrayList<String> unknowns_column_data = new ArrayList<String>();
				
				for (int j = 1; j < raw_data.size(); j++) {
					String entry = raw_data.get(j)[i];
					if (std_row_indices.indexOf(j) != -1) {
						stds_column_data.add(entry);
					} else if (unknown_row_indices.indexOf(j) != -1) {
						unknowns_column_data.add(entry);
					}
				}
				stds_table.put_info(current_column_name, stds_column_data);
				unknowns_table.put_info(current_column_name, unknowns_column_data);
			}	
		}
		HashMap<String, DataTable> tables = new HashMap<String, DataTable>();
		tables.put("standards", stds_table);
		tables.put("unknowns", unknowns_table);
		return tables;
	}
	
}
