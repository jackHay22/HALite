package ui_stdlib.dialogwindows;

import java.awt.Font;
import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;

import javax.swing.JLabel;

import system_utils.DataStore;
import system_utils.io_tools.SystemFileDialog;
import ui_framework.ScheduledState;
import ui_framework.SystemWindow;

public class SaveDialog implements ScheduledState<DataStore> {
	private JLabel save_current_instructions;
	private SystemWindow<DataStore> main_window;
	
	public SaveDialog(String title, SystemWindow<DataStore> main_window) {
		
		this.main_window = main_window;
		save_current_instructions = new JLabel("Save");
		save_current_instructions.setFont(new Font("SansSerif", Font.PLAIN, 16));
		save_current_instructions.setHorizontalAlignment(JLabel.CENTER);
	}

	private boolean try_save(DataStore backend) {
		if (backend.path_assigned()) {
			return backend.check_valid_target();
		} else {	
			return false;
		}
	}
	
	private void update_save_label(String new_label) {
		save_current_instructions.setText(new_label);
	}
	
	public void save(DataStore backend) {
		if (try_save(backend)) {
			String save_path = backend.get_path().toString();
    		update_save_label(save_path);
    		
    		try {
    			String datastore_save = ((DataStore) backend).toString();
    			FileOutputStream file_write = new FileOutputStream(save_path + ".ds");
    			ObjectOutputStream objectOut = new ObjectOutputStream(file_write);
    			objectOut.writeObject(datastore_save);
    			objectOut.close();
    		} catch (Exception e) {
    			ErrorDialog<DataStore> err = new ErrorDialog<DataStore>("Save Error", "Unable to save project.");
    			err.show_dialog();
    		}
    	}
	}
	
	public void save_as(DataStore backend) {
		SystemFileDialog<DataStore> save_file_chooser = new SystemFileDialog<DataStore>(this.main_window, "Save...", "ds");

		if (save_file_chooser.save_on_path(backend) && try_save(backend)) {
			String save_path = backend.get_path().toString();
    		update_save_label(save_path);
    		
    		try {

				File clear_file = new File(save_path + ".ds");
				
    			if(clear_file.exists() && !clear_file.isDirectory()) {
        			PrintWriter writer = new PrintWriter(clear_file);
        			writer.print("");
        			writer.close();
    			}
    			
    			
    			FileOutputStream file_write = new FileOutputStream(save_path + ".ds");
    			ObjectOutputStream objectOut = new ObjectOutputStream(file_write);
    			objectOut.writeObject(backend);
    			objectOut.close();
    		} catch (Exception e) {
    			e.printStackTrace();
    		}
		}
	}

	@Override
	public void on_scheduled(DataStore backend) {
	}

}
