package system_utils;

import java.util.ArrayList;
import java.util.HashMap;

public class ElementReport {
	
	private Element elem;
	private Integer length;
	private ArrayList<Element> elements_used;
	private HashMap<String, ArrayList<Element>> pairs_to_avoid;
	
	private HashMap<String, HashMap<String, Double>> all_data;
	
	public ElementReport(Element elem, ArrayList<CorrelationInfo> selected_elems, HashMap<String, ArrayList<Element>> avoid, HashMap<String, HashMap<String, Double>> element_data) {
		this.elem = elem;
		this.length = 3; // One col for each of Model, Actual, and the header row 
		this.elements_used = new ArrayList<Element>();
		this.pairs_to_avoid = avoid;
		this.all_data = element_data;
		begin(selected_elems);
	}
	
	private void begin(ArrayList<CorrelationInfo> selected_elems) {
		for (CorrelationInfo elem : selected_elems) {
			elements_used.add(elem.get_secondary());
		}
		if (!elements_used.isEmpty()) {
			length += elements_used.size() + 2;
		}
	}
	
	public String get_header_row() {
		Integer row_length = 0;
		StringBuilder sb = new StringBuilder();
		row_length++;
		sb.append(elem.toString());
		sb.append(',');
		for (Element elem : elements_used) {
			sb.append(elem.toString());
			sb.append(',');
			row_length++;
		}
		if (row_length > 1) {
			sb.append("Std Dev");
			sb.append(',');
			sb.append("WM");
			sb.append(',');
			row_length += 2;
		}
		sb.append("Model Value");
		sb.append(',');
		sb.append("Actual");
		sb.append(",");
		row_length += 2;
		
		while (row_length < this.length) {
			sb.append(',');
			row_length++;
		}
		sb.append(',');
		return sb.toString();
	}
	
	public String get_row(String s) {
		Integer row_length = 0;
		StringBuilder sb = new StringBuilder();
		HashMap<String, Double> data = all_data.get(s);
		sb.append(s);
		row_length++;
		sb.append(',');
		ArrayList<Element> avoid = this.pairs_to_avoid.get(s);
		for (Element elem : elements_used) {
			Double d = data.get(elem.toString());
			if (d == null) {
				sb.append("N/A");
			} else {
				if (avoid.indexOf(elem) == -1) {
					sb.append("*");
					sb.append(d);
					sb.append("*");
				} else {
					sb.append(d);
				}
			}
			sb.append(',');
			row_length++;
		}
		if (row_length > 1) {
			sb.append(data.get("Std Dev"));
			sb.append(',');
			sb.append(data.get("WM"));
			sb.append(',');
			row_length += 2;
		}
		if (data.get("Model_Value") == null) {
			sb.append(data.get("Actual"));
		} else {
			sb.append(data.get("Model_Value")); 
		}
		sb.append(',');
		sb.append(data.get("Actual"));
		sb.append(",");
		row_length += 2;
		
		while (row_length < this.length) {
			sb.append(',');
			row_length++;
		}
		sb.append(',');
		return sb.toString();
	}
	
}
