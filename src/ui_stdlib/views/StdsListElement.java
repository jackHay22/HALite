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
import ui_stdlib.components.SingleViewBar;
import ui_stdlib.components.SingleViewPanel;

@SuppressWarnings("serial")
public class StdsListElement extends SystemPanel {
	private SingleViewPanel weighted_mean;
	private SingleViewPanel actual;
	private DataStore datastore;
	private boolean backend_loaded;
	private String standard;
	private GridBagConstraints constraints;
	
	private CalculatedContent list;
	private ArrayList<SingleViewPanel> panels;
	private Element elem;
	
	private double wm_val;
	private double actual_val;
	
	private ArrayList<Element> header_elements;
	
	public StdsListElement(String standard, Element elem, ArrayList<Element> header_elements) {
		super();
		setLayout(new GridBagLayout());
		
		this.standard = standard;
		this.elem = elem;
		list = new CalculatedContent();
		
		wm_val = 0;
		actual_val = 0;
		this.header_elements = header_elements;
		
		backend_loaded = false;
	}

	@Override
	public void refresh() {
		if (backend_loaded) {
			wm_val = datastore.get_current_WM(standard);
			actual_val = datastore.get_current_actual(standard);
		}

		weighted_mean = new SingleViewPanel(SystemThemes.get_display_number(wm_val), 
											SystemThemes.HIGHLIGHT, SystemThemes.BACKGROUND);
		
		actual = new SingleViewPanel(SystemThemes.get_display_number(actual_val), 
									 SystemThemes.HIGHLIGHT, SystemThemes.BACKGROUND);
		
		for (Element e : header_elements) {
			Double d = datastore.get_element_std_pair(standard, e);
			
			if (d != null) {
				panels.add(new SingleViewPanel(SystemThemes.get_display_number(d), SystemThemes.MAIN, SystemThemes.BACKGROUND));
			}
		}
		
		panels.add(weighted_mean);
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
