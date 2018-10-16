package ui_stdlib.dialogwindows;

import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import ui_framework.ScheduledState;
import ui_framework.StateManager;
import ui_framework.StateResult;
import ui_stdlib.SystemThemes;

@SuppressWarnings("serial")
public class SaveDialog extends SystemDialog implements ScheduledState {
	private SaveTarget save_target;
	private JLabel save_current_instructions;
	
	public SaveDialog(String title) {
		super(title);
		//TODO: add save target to datastore
		save_target = new SaveTarget();
		this.setLayout(new GridLayout(4,0));
		
		save_current_instructions = new JLabel("Save");
		save_current_instructions.setFont(new Font("SansSerif", Font.PLAIN, 16));
		save_current_instructions.setHorizontalAlignment(JLabel.CENTER);
	}

	@Override
	public void on_scheduled(StateManager callback, ScheduledState previous, StateResult prev_res) {
		JButton save_current_state = new JButton("Save As...");
		JButton save_to_current_path = new JButton("Save");
		
		save_current_state.addActionListener(new ActionListener () {
		    public void actionPerformed(ActionEvent e) {
		    	if (get_new_target() && try_save(prev_res)) {
		    		update_save_label(save_target.get_path().toString());
		    		callback.release_to(previous, prev_res);
		    		close_dialog();
		    	}
		    }
		});
		
		save_to_current_path.addActionListener(new ActionListener () {
		    public void actionPerformed(ActionEvent e) {
		    	if (try_save(prev_res)) {
		    		callback.release_to(previous, prev_res);
		    		close_dialog();
		    	}
		    }
		});
		
		add(save_current_instructions);
		add(save_current_state);
		add(save_to_current_path);
		add(SystemThemes.get_copyright());
		show_dialog();
	}
	
	private boolean try_save(StateResult state) {
		if (save_target.path_assigned()) {
			return save_target.write_to_target();
		} else {	
			return false;
		}
	}
	
	private void update_save_label(String new_label) {
		save_current_instructions.setText(new_label);
	}
	
	private boolean get_new_target() {
		JFileChooser save_chooser = new JFileChooser();
		boolean approved = JFileChooser.APPROVE_OPTION == save_chooser.showSaveDialog(this);
		if (approved) save_target.assign(save_chooser.getSelectedFile());
		return approved;
	}

	@Override
	public void init() {
	}
}
