package ui_stdlib.dialogwindows;

import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JLabel;
import ui_framework.SetupCoordinator;
import ui_framework.StateResult;

@SuppressWarnings("serial")
public class SaveOpenDialog extends SystemDialog implements ui_framework.ScheduledState {

	public SaveOpenDialog(String title) {
		super(title);
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
		    	System.out.println("TODO: load saved file...");
		    	//release (to main) with filename 
		    	callback.release(null);
		    	close_dialog();
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

}
