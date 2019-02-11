package system_utils;

import ui_stdlib.SystemThemes;

public class Pair {
	private Element element;
	private double r2;
	private String truncate_mask = "#.000";
	private String sep = "  ";
	
	public Pair(Element elem, Double r2) {
		this.element = elem;
		this.r2 = r2;
	}
	
	public Element get_elem() {
		return this.element;
	}
	
	public Double get_r2() {
		return this.r2;
	}
	
	public void set_r2(Double d) {
		this.r2 = d;
	}
	
	public String toString() {
		return 
				String.format("%-4s", element.toString()) +
				sep + 
				String.format("%4s", SystemThemes.get_display_number(r2, truncate_mask));
				
	}
	
	public void set_truncate(String truncate_mask) {
		this.truncate_mask = truncate_mask;
	}
	
	public void set_sep(String sep) {
		this.sep = sep;
	}
}
