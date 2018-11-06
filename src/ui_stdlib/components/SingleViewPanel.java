package ui_stdlib.components;

import java.awt.Color;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.border.Border;
import ui_framework.DataBackend;
import ui_framework.Refreshable;
import ui_framework.SystemPanel;
import ui_stdlib.SystemThemes;

@SuppressWarnings("serial")
public class SingleViewPanel<Backend extends DataBackend> extends SystemPanel<Backend> {
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
	
	public void toggle_color(boolean toggle) {
		if (toggle) {
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
	public void set_datastore(Backend datastore) {	
	}

	@Override
	public void add_refreshable(Refreshable<Backend> refreshable_component) {
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
