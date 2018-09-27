package system_utils;

import java.util.Comparator;

public class PairComparison implements Comparator<Pair> {

	public int compare(Pair a, Pair b) {
		if (a.get_r2() < b.get_r2()) return -1;
        if (a.get_r2() > b.get_r2()) return 1;
        
        if (a.get_r2().isNaN() && b.get_r2().isNaN()) {
        	return 0;
        }
        if (!a.get_r2().isNaN() && b.get_r2().isNaN()) {
        	return 1;
        }
        if (a.get_r2().isNaN() && !b.get_r2().isNaN()) {
        	return -1;
        }
        
        return 0;
	}
}
