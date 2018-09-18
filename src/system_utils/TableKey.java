package system_utils;

public class TableKey implements Comparable<TableKey> {
	private String string_name;
	private Element name;
	
	public TableKey(String raw_name) {
		this.string_name = raw_name;
		this.name = getElement(raw_name);
	}
	
	private Boolean isElement(String raw_name) {
		for (Element c : Element.values()) {
	        if (c.name().equals(raw_name)) {
	            return true;
	        }
	    }
		return false;
	}
	
	private Element getElement(String raw_name) {
		if (isElement(raw_name)) {
			return Element.valueOf(raw_name);
		}
		else {
			return null;
		}
	}

	public int compareString(String name) {
		return this.string_name.equals(name) ? 1 : 0;
	}
	
	public int compareElement(Element name) {
		return this.name.equals(name) ? 1 : 0;
	}

	@Override
	public int compareTo(TableKey o) {
		if (o.name != null && this.name != null) {
			return compareElement(o.name);
		}
		else {
			return compareString(o.string_name);
		}
	}
}
