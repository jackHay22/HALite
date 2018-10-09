package ui_stdlib.dialogwindows;

import java.awt.Dimension;
import java.awt.Toolkit;
import javax.swing.JFrame;

@SuppressWarnings("serial")
public abstract class SystemDialog extends javax.swing.JFrame {
	private int window_width = 500;
	private int window_height = 300;
	
	protected SystemDialog(String title) {
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	protected void close_dialog() {
		this.setVisible(false);
		this.dispose();
	}
	
	protected void show_dialog() {
		this.setMinimumSize(new Dimension(window_width, window_height));
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);
		this.setResizable(false);

		setVisible(true);
	}
}
