package ui_stdlib;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JLabel;

import system_utils.DataStore;
import ui_framework.Refreshable;

@SuppressWarnings("serial")
public class PanelHeader extends ui_framework.SystemPanel {
	private String title;
	private GridBagConstraints constraints;
	private int contraint_loc;
	
	public PanelHeader(String title) {
		super();
		this.title = title;
		this.setBackground(SystemThemes.DARK);
		this.contraint_loc = 0;
		this.add(new JLabel(title));
	}
		
	@Override
	public void refresh() {
		// TODO Auto-generated method stub
		
	}
	
	public void add_header_component(Component c) {
		constraints.gridx = this.contraint_loc;
		this.add(c);
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
		setLayout(new GridBagLayout());
		constraints = SystemThemes.get_grid_constraints();
	}
	
}
