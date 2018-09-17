package system_utils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DataStore {
	private ui_framework.SystemWindow window_parent;
	private ArrayList<ArrayList<Integer>> correlation_matrix;
	private String database_url;
	
	public DataStore(String DatabaseName) {
		this.correlation_matrix = new ArrayList<ArrayList<Integer>>();
		this.database_url = initialize_database(DatabaseName);
	}
	
	private String initialize_database(String FileName) {
		String url = "jdbc:sqlite:C:/sqlite/db/" + FileName;
		
		try (Connection conn = DriverManager.getConnection(url)) {
            if (conn != null) {
                DatabaseMetaData meta = conn.getMetaData();
            }
 
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
		
		return url;
	}
	
	public void set_data_from_csv(String path_name) throws FileNotFoundException {
		String current_line = "";
		String delimiter = ",";
		
		BufferedReader reader = new BufferedReader(new FileReader(path_name));
		
		ArrayList<String[]> output = new ArrayList<String[]>();
		
		try {
			while ((current_line = reader.readLine()) != null) {
				String[] row_data = current_line.split(delimiter);
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
	}
	
	public ArrayList<Integer> get_from_corr_matrix(int row, int col) {
		// Select appropriate data from data
		return this.correlation_matrix.get(row);
	}
	
	public void add_update_notify(ui_framework.SystemWindow window_parent) {
		this.window_parent = window_parent;
	}
	public void notify_update() {
		//on changes to data
		this.window_parent.refresh();
	}
}
