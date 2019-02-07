package system_utils;

public class Pair {
	private Element element;
	private double r2;
	
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
		return element.toString() + ", " + r2;
	}
}
