package ui_framework;

import java.awt.Dimension;
import javax.swing.JPanel;

public class SystemSubFrame extends JPanel implements Resizeable, Refreshable {
	
	public SystemSubFrame() {
		super();
	}
	
	@Override
	public void refresh() {
		// TODO Auto-generated method stub

	}

	@Override
	public int get_width() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int get_height() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void set_dimension(Dimension dim) {
		this.setMinimumSize(dim);
	}

	@Override
	public void set_minimum_dimension(Dimension dim) {
		this.setMinimumSize(dim);
	}


}
