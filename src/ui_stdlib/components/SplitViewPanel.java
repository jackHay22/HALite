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
public class SplitViewPanel extends SystemPanel {
	private JButton field_one;
	private JButton field_two;
	private Color top_color;
	private Color bot_color;
	private Color default_color;
	
	public SplitViewPanel(String label_one, String label_two, Color top_color, Color bot_color, Color border_color) {
		this.field_one = new JButton(label_one);
		this.field_two = new JButton(label_two);
		
		this.setLayout(new GridLayout(2,0));
		
		Border border = BorderFactory.createLineBorder(border_color);
		this.field_one.setBorder(BorderFactory.createCompoundBorder(border, 
	            BorderFactory.createEmptyBorder(4, 4, 4, 4))); 
		this.field_two.setBorder(BorderFactory.createCompoundBorder(border, 
	            BorderFactory.createEmptyBorder(4, 4, 4, 4))); 
		
		SystemThemes.button_hover(field_one);
		SystemThemes.button_hover(field_two);
		
		this.top_color = top_color;
		this.bot_color = bot_color;
		default_color = field_one.getBackground();
		field_one.setOpaque(true);
		field_two.setOpaque(true);
		field_one.setFocusPainted(false);
		field_two.setFocusPainted(false);
	}
	
	public void toggle_color_top(boolean toggle_val) {
		if (toggle_val) {
			field_one.setBackground(top_color);
		}
		else {
			field_one.setBackground(default_color);
		}
	}
	
	public void toggle_color_bot(boolean toggle_val) {
		if (toggle_val) {
			field_two.setBackground(bot_color);
		}
		else {
			field_two.setBackground(default_color);
		}
	}
	
	@Override
	public void refresh() {
	}
	
	public JButton get_top() {
		return this.field_one;
	}
	public JButton get_bottom() {
		return this.field_two;
	}

	@Override
	public void set_datastore(DataStore datastore) {
	}

	@Override
	public void add_refreshable(Refreshable refreshable_component) {
	}

	@Override
	public void on_start() {
		this.add(field_one);
		this.add(field_two);
		this.setVisible(true);
	}

}
