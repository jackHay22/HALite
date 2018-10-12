package ui_stdlib.dialogwindows;

import java.awt.Dimension;
import java.awt.Toolkit;
import javax.swing.JFrame;

@SuppressWarnings("serial")
public abstract class SystemDialog extends JFrame {

	protected SystemDialog(String title) {
		super(title);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	protected void close_dialog() {
		this.setVisible(false);
		this.dispose();
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
