package ui_stdlib.components;

import java.awt.Color;
import javax.swing.JLabel;
import system_utils.DataStore;
import ui_framework.Refreshable;

@SuppressWarnings("serial")
public class VerticalPanel extends ui_framework.SystemPanel {
	private JLabel title;
	public VerticalPanel(String text, Color color) {
		super();
		this.setBackground(color);
		
		this.title = new VerticalLabel(text); //text
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
	public void set_datastore(DataStore datastore) {}

	@Override
	public void add_refreshable(Refreshable refreshable_component) {}

	@Override
	public void on_start() {
		title.setVisible(true);
		
		add(title);
		revalidate();
	}

}
