package ui_stdlib.components;

import java.awt.Color;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.ArrayList;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;
import ui_framework.DataBackend;
import ui_framework.Refreshable;
import ui_stdlib.SystemThemes;

@SuppressWarnings("serial")
public class PanelHeader<Backend extends DataBackend> extends ui_framework.SystemPanel<Backend> {
	private GridBagConstraints constraints;
	private int constraint_loc;
	private ArrayList<Refreshable<Backend>> refreshable_panels;
	private JLabel title;
	
	public PanelHeader(String title, Color color) {
		super();
		this.setBackground(color);
		this.constraint_loc = 0;
		this.title = new JLabel(title);
		this.add(this.title, constraints);
		this.constraint_loc ++;
	}
	
	public PanelHeader(String title, Color color, int inset) {	
		this(title,color);
		setBorder(new EmptyBorder(inset, inset, inset, inset));
	}
		
	@Override
	public void refresh() {
		for (int i=0; i < refreshable_panels.size(); i++) {
			refreshable_panels.get(i).refresh();
		}
	}
	
	public void set_font_size(float size) {
		title.setFont(title.getFont().deriveFont(size));
		revalidate();
	}
	
	public void add_header_component(ui_framework.SystemPanel<Backend> c, int weight) {
		constraints.gridx = this.constraint_loc;
		constraints.weightx = weight;
		this.add(c, constraints);
		this.constraint_loc ++;
		add_refreshable(c);
	}
	
	public void add_maximized_component(Component c) {
		constraints.weightx = 1;
		add(c, constraints);
	}

	@Override
	public void set_datastore(Backend datastore) {
	}
	
	public void set_text(String text) {
		title.setText(text);
	}

	@Override
	public void add_refreshable(Refreshable<Backend> refreshable_component) {
		refreshable_panels.add(refreshable_component);
	}

	@Override
	public void on_start() {
		setLayout(new GridBagLayout());
		constraints = SystemThemes.get_grid_constraints();
	}
	
}
