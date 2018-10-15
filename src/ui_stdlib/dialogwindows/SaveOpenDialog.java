package ui_stdlib.dialogwindows;

import java.awt.FileDialog;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JLabel;
import system_utils.DataStore;
import ui_framework.SetupCoordinator;
import ui_framework.StateResult;
import ui_framework.SystemWindow;

@SuppressWarnings("serial")
public class SaveOpenDialog extends SystemDialog implements ui_framework.ScheduledState {
	DataStore save_loader;
	
	public SaveOpenDialog(String title, SystemWindow main_window) {
		super(title);
		save_loader = new DataStore(main_window);
		this.setLayout(new GridLayout(4,0));
		JLabel title_desc = new JLabel("Select a saved file or open new files");
		title_desc.setHorizontalAlignment(JLabel.CENTER);
		title_desc.setFont(new Font("SansSerif", Font.PLAIN, 16));
		this.add(title_desc);
	}

	@Override
	public void on_scheduled(SetupCoordinator callback, StateResult prev_state) {
		JButton open_chooser = new JButton("Open new files...");
		JButton save_chooser = new JButton("Open saved...");

		open_chooser.addActionListener(new ActionListener () {
		    public void actionPerformed(ActionEvent e) {
		    	callback.release(null);
		    	close_dialog();
		    }
		});
		
		save_chooser.addActionListener(new ActionListener () {
		    public void actionPerformed(ActionEvent e) {
		    	if (load_from_save()) {
		    		callback.release(save_loader);
			    	close_dialog();
		    	}
		    }
		});
		
		JLabel copyright = new JLabel("© 2018 Ben Parfitt, Jack Hay, and Oliver Keh");
		copyright.setHorizontalAlignment(JLabel.CENTER);
		
		add(open_chooser);
		add(save_chooser);
		add(copyright);
		
		show_dialog();
	}

	@Override
	public void on_rollback(SetupCoordinator callback) {
	}
	
	private boolean load_from_save() {
		FileDialog dialog = new FileDialog(this, "Choose data files.");
		dialog.setMultipleMode(false);
		dialog.setVisible(true);
		java.io.File[] path = dialog.getFiles();
		
		try {
			return path.length > 0 && save_loader.load_from_save(path[0]);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
}
