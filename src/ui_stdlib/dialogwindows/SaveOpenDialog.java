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
import ui_stdlib.SystemThemes;

@SuppressWarnings("serial")
public class SaveOpenDialog extends SystemDialog implements ui_framework.ScheduledState {
	DataStore save_loader;
	FileDialog save_dialog;
	
	public SaveOpenDialog(String title, SystemWindow main_window) {
		super(title);
		save_loader = new DataStore(main_window);
		save_dialog = new FileDialog(this, "Choose saved file");
		this.setLayout(new GridLayout(4,0));
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
		
		JLabel title_desc = new JLabel("Select a saved file or open new files");
		title_desc.setHorizontalAlignment(JLabel.CENTER);
		title_desc.setFont(new Font("SansSerif", Font.PLAIN, 16));
		
		JLabel copyright = new JLabel(SystemThemes.COPYRIGHT);
		copyright.setHorizontalAlignment(JLabel.CENTER);
		
		add(title_desc);
		add(open_chooser);
		add(save_chooser);
		add(copyright);
		
		show_dialog();
	}

	@Override
	public void on_rollback(SetupCoordinator callback) {
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
}
