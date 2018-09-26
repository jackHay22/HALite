package system_utils;

import java.util.Comparator;

public class CorrelationComp implements Comparator<CorrelationInfo> {
	public int compare(CorrelationInfo a, CorrelationInfo b) {
		return (int) (a.get_r2() - b.get_r2());
	}
}
