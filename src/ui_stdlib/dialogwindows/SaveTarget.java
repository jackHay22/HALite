package ui_stdlib.dialogwindows;

import java.io.File;
import ui_framework.StateResult;

public class SaveTarget extends StateResult {
	private File path;
	private boolean loaded_from_save;
	
	public void assign(File path) {
		this.path = path;
	}
	
	public void unassign() {
		this.loaded_from_save = false;
		this.path = null;
	}
	
	public File get_path() {
		loaded_from_save = true;
		return this.path;
	}
	
	public boolean path_assigned() {
		return loaded_from_save;
	}
}
