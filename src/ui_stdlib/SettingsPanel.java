package ui_stdlib;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JLabel;

import system_utils.DataStore;
import ui_framework.Refreshable;

@SuppressWarnings("serial")
public class SettingsPanel extends ui_framework.SystemPanel{
	ListingSet<RSquaredListElement> r_sqrd_list;
	private DataStore data_store;
	private boolean backend_loaded;
	
	public SettingsPanel() {
		super();
		r_sqrd_list = new ListingSet<RSquaredListElement>(RSquaredListElement.class);
		setLayout(new BorderLayout());

		addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
            	//add_new_element();
            }
            @Override
            public void mouseReleased(MouseEvent e) {  
            }
        });
	}
	
	@Override
	public void refresh() {
		r_sqrd_list.refresh();
		//TODO: refresh header
	}
	
	public void add_new_element() {
		if (this.backend_loaded) {
			try {
				r_sqrd_list.display_new_element();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
			this.data_store.notify_update();
		}
	}

	@Override
	public void set_datastore(DataStore datastore) {
		this.data_store = datastore;
		this.backend_loaded = true;
		r_sqrd_list.set_datastore(datastore);
	}

	@Override
	public void add_refreshable(Refreshable refreshable_window) {
	}

	@Override
	public void on_start() {
		setLayout(new GridBagLayout());
		GridBagConstraints constraints = SystemThemes.get_grid_constraints();
//		constraints.gridx = 0;
//		constraints.gridy = 0;
		constraints.anchor = GridBagConstraints.NORTH;
		//constraints.weighty = 0.7;
		
		PanelHeader header = new PanelHeader("Test");
		this.add(header, constraints);
		header.on_start();
		header.setVisible(true);
		
		constraints.gridy = 1;
		constraints.weighty = 0.7;
		this.add(r_sqrd_list, constraints);
		r_sqrd_list.on_start();
		r_sqrd_list.setVisible(true);
	}
}
