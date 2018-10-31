package ui_stdlib.views;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.JComboBox;
import javax.swing.JScrollPane;
import system_utils.DataStore;
import ui_framework.Refreshable;
import ui_stdlib.SystemThemes;
import ui_stdlib.components.ListingSet;
import ui_stdlib.components.PanelHeader;

@SuppressWarnings("serial")
public class R2SettingsPanel extends ui_framework.SystemPanel{
	private ListingSet<RSquaredListElement> r_sqrd_list;
	private DataStore data_store;
	private int display_rsqrd_assocs = 9; //-1 
	private JScrollPane pane;
	
	public R2SettingsPanel() {
		super();
		r_sqrd_list = new ListingSet<RSquaredListElement>(RSquaredListElement.class);
	}
	
	@Override
	public void refresh() {
		r_sqrd_list.refresh();
		revalidate();
	}
	
	@Override
	public void set_datastore(DataStore datastore) {
		this.data_store = datastore;
		r_sqrd_list.set_datastore(datastore);
	}

	@Override
	public void add_refreshable(Refreshable refreshable_window) {
	}
	
	public JComboBox<String> get_rsqrd_dropdown(int size) {
		ArrayList<String> string_list = new ArrayList<String>();
		for (int i = 0; i < size; i++) {
			string_list.add(Integer.toString(i + 1));
		}
		
		JComboBox<String> rsqrd_total = new JComboBox<>(string_list.toArray(new String[0]));
		rsqrd_total.addActionListener(new ActionListener () {
		    public void actionPerformed(ActionEvent e) {
		    	data_store.set_elem_num(Integer.valueOf((String)rsqrd_total.getSelectedItem()));
		    }
		});
		
		rsqrd_total.setSelectedIndex(display_rsqrd_assocs);
		return rsqrd_total;
	}
	
	@Override
	public void on_start() {
		setLayout(new GridBagLayout());
		GridBagConstraints constraints = SystemThemes.get_grid_constraints();
		constraints.anchor = GridBagConstraints.NORTH;
		constraints.ipady = SystemThemes.HEADER_PADDING;
		PanelHeader header = new PanelHeader("R^2 Entries to display: ", SystemThemes.MAIN);

		header.add(get_rsqrd_dropdown(10));
		this.add(header, constraints);
		header.on_start();
		
		constraints.ipady = 0;
		constraints.gridy = 1;
		constraints.weighty = 1;
		
		pane = SystemThemes.get_scrollable_panel(r_sqrd_list);

		this.add(pane, constraints);
		
		r_sqrd_list.on_start();
		
		header.setVisible(true);
		r_sqrd_list.setVisible(true);
	}
}
