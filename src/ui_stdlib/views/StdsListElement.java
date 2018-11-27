package ui_stdlib.views;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.JLabel;
import system_utils.DataStore;
import system_utils.Element;
import ui_framework.Refreshable;
import ui_framework.SystemPanel;
import ui_stdlib.SystemThemes;
import ui_stdlib.components.SingleViewPanel;

@SuppressWarnings("serial")
public class StdsListElement extends SystemPanel<DataStore> {
	private SingleViewPanel<DataStore> model;
	private SingleViewPanel<DataStore> actual;
	private DataStore datastore;
	private boolean backend_loaded;
	private String standard;
	private GridBagConstraints constraints;
	
	private CalculatedContent list;
	private ArrayList<SingleViewPanel<DataStore>> panels;
	
	private Double model_val;
	private Double actual_val;
	
	private ArrayList<String> header_labels;
	private ArrayList<Element> header_elems;
	
	public StdsListElement(String standard, ArrayList<Element> header_elems, ArrayList<String> header_labels) {
		super();
		//set layout manager
		setLayout(new GridBagLayout());
		
		this.standard = standard;
		list = new CalculatedContent();

		//set defaults
		model_val = 0.0;
		actual_val = 0.0;
		this.header_labels = header_labels;
		this.header_elems = header_elems;
		
		backend_loaded = false;
	}

	@Override
	public void refresh() {
		//only make updates if backend has been loaded
		if (backend_loaded) {
			
			//attempt to get information (if it exists) from datastore
			try {
				actual_val = datastore.get_current_actual(standard);
			} catch (Exception e) {
				actual_val = null;
			}
			
			try {
				model_val = datastore.get_current_model(standard);
			} catch (Exception e) {
				model_val = null;
			}
		}
		
		for (Element disp_e : header_elems) {
			//get info from backend
			Double d = datastore.get_header_std_pair(standard, disp_e.toString());
			String display = "-";
			
			//format number 
			if (d != null) {
				display = SystemThemes.get_display_number(d);
			}
			
			//create a graphical panel to store element 
			SingleViewPanel<DataStore> element_panel = new SingleViewPanel<DataStore>(display, SystemThemes.HIGHLIGHT, SystemThemes.BACKGROUND);
			
			element_panel.toggle_color(datastore.not_used_in_model(standard, disp_e));
			
			//set an action listener to notify backend on selection
			element_panel.get_button().addActionListener(new ActionListener () {
			    public void actionPerformed(ActionEvent e) {
					datastore.toggle_sample_elem_pair(standard, disp_e);
			    }
			});
			
			
			panels.add(element_panel);	
		}
		
		for (String l_string : header_labels) {
			//get from backend
			Double d = datastore.get_header_std_pair(standard, l_string);
			String display = "-";

			//get display
			if (d != null) {
				display = SystemThemes.get_display_number(d);
			}

			panels.add(new SingleViewPanel<DataStore>(display, SystemThemes.MAIN, SystemThemes.BACKGROUND));
		}
		
		//create individual standalone panels
		model = new SingleViewPanel<DataStore>(SystemThemes.get_display_number(model_val), 
				SystemThemes.HIGHLIGHT, SystemThemes.BACKGROUND);
		
		actual = new SingleViewPanel<DataStore>(SystemThemes.get_display_number(actual_val), 
									 SystemThemes.HIGHLIGHT, SystemThemes.BACKGROUND);
		
		panels.add(model);
		panels.add(actual);
		
		list.set_panels(panels);
		
		//use grid bag constraints to add components
		constraints.weightx = 0;
		
		JLabel standards = new JLabel(standard);
		standards.setToolTipText(standard);
		
		//lines up standards label width with dropdown width
		standards.setPreferredSize(new Dimension(70, 12));
		add(standards, constraints);
		
		constraints.ipadx = 0;
		constraints.weightx = 1;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		add(list, constraints);
		
		//reformat
		revalidate();
	}

	@Override
	public void set_datastore(DataStore datastore) {
		this.datastore = datastore;
		backend_loaded = true;
	}

	@Override
	public void add_refreshable(Refreshable<DataStore> refreshable_component) {
	}

	@Override
	public void on_start() {
		constraints = SystemThemes.get_grid_constraints();
		panels = new ArrayList<SingleViewPanel<DataStore>>();	
	}

}
