package ui_stdlib.dialogwindows;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import ui_framework.DataBackend;
import ui_stdlib.SystemThemes;

@SuppressWarnings("serial")
public class ErrorDialog<Backend extends DataBackend> extends SystemDialog implements ui_framework.ScheduledState<Backend> {
	private String message;
	private JLabel error_message;
	private JButton dismiss;
	private GridBagConstraints constraints;
	
	public ErrorDialog(String title, String message) {
		super(title);
		this.setLayout(new GridBagLayout());
		this.message = message;
		constraints = SystemThemes.get_grid_constraints();

		StringBuffer formatted_message = new StringBuffer();
		formatted_message.append("<html>");
		formatted_message.append(String.format("<p>%s</p", this.message));
		formatted_message.append("</html>");
		
		error_message = new JLabel(formatted_message.toString(), JLabel.CENTER);
		
		dismiss = new JButton("Dismiss");

		Border margin = new EmptyBorder(10,20,10,20);
		error_message.setBorder(margin);
	}
	
	@Override
	public void show_dialog() {
		
		// Format positioning of text on the ErrorDialog
		constraints.gridx = 0;
		constraints.gridy = 1;
		constraints.fill = GridBagConstraints.BOTH;
		constraints.weighty = 0.7;
		
		this.add(error_message, constraints);
		
		// Format positioning of 'Dismiss' button
		constraints.gridwidth = 2;
		constraints.gridy = 3;
		constraints.gridx = 0;
		constraints.weighty = 0.3;
		
		this.add(dismiss, constraints);
		
		dismiss.addActionListener(new ActionListener () {
		    public void actionPerformed(ActionEvent e) {
		    	
		    	dismiss.setText("Dismiss");
		    	
		    	close_dialog();
		    }
		});
		
		this.setMinimumSize(new Dimension(ui_stdlib.SystemThemes.DIALOG_WINDOW_WIDTH, 
										  250));
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);
		this.setResizable(false);

		setVisible(true);
	}
	
	@Override
	public void on_scheduled(Backend backend) {
		show_dialog();
	}

}
