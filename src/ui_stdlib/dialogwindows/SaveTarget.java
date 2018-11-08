package ui_stdlib.dialogwindows;

import java.io.File;

public class SaveTarget {
	private File path;
	private boolean target_assigned = false;
	
	public void assign(File path) {
		this.path = path;
	}
	
	public File get_path() {
		target_assigned = true;
		return this.path;
	}
	
	public boolean write_to_target() {
		if (target_assigned) {
			
			
			
			return true;
		} else {
			return false;
		}
	}
	
	public boolean path_assigned() {
		return target_assigned;
	}
}
