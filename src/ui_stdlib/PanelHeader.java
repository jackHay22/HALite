package ui_stdlib;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.ArrayList;
import javax.swing.JLabel;
import system_utils.DataStore;
import ui_framework.Refreshable;

@SuppressWarnings("serial")
public class PanelHeader extends ui_framework.SystemPanel {
	private GridBagConstraints constraints;
	private int constraint_loc;
	private ArrayList<Refreshable> refreshable_panels;
	
	public PanelHeader(String title, Color color) {
		super();
		this.setBackground(color);
		this.constraint_loc = 0;
		this.add(new JLabel(title));
		this.constraint_loc ++;
	}
		
	@Override
	public void refresh() {
		for (int i=0; i < refreshable_panels.size(); i++) {
			refreshable_panels.get(i).refresh();
		}
	}
	
	public void add_header_component(ui_framework.SystemPanel c) {
		constraints.gridx = this.constraint_loc;
		this.add(c);
		this.constraint_loc ++;
		add_refreshable(c);
	}

	@Override
	public void set_datastore(DataStore datastore) {
	}

	@Override
	public void add_refreshable(Refreshable refreshable_component) {
		refreshable_panels.add(refreshable_component);
	}

	@Override
	public void on_start() {
		setLayout(new GridBagLayout());
		constraints = SystemThemes.get_grid_constraints();
	}
	
}
