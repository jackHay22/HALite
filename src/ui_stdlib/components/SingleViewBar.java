package ui_stdlib.components;

import java.util.ArrayList;
import javax.swing.BoxLayout;

import ui_framework.DataBackend;
import ui_framework.SystemPanel;

@SuppressWarnings("serial")
public abstract class SingleViewBar<Backend extends DataBackend> extends SystemPanel<Backend>{
	private ArrayList<SingleViewPanel<Backend>> panels;
	public SingleViewBar() {
		super();
		panels = new ArrayList<SingleViewPanel<Backend>>();
		this.setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
	}
	
	protected void add_single_view(SingleViewPanel<Backend> panel) {
		panels.add(panel);
	}
	
	protected void clear_views() {
		for (SingleViewPanel<Backend> p : panels) {
			remove(p);
		}
		panels.clear();
	}
	
	protected void show_views() {
		setVisible(true);
		for (SingleViewPanel<Backend> p : panels) {
			p.on_start();
			add(p);
		}
	}
}
