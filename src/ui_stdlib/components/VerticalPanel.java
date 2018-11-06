package ui_stdlib.components;

import java.awt.Color;
import javax.swing.JLabel;
import ui_framework.DataBackend;
import ui_framework.Refreshable;

@SuppressWarnings("serial")
public class VerticalPanel<Backend extends DataBackend> extends ui_framework.SystemPanel<Backend> {
	private JLabel title;
	public VerticalPanel(String text, Color color) {
		super();
		this.setBackground(color);
		
		this.title = new VerticalLabel(text);
		this.setToolTipText(text);
	}
	
	@Override
	public void refresh() {
	}
	
	public void set_text(String text) {
		this.title.setText(text);
		this.setToolTipText(text);
		revalidate();
	}

	@Override
	public void set_datastore(Backend datastore) {}

	@Override
	public void add_refreshable(Refreshable<Backend> refreshable_component) {}

	@Override
	public void on_start() {
		title.setVisible(true);
		revalidate();
		add(title);
		
	}

}
