package ui_stdlib.components;

import java.awt.Color;
import java.awt.GridLayout;

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
	private Color color;
	private Color default_color;
	
	public SingleViewPanel(String label, Color color, Color border_color) {
		super();
		field = new JButton(label);
		
		this.setLayout(new GridLayout(1,0));
		Border border = BorderFactory.createLineBorder(border_color);
		this.field.setBorder(BorderFactory.createCompoundBorder(border, 
	            BorderFactory.createEmptyBorder(4, 4, 4, 4)));
		
		this.color = color;
		this.default_color = field.getBackground();
		
		SystemThemes.button_hover(field);
		field.setOpaque(true);
	}
	
	public void toggle_color(boolean toggle_val) {
		if (toggle_val) {
			field.setBackground(color);
		}
		else {
			field.setBackground(default_color);
		}
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
