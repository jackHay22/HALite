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
				
				data_output.put(current_element, new ElementCPSInfo());
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
		double time_fraction;
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
				current_output = current_split_line[0];
				stds.add(current_output);
				
				time_fraction = time_calc();
				
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
	
	public double time_calc() {
		//TODO @BEN
		return 0.0;
	}
}


