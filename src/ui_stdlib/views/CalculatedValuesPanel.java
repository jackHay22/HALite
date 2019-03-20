package ui_stdlib.views;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import system_utils.DataStore;
import system_utils.Element;
import ui_framework.Refreshable;
import ui_stdlib.SystemThemes;
import ui_stdlib.components.PanelHeader;
import ui_stdlib.components.SingleViewPanel;

@SuppressWarnings("serial")
public class CalculatedValuesPanel extends ui_framework.SystemPanel<DataStore> {
	private DataStore datastore;
	private JComboBox<Element> selection_dropdown;
	private GridBagConstraints constraints;
	private ArrayList<SingleViewPanel<DataStore>> header_panels;
	private JScrollPane pane;
	private CalculatedContent header;
	private CalculatedValsScrollingSet set_list;
	private boolean backend_loaded = false;
	private SingleViewPanel<DataStore> model_label;
	private SingleViewPanel<DataStore> actual_label;
	
	public CalculatedValuesPanel() {
		super();
		selection_dropdown = new JComboBox<Element>(Element.values());
		set_list = new CalculatedValsScrollingSet();

		header_panels = new ArrayList<SingleViewPanel<DataStore>>();
		
		//default labels that don't need to be recreated
		model_label =  new SingleViewPanel<DataStore>("Model",SystemThemes.MAIN,SystemThemes.BACKGROUND);
		actual_label =  new SingleViewPanel<DataStore>("Actual",SystemThemes.MAIN,SystemThemes.BACKGROUND);

		//graphical header panel
		header = new CalculatedContent();
	}

	@Override
	public void refresh() {
		//prevent complete rebuild of calculated values listing panel if no changes
		//occurred in backend
		
		//check if there were updates before rebuilding entire list
		if (datastore.calculated_vals_updated) {
			
			//set update flag to false
			datastore.calculated_vals_updated = false;
			set_list.refresh();
		
			Color main = SystemThemes.MAIN;
			Color bg = SystemThemes.BACKGROUND;
			
			//clear content
			header_panels.clear();
			
			//get header values from backend
			ArrayList<Element> header_elems = datastore.get_WM_elems();
			ArrayList<String> header_labels = datastore.get_WM_headers();
			
			
			//add graphical panels for values
			for (Element elem : header_elems ) {
				header_panels.add(new SingleViewPanel<DataStore>(elem.toString(), main, bg));
			}
			
			for (String l_string : header_labels) {
				header_panels.add(new SingleViewPanel<DataStore>(l_string, main, bg));
			}
			
			header_panels.add(model_label);
			header_panels.add(actual_label);
			header.set_panels(header_panels);
			
			//reformat based on updates
			revalidate();
		}
	}

	@Override
	public void set_datastore(DataStore datastore) {
		this.datastore = datastore;
		set_list.set_datastore(datastore);
		this.backend_loaded = true;
	}

	@Override
	public void add_refreshable(Refreshable<DataStore> refreshable_component) {
	}

	@Override
	public void on_start() {
		
		//using grid bag layout
		setLayout(new GridBagLayout());
		constraints = SystemThemes.get_grid_constraints();
		
		//format panel orientation through GB constraints
		constraints.anchor = GridBagConstraints.NORTH;
		constraints.ipady = SystemThemes.HEADER_PADDING;
		constraints.gridwidth = 2;
		constraints.gridx = 0;
		constraints.weightx = 1;
		PanelHeader<DataStore> panel_header = new PanelHeader<DataStore>("Calculated Values: ", SystemThemes.MAIN);
		JButton remove_outliers = new JButton("Remove Outliers for Element");
		
		remove_outliers.addActionListener(new ActionListener () {
		    public void actionPerformed(ActionEvent e) {
		    	datastore.remove_outliers_for_element();
		    }
		});
		
		panel_header.on_start();
		
		//add header label
		add(panel_header, constraints);
		
		constraints.gridwidth = 1;
		constraints.gridx = 2;
		constraints.gridy = 0;
		constraints.weightx = 0;
		add(remove_outliers, constraints);
		
		constraints.gridwidth = 1;
		constraints.gridy = 1;
		constraints.gridx = 0;
		constraints.weightx = 0;
		
		//add dropdown
		add(selection_dropdown, constraints);
		
		constraints.gridy = 1;
		constraints.gridx = 1;
		constraints.weightx = 1;
		constraints.gridwidth = 2;
		add(header, constraints);
		
		//set subpanels of header crossbar
		header.set_panels(header_panels);
		header.on_start();
		
		constraints.gridx = 2;
		constraints.weightx = 0.1;
		constraints.gridwidth = 1;
		
		//add label to far right to align header with scrollable section
		//TODO fix this formatting issue
		JLabel format_placeholder = new JLabel("");
		format_placeholder.setMinimumSize(new Dimension(30, 10));
		add(format_placeholder, constraints);

		constraints.gridwidth = 3;
		constraints.gridx = 0;
		constraints.ipady = 0;
		constraints.gridy = 2;
		constraints.weighty = 1;

		//turn set list into a scrollable pane
		pane = SystemThemes.get_scrollable_panel(set_list);
		
		set_list.on_start();
		add(pane, constraints);
		
		//add an action listener to the dropdown to update backend
		selection_dropdown.addActionListener(new ActionListener () {
		    public void actionPerformed(ActionEvent e) {
		    	datastore.set_model_data_element((Element)selection_dropdown.getSelectedItem());
		    }
		});
	
		header.setVisible(true);
		
		if (backend_loaded) {
			//if backend has been loaded when panel is started, set initial item in backend
			datastore.set_model_data_element((Element)selection_dropdown.getSelectedItem());
		}
	}
}
