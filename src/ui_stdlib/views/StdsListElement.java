package ui_stdlib.views;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.ArrayList;
import javax.swing.JLabel;
import system_utils.DataStore;
import system_utils.Element;
import ui_framework.Refreshable;
import ui_framework.SystemPanel;
import ui_stdlib.SystemThemes;
import ui_stdlib.components.SingleViewPanel;
import ui_stdlib.dialogwindows.ErrorDialog;

@SuppressWarnings("serial")
public class StdsListElement extends SystemPanel {
	private SingleViewPanel weighted_mean;
	private SingleViewPanel std_dev;
	private SingleViewPanel model;
	private SingleViewPanel actual;
	private DataStore datastore;
	private boolean backend_loaded;
	private String standard;
	private GridBagConstraints constraints;
	
	private CalculatedContent list;
	private ArrayList<SingleViewPanel> panels;
	private Element elem;
	
	private double wm_val;
	private double std_dev_val;
	private double model_val;
	private double actual_val;
	
	private ArrayList<String> header_elements;
	
	public StdsListElement(String standard, Element elem, ArrayList<String> header_elements) {
		super();
		setLayout(new GridBagLayout());
		
		this.standard = standard;
		this.elem = elem;
		list = new CalculatedContent();
		
		wm_val = 0;
		std_dev_val = 0;
		model_val = 0;
		actual_val = 0;
		this.header_elements = header_elements;
		
		backend_loaded = false;
	}

	@Override
	public void refresh() {
		if (backend_loaded) {
			try {
				model_val = datastore.get_current_model(standard);
				actual_val = datastore.get_current_actual(standard);
			} catch (Exception e) {
				//open an error dialog
				System.out.println("WOULD LOAD ERROR DIALOG");
				e.printStackTrace();
				ErrorDialog error = new ErrorDialog("Error", "Failed on standard: " + standard + ", element: " + elem);
				//error.show_dialog();
			}
			
		}
		
		wm_val = datastore.get_current_WM(standard);
		std_dev_val = datastore.get_current_stdev(standard);
		
		for (String e_string : header_elements) {
			Double d = datastore.get_header_std_pair(standard, e_string);
			
			if (d != null) {
				panels.add(new SingleViewPanel(SystemThemes.get_display_number(d), SystemThemes.MAIN, SystemThemes.BACKGROUND));
			}
		}
		
		model = new SingleViewPanel(SystemThemes.get_display_number(model_val), 
				SystemThemes.HIGHLIGHT, SystemThemes.BACKGROUND);
		
		actual = new SingleViewPanel(SystemThemes.get_display_number(actual_val), 
									 SystemThemes.HIGHLIGHT, SystemThemes.BACKGROUND);
		
		panels.add(weighted_mean);
		panels.add(std_dev);
		panels.add(model);
		panels.add(actual);
		
		list.set_panels(panels);
		
		constraints.weightx = 0;
		
		JLabel standards = new JLabel(standard);
		standards.setPreferredSize(new Dimension(70, 12));
		add(standards, constraints);
		
		constraints.ipadx = 0;
		constraints.weightx = 1;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		add(list, constraints);
		
		revalidate();
	}

	@Override
	public void set_datastore(DataStore datastore) {
		this.datastore = datastore;
		backend_loaded = true;
	}

	@Override
	public void add_refreshable(Refreshable refreshable_component) {
	}

	@Override
	public void on_start() {
		constraints = SystemThemes.get_grid_constraints();
		panels = new ArrayList<SingleViewPanel>();	
	}

}
