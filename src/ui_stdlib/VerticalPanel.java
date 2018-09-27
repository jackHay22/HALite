package ui_stdlib;

import java.awt.Color;
import java.awt.geom.AffineTransform;
import javax.swing.JLabel;
import system_utils.DataStore;
import ui_framework.Refreshable;

@SuppressWarnings("serial")
public class VerticalPanel extends ui_framework.SystemPanel {
	private JLabel title;
	public VerticalPanel(String text, Color color) {
		super();
		this.setBackground(color);
		
		AffineTransform transformer = new AffineTransform();
		this.title = new JLabel(text);
		this.add(this.title);
	}
	
	@Override
	public void refresh() {
		// TODO Auto-generated method stub
		
	}
	
	public void set_text(String text) {
		this.title.setText(text);
	}

	@Override
	public void set_datastore(DataStore datastore) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void add_refreshable(Refreshable refreshable_component) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void on_start() {
		// TODO Auto-generated method stub
		
	}

}
