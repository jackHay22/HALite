package ui_stdlib.dialogwindows;

import java.awt.Font;
import java.awt.GridLayout;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;

import javax.swing.JLabel;

import system_utils.DataStore;
import system_utils.FileChooser;
import ui_framework.ScheduledState;
import ui_framework.StateManager;
import ui_framework.StateResult;
import ui_framework.SystemWindow;

@SuppressWarnings("serial")
public class ExportDialog extends SystemDialog implements ScheduledState {
	private JLabel save_current_instructions;
	
	public ExportDialog(String title) {
		super(title);
		this.setLayout(new GridLayout(4,0));
		
		save_current_instructions = new JLabel("Save");
		save_current_instructions.setFont(new Font("SansSerif", Font.PLAIN, 16));
		save_current_instructions.setHorizontalAlignment(JLabel.CENTER);
	}
	
	@Override
	public void on_scheduled(StateManager callback, ScheduledState previous, StateResult prev_res) {
		DataStore ds = (DataStore) prev_res;
		FileChooser file_chooser = new FileChooser(this);

		if (file_chooser.save_file(ds)) {
			String save_path = ds.get_path().toString();
			update_save_label(save_path);
			
			try {
    			
    		} catch (Exception e) {
    			ErrorDialog err = new ErrorDialog("Export Error", "Unable to export project to PDF.");
    			err.show_dialog();
    		}
    		
    		close_dialog();
		}
		

	}
	
	private void update_save_label(String new_label) {
		save_current_instructions.setText(new_label);
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub

	}

}
