package ui_framework;

import java.awt.Dimension;
import javax.swing.JPanel;
import java.util.ArrayList;

@SuppressWarnings("serial")
public class SystemSubPanel extends JPanel implements Resizeable, Refreshable {
	private ArrayList<JPanel> added_panels;
	public SystemSubPanel() {
		super();
		added_panels = new ArrayList<JPanel>();
	}
	
	@Override
	public void refresh() {
		// TODO Auto-generated method stub

	}
	
	public void add_panel(JPanel component) {
		this.added_panels.add(component);
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
