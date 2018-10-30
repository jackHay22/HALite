package system_formulas;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import java.util.ArrayList;

public class Formulas {
	public static void weighted_mean() {
		
	}
	
	private static Double mean_of_array(ArrayList<Double> points) {
		DescriptiveStatistics stats = new DescriptiveStatistics();
		
		for (Double pt : points) {
			stats.addValue(pt);
		}
		Double mean = stats.getMean();
		return mean;
	}
	
	private static Double sum_mean_diff_squared(ArrayList<Double> points) {
		
		Double mean = mean_of_array(points);
		Double squares_sum = 0.0;
		for (Double pt : points) {
			squares_sum += Math.pow((pt - mean), 2);
		}
		
		return squares_sum;
		
	}
	
	private static Double sum_product_diff_squared(ArrayList<Double> x_list, ArrayList<Double> y_list) {

		Double x_mean = mean_of_array(x_list);
		Double y_mean = mean_of_array(y_list);
		
		Double prod_sum = 0.0;
		
		for (int i = 0; i < x_list.size(); i++) {
			Double x = x_list.get(i);
			Double y = y_list.get(i);
			
			prod_sum += (x - x_mean)*(y - y_mean);
			
		}
		
		return prod_sum;
		
	}
	
	public static Double standard_error(ArrayList<Double> x_list, ArrayList<Double> y_list) {

		Double x_sum_squares = sum_mean_diff_squared(x_list);
		Double y_sum_squares = sum_mean_diff_squared(y_list);
		Double prod_sum = sum_product_diff_squared(x_list, y_list);
		
		Double coeff = 1/((double) x_list.size()-2);
		Double pre_root = y_sum_squares - (Math.pow(prod_sum, 2))/x_sum_squares;
				
		return Math.pow(coeff*pre_root, 0.5);
		
	}
	
}
