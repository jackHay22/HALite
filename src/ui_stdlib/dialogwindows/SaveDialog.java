package ui_stdlib.dialogwindows;

import java.awt.Font;
import java.awt.GridLayout;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;

import javax.swing.JFileChooser;
import javax.swing.JLabel;

import system_utils.DataStore;
import ui_framework.ScheduledState;
import ui_framework.StateManager;
import ui_framework.StateResult;

@SuppressWarnings("serial")
public class SaveDialog extends SystemDialog implements ScheduledState {
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
		if (ds == null) {
			return;
		}
		else if (try_save(ds)) {
			String save_path = ds.get_path().toString();
    		update_save_label(save_path);
    		
    		try {
    			String datastore_save = ds.toString();
    			FileOutputStream file_write = new FileOutputStream(save_path);
    			ObjectOutputStream objectOut = new ObjectOutputStream(file_write);
    			objectOut.writeObject(datastore_save);
    			objectOut.close();
    		} catch (Exception e) {
    			e.printStackTrace();
    		}
    		
    		callback.release_to(previous, ds);
    		close_dialog();
    	}
		else if (get_new_target(ds) && try_save(ds)) {
			String save_path = ds.get_path().toString();
    		update_save_label(save_path);
    		
    		try {
    			String datastore_save = ds.toString();
    			FileOutputStream file_write = new FileOutputStream(save_path);
    			ObjectOutputStream objectOut = new ObjectOutputStream(file_write);
    			objectOut.writeObject(datastore_save);
    			objectOut.close();
    		} catch (Exception e) {
    			e.printStackTrace();
    		}
    		
    		callback.release_to(previous, ds);
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
	
	private boolean get_new_target(DataStore ds) {
		JFileChooser save_chooser = new JFileChooser();
		boolean approved = JFileChooser.APPROVE_OPTION == save_chooser.showSaveDialog(this);
		if (approved) {
			ds.set_save_path(save_chooser.getSelectedFile().getName());
		}
		return approved;
	}

	@Override
	public void init() {
	}
}
