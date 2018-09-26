package system_utils;

import java.util.Comparator;

public class PairComparison implements Comparator<Pair> {

	public int compare(Pair a, Pair b) {
		return (int) (a.get_r2() - b.get_r2());
	}
}
