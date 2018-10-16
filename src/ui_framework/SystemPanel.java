package ui_framework;

import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JPanel;


@SuppressWarnings("serial")
public abstract class SystemPanel extends JPanel implements Refreshable {
	public SystemPanel() {
		super();
	}

	public void set_minimum_dimension(int width, int height) {
		super.setMinimumSize(new Dimension(width, height));
	}
}
