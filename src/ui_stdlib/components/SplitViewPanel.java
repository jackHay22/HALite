package ui_stdlib.components;

import java.awt.Color;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.border.Border;
import system_utils.DataStore;
import ui_framework.Refreshable;
import ui_framework.SystemPanel;

@SuppressWarnings("serial")
public class SplitViewPanel extends SystemPanel {
	private JButton field_one;
	private JButton field_two;
	
	public SplitViewPanel(String label_one, String label_two, Color top_color, Color bot_color, Color border_color) {
		this.field_one = new JButton(label_one);
		this.field_two = new JButton(label_two);
		
		this.setLayout(new GridLayout(2,0));
		
		Border border = BorderFactory.createLineBorder(border_color);
		this.field_one.setBorder(BorderFactory.createCompoundBorder(border, 
	            BorderFactory.createEmptyBorder(4, 4, 4, 4))); 
		this.field_two.setBorder(BorderFactory.createCompoundBorder(border, 
	            BorderFactory.createEmptyBorder(4, 4, 4, 4))); 
		
		field_one.setBackground(top_color);
		field_two.setBackground(bot_color);
	}
	
	public void toggle_color_top(boolean toggle_val) {
		this.field_one.setOpaque(toggle_val);
	}
	
	public void toggle_color_bot(boolean toggle_val) {
		this.field_two.setOpaque(toggle_val);
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
