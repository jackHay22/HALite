package ui_stdlib.dialogwindows;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

import system_main.ViewBuilder;

@SuppressWarnings("serial")
public abstract class SystemDialog extends JFrame {

	protected SystemDialog(String title) {
		super(title);
		super.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
            	ViewBuilder.update_dialog_status(false);
            }
        });
	}
	
	protected void close_dialog() {
		this.setVisible(false);
		this.dispose();
		
		//notify view builder that window closed
		ViewBuilder.update_dialog_status(false);
	}
	
	protected void show_dialog() {
		this.setMinimumSize(new Dimension(ui_stdlib.SystemThemes.DIALOG_WINDOW_WIDTH, 
										  ui_stdlib.SystemThemes.DIALOG_WINDOW_HEIGHT));
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);
		this.setResizable(false);

		setVisible(true);
	}
}
