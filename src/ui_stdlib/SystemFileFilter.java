package ui_stdlib;

import java.io.File;
import java.io.FilenameFilter;

public class SystemFileFilter implements FilenameFilter {
    private String ext;

    public SystemFileFilter(String ext) {
    	this.ext = ext;
    }

	@Override
	public boolean accept(File dir, String name) {
        return dir.isDirectory() && name.toLowerCase().endsWith(ext);
	}
}
