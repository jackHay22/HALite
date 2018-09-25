package ui_stdlib;

import java.awt.Color;
import java.awt.geom.AffineTransform;
import javax.swing.JLabel;
import system_utils.DataStore;
import ui_framework.Refreshable;

@SuppressWarnings("serial")
public class VerticalPanel extends ui_framework.SystemPanel {
	public VerticalPanel(String text, Color color) {
		super();
		this.setBackground(color);
		
		AffineTransform transformer = new AffineTransform();
		JLabel test = new JLabel("test");
		
		this.add(new JLabel(text));
	}
	
	@Override
	public void refresh() {
		// TODO Auto-generated method stub
		
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
