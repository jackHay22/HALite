package ui_framework;

import java.awt.Dimension;
import java.util.ArrayList;

@SuppressWarnings("serial")
public class WindowSubPanel extends SystemPanel implements Resizeable {
	private ArrayList<SystemPanel> added_panels;
	public WindowSubPanel() {
		super();
		added_panels = new ArrayList<SystemPanel>();
	}
	
	@Override
	public void refresh() {
		for (int i=0; i < this.added_panels.size(); i++) {
			this.added_panels.get(i).refresh();
		}
	}
	
	public void add_panel(SystemPanel component) {
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
