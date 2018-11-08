package ui_stdlib.dialogwindows;

import java.awt.Font;
import java.awt.GridLayout;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;

import javax.swing.JLabel;

import system_utils.DataStore;
import system_utils.io_tools.FileChooser;
import ui_framework.ScheduledState;
import ui_framework.StateManager;
import ui_framework.StateResult;

@SuppressWarnings("serial")
public class SaveDialog extends SystemDialog implements ScheduledState { //TODO: What type should this be?
	private JLabel save_current_instructions;
	
	public SaveDialog(String title) {
		super(title);
		//TODO: add save target to datastore
		this.setLayout(new GridLayout(4,0));
		
		save_current_instructions = new JLabel("Save");
		save_current_instructions.setFont(new Font("SansSerif", Font.PLAIN, 16));
		save_current_instructions.setHorizontalAlignment(JLabel.CENTER);
	}

	@Override
	public void on_scheduled(StateManager callback, ScheduledState previous, StateResult prev_res) {
		DataStore ds = (DataStore) prev_res;
		FileChooser file_chooser = new FileChooser(this);

		if (try_save(ds)) {
			String save_path = ds.get_path().toString();
    		update_save_label(save_path);
    		
    		try {
    			String datastore_save = ds.toString();
    			FileOutputStream file_write = new FileOutputStream(save_path + ".ds");
    			ObjectOutputStream objectOut = new ObjectOutputStream(file_write);
    			objectOut.writeObject(datastore_save);
    			objectOut.close();
    		} catch (Exception e) {
    			ErrorDialog err = new ErrorDialog("Save Error", "Unable to save project.");
    			err.show_dialog();
    		}
    		
    		close_dialog();
    	}
		else if (file_chooser.save_file(ds) && try_save(ds)) {
			String save_path = ds.get_path().toString();
    		update_save_label(save_path);
    		
    		try {
    			FileOutputStream file_write = new FileOutputStream(save_path + ".ds");
    			ObjectOutputStream objectOut = new ObjectOutputStream(file_write);
    			objectOut.writeObject(ds);
    			objectOut.close();
    		} catch (Exception e) {
    			e.printStackTrace();
    		}
    		
    		close_dialog();
		}
		
	}
	
	private boolean try_save(DataStore ds) {
		if (ds.path_assigned()) {
			return ds.check_valid_target();
		} else {	
			return false;
		}
	}
	
	private void update_save_label(String new_label) {
		save_current_instructions.setText(new_label);
	}

}
