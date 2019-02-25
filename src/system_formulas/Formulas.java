package system_formulas;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import java.util.ArrayList;
import java.util.List;

public class Formulas {
	public static void weighted_mean() {
		
	}
	
	// Normalizes all points in a list by dividing by the provided value
	public static ArrayList<Double> normalize(ArrayList<Double> vals, Double norm) {
		for (int i = 0; i < vals.size(); i++) {
			Double d = vals.get(i)/norm;
			vals.add(i, d);
			vals.remove(i+1);
		}
		return vals;
	}
	
	// Takes the mean of an array
	public static Double mean_of_array(ArrayList<Double> points) {
		DescriptiveStatistics stats = new DescriptiveStatistics();
		
		for (Double pt : points) {
			stats.addValue(pt);
		}
		Double mean = stats.getMean();
		return mean;
	}
	
	public static Double meadian_of_array(ArrayList<Double> points) {
		ArrayList<Double> sorted_array = merge_sort(points);
		int middle = sorted_array.size()/2;
		if (sorted_array.size() % 2 == 0) {
			return (sorted_array.get(middle) + sorted_array.get(middle - 1))/2;
		} else {
			return sorted_array.get(middle);
		}
	}
	
	private static ArrayList<Double> merge_sort(List<Double> points){
		if (points.size() == 1 || points.size() == 0) {
			return new ArrayList<Double>(points);
		}
		
		List<Double> first_half = points.subList(0, points.size()/2);
		List<Double> second_half = points.subList(points.size()/2, points.size());
		
		first_half = Formulas.merge_sort(first_half);
		second_half = Formulas.merge_sort(second_half);
		
		int pos_in_first = 0;
		int pos_in_second = 0;
		
		ArrayList<Double> merged = new ArrayList<Double>();
		for (int i = 0; i < points.size(); i++) {
			if (pos_in_first >= first_half.size()) {
				merged.add(second_half.get(pos_in_second));
				pos_in_second++;
			} else if (pos_in_second >= second_half.size()) {
				merged.add(first_half.get(pos_in_first));
				pos_in_first++;
			}
			else if (first_half.get(pos_in_first) < second_half.get(pos_in_second)) {
				merged.add(first_half.get(pos_in_first));
				pos_in_first++;
			} else {
				merged.add(second_half.get(pos_in_second));
				pos_in_second++;
			}
		}
		
		return merged;
	}
	
	// Used in several statistical calculations
	public static Double sum_mean_diff_squared(ArrayList<Double> points) {
		
		Double mean = mean_of_array(points);
		Double squares_sum = 0.0;
		for (Double pt : points) {
			squares_sum += Math.pow((pt - mean), 2);
		}
		
		return squares_sum;
		
	}
	
	// Used in several statistical calculations
	public static Double sum_mean_diff_squared(ArrayList<Double> points, Double mean) {
		
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
	
	// Brings the above equations together
	public static Double standard_error(ArrayList<Double> x_list, ArrayList<Double> y_list) {

		Double x_sum_squares = sum_mean_diff_squared(x_list);
		Double y_sum_squares = sum_mean_diff_squared(y_list);
		Double prod_sum = sum_product_diff_squared(x_list, y_list);
		
		Double coeff = 1/((double) x_list.size()-2);
		Double pre_root = y_sum_squares - (Math.pow(prod_sum, 2))/x_sum_squares;
				
		return Math.pow(coeff*pre_root, 0.5);
		
	}
	
}
