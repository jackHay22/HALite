package system_utils;

public class DoublePair {

	private Double x;
	private Double y;
	
	public DoublePair(Double x, Double y) {
		this.x = x;
		this.y = y;
	}

	public Double get_x() {
		return x;
	}
	public Double get_y() {
		return y;
	}
	
	public String toString() {
		return "(" + x.toString() + ", " + y.toString() + ")";
	}
	
}
