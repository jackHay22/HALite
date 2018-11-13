package ui_stdlib.dialogwindows;

import java.awt.Font;
import java.awt.GridLayout;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import javax.swing.JLabel;
import system_utils.io_tools.SystemFileDialog;
import ui_framework.DataBackend;
import ui_framework.ScheduledState;

@SuppressWarnings("serial")
public class SaveDialog<Backend extends DataBackend> extends SystemDialog implements ScheduledState<Backend> {
	private JLabel save_current_instructions;
	
	public SaveDialog(String title) {
		super(title);
		//TODO: add save target to datastore
		this.setLayout(new GridLayout(4,0));
		
		save_current_instructions = new JLabel("Save");
		save_current_instructions.setFont(new Font("SansSerif", Font.PLAIN, 16));
		save_current_instructions.setHorizontalAlignment(JLabel.CENTER);
	}

	
	private boolean try_save(Backend backend) {
		if (backend.path_assigned()) {
			return backend.check_valid_target();
		} else {	
			return false;
		}
	}
	
	private void update_save_label(String new_label) {
		save_current_instructions.setText(new_label);
	}

	@Override
	public void on_scheduled(Backend backend) {
		SystemFileDialog<Backend> save_file_chooser = new SystemFileDialog<Backend>(this, "Save...", "ds");

		if (try_save(backend)) {
			String save_path = backend.get_path().toString();
    		update_save_label(save_path);
    		
    		try {
    			String datastore_save = backend.toString();
    			FileOutputStream file_write = new FileOutputStream(save_path + ".ds");
    			ObjectOutputStream objectOut = new ObjectOutputStream(file_write);
    			objectOut.writeObject(datastore_save);
    			objectOut.close();
    		} catch (Exception e) {
    			ErrorDialog<Backend> err = new ErrorDialog<Backend>("Save Error", "Unable to save project.");
    			err.show_dialog();
    		}
    		
    		close_dialog();
    	}
		else if (save_file_chooser.save_on_path(backend) && try_save(backend)) {
			String save_path = backend.get_path().toString();
    		update_save_label(save_path);
    		
    		try {
    			FileOutputStream file_write = new FileOutputStream(save_path + ".ds");
    			ObjectOutputStream objectOut = new ObjectOutputStream(file_write);
    			objectOut.writeObject(backend);
    			objectOut.close();
    		} catch (Exception e) {
    			e.printStackTrace();
    		}
    		
    		close_dialog();
		}
		
	}

}
