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
	private String standard;
	private GridBagConstraints constraints;
	
	private CalculatedContent list;
	private Element elem;
	
	public StdsListElement(String standard, Element elem) {
		super();
		setLayout(new GridBagLayout());
		
		this.standard = standard;
		this.elem = elem;
		list = new CalculatedContent();
	}

	@Override
	public void refresh() {
	}

	@Override
	public void set_datastore(DataStore datastore) {
		this.datastore = datastore;
	}
	
	public void set_element(Element elem) {
		this.elem = elem;
	}

	@Override
	public void add_refreshable(Refreshable refreshable_component) {
		
	}

	@Override
	public void on_start() {
		constraints = SystemThemes.get_grid_constraints();
		ArrayList<SingleViewPanel> panels = new ArrayList<SingleViewPanel>();
		
		double wm_val = 0;
		double actual_val = 0;
		weighted_mean = new SingleViewPanel(SystemThemes.get_display_number(wm_val), 
											SystemThemes.HIGHLIGHT, SystemThemes.BACKGROUND);
		
		actual = new SingleViewPanel(SystemThemes.get_display_number(actual_val), 
									 SystemThemes.HIGHLIGHT, SystemThemes.BACKGROUND);
		
		//Get values based on elements
		
		panels.add(new SingleViewPanel(".000", SystemThemes.MAIN, SystemThemes.BACKGROUND));
		panels.add(new SingleViewPanel(".000", SystemThemes.MAIN, SystemThemes.BACKGROUND));
		panels.add(new SingleViewPanel(".000", SystemThemes.MAIN, SystemThemes.BACKGROUND));
		
		panels.add(weighted_mean);
		panels.add(actual);
		list.set_panels(panels);
		
		constraints.weightx = 0;
//		constraints.ipadx = SystemThemes.HEADER_PADDING;
		JLabel standards = new JLabel(standard);
		standards.setPreferredSize(new Dimension(70, 10));
		add(standards, constraints);
		
		constraints.ipadx = 0;
		constraints.weightx = 1;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		add(list, constraints);
		
	}

}
