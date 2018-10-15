package ui_stdlib.dialogwindows;

import java.awt.FileDialog;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JLabel;
import system_utils.DataStore;
import ui_framework.ScheduledState;
import ui_framework.StateManager;
import ui_framework.StateResult;
import ui_framework.SystemWindow;
import ui_stdlib.SystemThemes;

@SuppressWarnings("serial")
public class OpenDialog extends SystemDialog implements ui_framework.ScheduledState {
	DataStore save_loader;
	FileDialog save_dialog;
	
	public OpenDialog(String title, SystemWindow main_window) {
		super(title);
		save_loader = new DataStore(main_window);
		save_dialog = new FileDialog(this, "Choose saved file");
		this.setLayout(new GridLayout(4,0));
	}

	@Override
	public void on_scheduled(StateManager callback, ScheduledState previous, StateResult prev_res) {
		JButton save_chooser = new JButton("Open saved...");
		
		save_chooser.addActionListener(new ActionListener () {
		    public void actionPerformed(ActionEvent e) {
		    	if (load_from_save()) {
		    		callback.release_to(previous, save_loader);
			    	close_dialog();
		    	}
		    }
		});
		
		JLabel title_desc = new JLabel("Select a saved file");
		title_desc.setHorizontalAlignment(JLabel.CENTER);
		title_desc.setFont(new Font("SansSerif", Font.PLAIN, 16));
		
		add(title_desc);
		add(save_chooser);
		add(SystemThemes.get_copyright());
		
		show_dialog();
	}

	private boolean load_from_save() {
		save_dialog.setMultipleMode(false);
		save_dialog.setVisible(true);
		java.io.File[] path = save_dialog.getFiles();
		
		try {
			return path.length > 0 && save_loader.load_from_save(path[0]);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public void init() {

	}
}
