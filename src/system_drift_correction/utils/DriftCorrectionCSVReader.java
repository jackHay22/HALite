package system_drift_correction.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import system_utils.Element;
import system_utils.io_tools.CSVParser;
import system_utils.io_tools.ValExpectedException;
import ui_graphlib.Point;

public class DriftCorrectionCSVReader {
	private final String DELIM = ",";
	
	private Integer first_day;		
	private Integer first_month;
	
	public HashMap<Element, ElementCPSInfo> load_dc_file(BufferedReader file_reader, ArrayList<String> stds) throws ValExpectedException, IOException {
		HashMap<Element, ElementCPSInfo> data_output = new HashMap<Element, ElementCPSInfo>();
		HashMap<Integer, Element> col_index_elem = new HashMap<Integer, Element>();
		CSVParser elem_parser = new CSVParser();
		
		String current_line = "";
		String[] current_split_line;
		
		
		//read csv header
		current_split_line = file_reader.readLine().split(DELIM);
		
		int date = -1;
		int time = -1;
		
		//read through first row of csv
		for (int i=0;i<current_split_line.length; i++) {
			//check for lines with cps, and date and time column locations
			if (current_split_line[i].endsWith("CPS")) {
				//parse element from col name
				Element current_element = Element.valueOf(elem_parser.col_name(current_split_line[i]));
				col_index_elem.put(new Integer(i), current_element);
				
				data_output.put(current_element, new ElementCPSInfo(current_element));
			} else if (current_split_line[i].toLowerCase().contains("date")) {
				date = i;
			} else if (current_split_line[i].toLowerCase().contains("time")) {
				time = i;
			}
		}
		
		if (date == -1) {
			throw new ValExpectedException("No date in drift correction csv!");
		}
		
		if (time == -1) {
			throw new ValExpectedException("No time in drift correction csv!");
		}
		
		double current_val;
		double time_fraction = 0.0;
		Element current_elem;
		String current_output;
		ElementCPSInfo current_cpsobj;
		String current_date;
		String current_time;
		
		//read through output lines of csv
		while ((current_line = file_reader.readLine()) != null) {
			//check if current line is an output line
			if (current_line.length() > 0 && current_line.charAt(0) == 'O') {
				current_split_line = current_line.split(DELIM);
				
				//TODO: calculate time
				current_date = current_split_line[date];
				current_time = current_split_line[time];
				current_output = current_split_line[1];
				stds.add(current_output);
				
				time_fraction = time_calc(current_date, current_time);
				
				for (Map.Entry<Integer, Element> entry : col_index_elem.entrySet()) {
					
					//parse double at col location in current row
				    current_val = Double.parseDouble(current_split_line[entry.getKey()]);
				    current_elem = entry.getValue();
				    
				    current_cpsobj = data_output.get(current_elem);
				    
				    current_cpsobj.set_pair(current_output, new Point(time_fraction, current_val));
				}
			}
		}
		
		return data_output;
	}
	
	// This method returns an integer representing the day from the csv 
	// as an integer (0 - day before initially read day) (1 - same day) (2 - day after)
	private Integer day_check(Integer new_day, Integer new_month) {
		if (new_day == first_day) {
			return 1;
		}
		if (new_day < first_day) {
			if (first_month == new_month) {
				return 0;
			} else if (first_month > new_month) {
				if (first_month == 12) {
					return 2;
				} else {
					return 0;
				}
			} else {
				return 2;
			}
		} else {
			if (first_month == new_month) {
				return 2;
			} else {
				return 1;
			}
		}
	}
	
	//tries to parse time and date and calculate
	public double time_calc(String date, String time) throws ValExpectedException {
		String[] date_components = date.trim().split("/");
		String[] time_components = time.trim().split(":");
		
		int month = Integer.parseInt(date_components[0]);
		int day = Integer.parseInt(date_components[1]);
		int year = Integer.parseInt(date_components[2]);
		
		// If we haven't read any days from the file yet we set the value read in as the middle
		// day in a three day range used to normalize the time value down to a fraction of 1
		double day_num = 1;
		if (this.first_day == null) {
			first_day = day;
			first_month = month;
		} else {
			day_num = day_check(day, month);
		}
		
		int hr = Integer.parseInt(time_components[0]);
		int min = Integer.parseInt(time_components[1]);
		int sec = Integer.parseInt(time_components[2]);
		
		// Total time as seconds in the tree day range
		Double total_time = day_num * 24 * 60 * 60 + hr * 60 * 60 + min * 60 + sec;
		
		// Divide by the total seconds in three days
		Double divisor = 3 * 24 * 60 * 60.0;
		
		return total_time/divisor;
	}
}


