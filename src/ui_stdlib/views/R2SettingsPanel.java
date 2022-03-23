package ui_stdlib.views;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import system_utils.DataStore;
import ui_framework.Refreshable;
import ui_stdlib.SystemThemes;
import ui_stdlib.components.PanelHeader;

@SuppressWarnings("serial")
public class R2SettingsPanel extends ui_framework.SystemPanel<DataStore>{
	private RSquaredListElement r_sqrd_list;
	private DataStore data_store;
	private JScrollPane pane;
	
	private int sense_outliers_sel = 1;
	
	public R2SettingsPanel() {
		super();
		
		r_sqrd_list = new RSquaredListElement();
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
	public void add_refreshable(Refreshable<DataStore> refreshable_window) {
	}
	
	private JMenuItem get_elim_highest_sensitivity() {
		JMenuItem auto_sel_elems_button = new JMenuItem("Elim. Highest Sensitivity");
		auto_sel_elems_button.addActionListener(new ActionListener () {
		    public void actionPerformed(ActionEvent e) {
		    	data_store.elim_highest_sensitivity();
		    }
		});
		return auto_sel_elems_button;
	}
	
	private JMenu get_auto_sel_submenu() {
		JMenu auto_sel_submenu = new JMenu("Auto-select Elements...");
		
		JMenuItem all = new JMenuItem("All");
		all.addActionListener(new ActionListener () {
		    public void actionPerformed(ActionEvent e) {
		    	data_store.try_in_order_for_primary(false);
		    }
		});
		auto_sel_submenu.add(all);
		
		JMenuItem subset = new JMenuItem("Subset");
		subset.addActionListener(new ActionListener () {
		    public void actionPerformed(ActionEvent e) {
		    	data_store.try_in_order_for_primary(true);
		    }
		});
		auto_sel_submenu.add(subset);
		
		return auto_sel_submenu;
	}
	
	private JComboBox<String> get_sens_outliers_dropdown(int size) {
		//create a dropdown for rsqrd vals to display
		
		ArrayList<String> string_list = new ArrayList<String>();
		for (int i = 0; i < size; i++) {
			string_list.add(Integer.toString(i + 1));
		}
		
		//add backend action listener
		JComboBox<String> remove_outliers = new JComboBox<>(string_list.toArray(new String[0]));
		remove_outliers.addActionListener(new ActionListener () {
		    public void actionPerformed(ActionEvent e) {
		    	sense_outliers_sel = Integer.valueOf((String)remove_outliers.getSelectedItem());
		    }
		});
		
		//set default index
		remove_outliers.setSelectedIndex(0);
		return remove_outliers;
	}
	
	private JMenuItem get_sens_outliers_button() {
		JMenuItem sense_outliers_button = new JMenuItem("Remove Outliers");
		sense_outliers_button.addActionListener(new ActionListener () {
		    public void actionPerformed(ActionEvent e) {
		    	data_store.remove_n_outliers(sense_outliers_sel);
		    }
		});
		return sense_outliers_button;
	}
	
	@Override
	public void on_start() {
		
		//set layout manager to GBC
		setLayout(new GridBagLayout());
		GridBagConstraints constraints = SystemThemes.get_grid_constraints();
		constraints.anchor = GridBagConstraints.NORTH;
		constraints.ipady = SystemThemes.HEADER_PADDING;
		
		//add header label
		PanelHeader<DataStore> header = new PanelHeader<DataStore>("Sensitivity Outliers", SystemThemes.MAIN);

		//add dropdown for outliers count
		header.add(get_sens_outliers_dropdown(SystemThemes.SENS_OUTLIERS_SIZE));
		
		header.add(Box.createHorizontalStrut(50));
		
		JMenuBar actions_bar = new JMenuBar();
		
		//create the actions menu
		JMenu actions_dropdown = new JMenu("Actions");
		actions_bar.add(actions_dropdown);
		
		actions_dropdown.add(get_sens_outliers_button());
		actions_dropdown.add(get_auto_sel_submenu());
		actions_dropdown.add(get_elim_highest_sensitivity());
		
		header.add(actions_bar);
		this.add(header, constraints);

		header.on_start();

		constraints.ipady = 0;
		constraints.gridy = 1;
		constraints.weighty = 1;
		
		//turn this listing set into a scrollable pane
		pane = SystemThemes.get_scrollable_panel(r_sqrd_list);

		this.add(pane, constraints);
		
		//start pane
		r_sqrd_list.on_start();
		
		//set graphical elements visible
		header.setVisible(true);
		r_sqrd_list.setVisible(true);
	}
}
