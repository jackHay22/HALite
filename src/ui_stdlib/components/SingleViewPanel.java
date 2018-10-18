package ui_stdlib.components;

import java.awt.Color;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.border.Border;
import system_utils.DataStore;
import ui_framework.Refreshable;
import ui_framework.SystemPanel;
import ui_stdlib.SystemThemes;

@SuppressWarnings("serial")
public class SingleViewPanel extends SystemPanel {
	private JButton field;
	
	public SingleViewPanel(String label, Color color, Color border_color) {
		field = new JButton(label);
		Border border = BorderFactory.createLineBorder(border_color);
		this.field.setBorder(BorderFactory.createCompoundBorder(border, 
	            BorderFactory.createEmptyBorder(4, 4, 4, 4)));
		field.setBackground(color);
		
		SystemThemes.button_hover(field);
	}
	
	public void toggle_color(boolean toggle_val) {
		this.field.setOpaque(toggle_val);
	}

	@Override
	public void refresh() {
	}

	@Override
	public void set_datastore(DataStore datastore) {	
	}

	@Override
	public void add_refreshable(Refreshable refreshable_component) {
	}

	@Override
	public void on_start() {
		add(field);
		setVisible(true);
	}
	
	public JButton get_button() {
		return this.field;
	}
}
