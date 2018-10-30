package system_utils;

import java.io.Serializable;
import java.util.ArrayList;

public class Data implements Serializable {

	private static final long serialVersionUID = 976796036134277108L;
	private ArrayList<Double> data;
	
	public Data(ArrayList<Double> entry) {
		this.data = entry;
	}

	public ArrayList<Double> get_data() {
		return this.data;
	}
	
	public Double get_data(Integer pos) {
		return this.data.get(pos);
	}
}
