package ui_stdlib;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.ArrayList;

import javax.swing.JComboBox;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;

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
	}
	
	@Override
	public void refresh() {
		r_sqrd_list.refresh();
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
	
	public JComboBox<String> get_rsqrd_dropdown(int size) {
		ArrayList<String> string_list = new ArrayList<String>();
		for (int i = 0; i < size; i++) {
			string_list.add(Integer.toString(i));
		}
		return new JComboBox<>(string_list.toArray(new String[0]));
	}

	@Override
	public void on_start() {
		setLayout(new GridBagLayout());
		GridBagConstraints constraints = SystemThemes.get_grid_constraints();
		constraints.anchor = GridBagConstraints.NORTH;
		
		PanelHeader header = new PanelHeader("Test");
		header.add(get_rsqrd_dropdown(10));
		
		this.add(header, constraints);
		header.on_start();
		
		
		constraints.gridy = 1;
		constraints.weighty = 1;

		JScrollPane pane = new JScrollPane(r_sqrd_list, 
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		
		//prevent pane from disappearing when shrunk
		pane.setMinimumSize(new Dimension(200, 600));
		this.add(pane, constraints);
		
		r_sqrd_list.on_start();
		
		header.setVisible(true);
		r_sqrd_list.setVisible(true);
	}
}
