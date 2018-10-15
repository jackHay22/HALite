package ui_stdlib.dialogwindows;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import ui_framework.ScheduledState;
import ui_framework.SetupCoordinator;
import ui_framework.StateResult;

@SuppressWarnings("serial")
public class SaveDialog extends SystemDialog implements ScheduledState {
	private ScheduledState previous_state;
	private SaveTarget save_target;
	private JLabel save_current_instructions;
	private String current_stub = "Save to ";
	
	public SaveDialog(String title, ScheduledState previous_state) {
		super(title);
		this.previous_state = previous_state;
		save_target = new SaveTarget();
		this.setLayout(new GridLayout(4,0));
		save_current_instructions = new JLabel("Save to new file...");
		save_current_instructions.setHorizontalAlignment(JLabel.CENTER);
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
		    	if (get_new_target() && try_save(prev_state)) {
		    		callback.release_to(previous_state);
		    		close_dialog();
		    	}
		    }
		});
		save_to_current_path.addActionListener(new ActionListener () {
		    public void actionPerformed(ActionEvent e) {
		    	if (try_save(prev_state)) {
		    		callback.release_to(previous_state);
		    		close_dialog();
		    	}
		    }
		});
		
		JLabel save_instructions = new JLabel("Save to new file...");
		save_instructions.setHorizontalAlignment(JLabel.CENTER);
		
		add(save_instructions);
		add(save_current_state);
		add(save_current_instructions);
		add(save_to_current_path);
		show_dialog();
	}

	@Override
	public void on_rollback(SetupCoordinator callback) {
	}
	
	private boolean try_save(StateResult state) {
		if (save_target.path_assigned()) {
			return save_target.write_to_target();
		} else {	
			return false;
		}
	}
	
	private boolean get_new_target() {
		JFileChooser save_chooser = new JFileChooser();
		boolean approved = JFileChooser.APPROVE_OPTION == save_chooser.showSaveDialog(this);
		if (approved) save_target.assign(save_chooser.getSelectedFile());
		return approved;
	}
}
