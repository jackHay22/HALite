package system_utils;

import java.io.Serializable;

public class TableKey implements Comparable<TableKey>, Serializable {
	private static final long serialVersionUID = 7;
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
	public boolean equals(Object o) {
		if (this == o) return true;
		if (this.getClass() != o.getClass()) return false;
		TableKey key = (TableKey) o;
		if (key.name != null && this.name != null) {
			return (key.name.equals(this.name));
		}
		else {
			return (key.string_name.equals(this.string_name));
		}
	}
	
	@Override
	public int hashCode() {
		if (this.name == null) {
			int initial = 0;
			for (char s : this.string_name.toCharArray()) {
				initial += (int)s;
			}
			int result = (int) (initial ^ (initial >>> 32));
			return result;
		}
        return name.hashCode();
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
	
	public Element get_val() {
		return this.name;
	}
	
	public String get_string() {
		return this.string_name;
	}
}
