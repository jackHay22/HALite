package system_utils;

import java.util.HashMap;
import java.util.Map;
import java.io.Serializable;
import java.util.ArrayList;

public class DataTable implements Serializable {
	private HashMap<TableKey, Data> data;
	private HashMap<TableKey, ArrayList<String>> string_data;
	
	public DataTable() {
		this.data = new HashMap<TableKey, Data>();
		this.string_data = new HashMap<TableKey, ArrayList<String>>();
	}
	
	public void put_data(TableKey key, ArrayList<Double> a) {
		Data entry = new Data(a);
		this.data.put(key, entry);
	}
	
	public void put_info(TableKey key, ArrayList<String> a) {
		this.string_data.put(key, a);
	}
	
	public Data get_data(TableKey key) {
		return this.data.get(key);
	}
	
	public Data get_data(Element elem) {
		for (TableKey key : this.data.keySet()) {
			if (key.get_val() != null && key.get_val().equals(elem)) {
				return this.data.get(key);
			}
		}
		
		return null;
	}
	
	public ArrayList<String> get_info(TableKey key) {
		return this.string_data.get(key);
	}

	public boolean contains_data(TableKey key) {
		return this.data.containsKey(key);
	}
	
	public HashMap<TableKey, Data> get_data() {
		return this.data;
	}

	/*public String get_raw_table() {
		String data_output = "";
		
		// Add bracket for start of hashmap
		data_output += "{";
		
		Map<TableKey, ArrayList<String>> string_map = this.string_data;
		Map<TableKey, Data> data_map = this.data;
		// Save all 
		for (Map.Entry<TableKey, ArrayList<String>> entry : string_map.entrySet()) {
			String key = entry.getKey().get_string();
			ArrayList<String> value_list = entry.getValue();
			
			String output_entry = key + "=" + value_list.toString() + ", ";
			data_output += output_entry;
			
		}
		for (Map.Entry<TableKey, Data> entry : data_map.entrySet()) {
			String key = entry.getKey().get_string();
			ArrayList<Double> value_list = entry.getValue().get_data();
			
			String output_entry = key + "=" + value_list.toString() + ", ";
			data_output += output_entry;
			
		}
		
		// Remove last comma
		data_output = data_output.substring(0, data_output.length() - 1);
		
		// Add bracket for end of hashmap
		data_output = data_output + "}";
		
		return data_output;
	}*/
	
}

