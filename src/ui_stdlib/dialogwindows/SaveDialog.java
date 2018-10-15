package ui_stdlib.dialogwindows;

import java.awt.FileDialog;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JLabel;
import ui_framework.ScheduledState;
import ui_framework.SetupCoordinator;
import ui_framework.StateResult;

@SuppressWarnings("serial")
public class SaveDialog extends SystemDialog implements ScheduledState {
	private FileDialog save_dialog;
	private ScheduledState release_to_state;
	private SaveTarget save_target;
	private JLabel save_current_instructions;
	private String current_stub = "Save to ";
	
	public SaveDialog(String title, ScheduledState release_to_state) {
		super(title);
		this.release_to_state = release_to_state;
		save_target = new SaveTarget();
		save_dialog = new FileDialog(this, "Save as...");
		this.setLayout(new GridLayout(4,0));
		save_current_instructions = new JLabel("Save to new file...");
	}

	@Override
	public void on_scheduled(SetupCoordinator callback, StateResult prev_state) {
		JButton save_current_state = new JButton("Save As...");
		JButton save_to_current_path = new JButton("Save...");
		
		if (save_target.path_assigned()) {
			save_current_instructions.setText(current_stub + save_target.get_path());
		}
		
		save_current_state.addActionListener(new ActionListener () {
		    public void actionPerformed(ActionEvent e) {
		    	if (try_new_save(prev_state)) {
		    		callback.release_to(release_to_state);
		    		close_dialog();
		    	}
		    }
		});
		save_to_current_path.addActionListener(new ActionListener () {
		    public void actionPerformed(ActionEvent e) {
		    	if (try_save(prev_state, save_target)) {
		    		callback.release_to(release_to_state);
		    		close_dialog();
		    	}
		    }
		});
		
		JLabel save_instructions = new JLabel("Save to new file...");
		add(save_instructions);
		add(save_current_state);
		add(save_current_instructions);
		add(save_to_current_path);
		show_dialog();
	}

	@Override
	public void on_rollback(SetupCoordinator callback) {
	}
	
	private boolean try_save(StateResult state, SaveTarget target) {
		if (target.path_assigned()) {
			//TODO
			//save_target.assign(path);
			return true;
		} else {
			//target unassigned
			return try_new_save(state);
		}
	}
	
	private boolean try_new_save(StateResult state) {
		return false;
	}
}
