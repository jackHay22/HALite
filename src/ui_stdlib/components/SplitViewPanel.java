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
public class SplitViewPanel<Backend extends DataBackend> extends SystemPanel<Backend> {
	private JButton field_one;
	private JButton field_two;
	private Color top_color;
	private Color bot_color;
	private Color default_color;
	
	public SplitViewPanel(String label_one, String label_two, Color top_color, Color bot_color, Color border_color) {
		this.field_one = new JButton(label_one);
		this.field_two = new JButton(label_two);
		
		//set layout to vertical two panels
		this.setLayout(new GridLayout(2,0));
		
		//set border of top and bottom elements
		Border border = BorderFactory.createLineBorder(border_color);
		this.field_one.setBorder(BorderFactory.createCompoundBorder(border, 
	            BorderFactory.createEmptyBorder(4, 4, 4, 4))); 
		this.field_two.setBorder(BorderFactory.createCompoundBorder(border, 
	            BorderFactory.createEmptyBorder(4, 4, 4, 4))); 
		
		SystemThemes.button_hover(field_one);
		SystemThemes.button_hover(field_two);
		
		this.top_color = top_color;
		this.bot_color = bot_color;
		
		//get default background color
		default_color = field_one.getBackground();
		
		//set buttons to be opaque
		field_one.setOpaque(true);
		field_two.setOpaque(true);
	}
	
	public void toggle_color_top(boolean toggle_val) {
		//graphical changes on toggle
		if (toggle_val) {
			field_one.setBackground(top_color);
		}
		else {
			field_one.setBackground(default_color);
		}
	}
	
	public void toggle_color_bot(boolean toggle_val) {
		//graphical changes on toggle
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
	public void set_datastore(Backend datastore) {
	}

	@Override
	public void add_refreshable(Refreshable<Backend> refreshable_component) {
	}

	@Override
	public void on_start() {
		this.add(field_one);
		this.add(field_two);
		this.setVisible(true);
	}

}
