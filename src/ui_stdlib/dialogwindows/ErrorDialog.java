package ui_stdlib.dialogwindows;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;

import javax.swing.JLabel;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

import ui_framework.ScheduledState;
import ui_framework.StateManager;
import ui_framework.StateResult;

@SuppressWarnings("serial")
public class ErrorDialog extends SystemDialog implements ui_framework.ScheduledState {
	private String message;
	private JLabel error_message;
	
	public ErrorDialog(String title, String message) {
		super(title);
		this.setLayout(new GridLayout(4,0));
		this.message = message;

		StringBuffer formatted_message = new StringBuffer();
		formatted_message.append("<html>");
		formatted_message.append(String.format("<p>%s</p", this.message));
		formatted_message.append("</html>");
		
		error_message = new JLabel(formatted_message.toString(), JLabel.CENTER);

		Border margin = new EmptyBorder(10,20,10,20);
		error_message.setBorder(margin);
	

	}
	
	@Override
	public void on_scheduled(StateManager callback, ScheduledState previous, StateResult prev_res) {

	}
	
	@Override
	public void show_dialog() {
		
		this.add(error_message);
		
		
		this.setMinimumSize(new Dimension(ui_stdlib.SystemThemes.DIALOG_WINDOW_WIDTH, 
										  250));
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);
		this.setResizable(false);

		setVisible(true);
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub

	}

}
