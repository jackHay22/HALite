package system_utils;

import java.util.HashMap;
import java.util.ArrayList;

public class DataTable {
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
	
	public ArrayList<String> get_info(TableKey key) {
		return this.string_data.get(key);
	}
}