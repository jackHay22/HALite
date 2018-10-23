package ui_stdlib.views;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.JComboBox;
import javax.swing.JScrollPane;
import system_utils.DataStore;
import system_utils.Element;
import ui_framework.Refreshable;
import ui_stdlib.SystemThemes;
import ui_stdlib.components.PanelHeader;
import ui_stdlib.components.SingleViewPanel;

@SuppressWarnings("serial")
public class CalculatedValuesPanel extends ui_framework.SystemPanel {
	private DataStore datastore;
	private JComboBox<Element> selection_dropdown;
	private GridBagConstraints constraints;
	private ArrayList<SingleViewPanel> header_panels;
	private JScrollPane pane;

	private CalculatedHeader header;
	private CalculatedValsScrollingSet set_list;
	private boolean backend_loaded = false;
	
	private SingleViewPanel wm_label;
	private SingleViewPanel actual_label;
	
	public CalculatedValuesPanel() {
		super();
		selection_dropdown = new JComboBox<Element>(Element.values());
		set_list = new CalculatedValsScrollingSet();

		header_panels = new ArrayList<SingleViewPanel>();
		
		wm_label =  new SingleViewPanel("WM",SystemThemes.MAIN,SystemThemes.BACKGROUND);
		actual_label =  new SingleViewPanel("Actual",SystemThemes.MAIN,SystemThemes.BACKGROUND);
		header = new CalculatedHeader(wm_label, actual_label);
	}

	@Override
	public void refresh() {
		set_list.refresh();
		
		Color main = SystemThemes.MAIN;
		Color bg = SystemThemes.BACKGROUND;
		
		header_panels.clear();
		//ArrayList<Element> header_elements = datastore.get_weighted_mean_header();
		//for (Element e : header_elements ) {
		//	header_panels.add(new SingleViewPanel(e.name(), main, bg);
		//}
		header_panels.add(new SingleViewPanel("placeholder",main,bg));
		header_panels.add(new SingleViewPanel("placeholder",main,bg));
		header_panels.add(new SingleViewPanel("placeholder",main,bg));

		header.set_panels(header_panels);

	}

	@Override
	public void set_datastore(DataStore datastore) {
		this.datastore = datastore;
		set_list.set_datastore(datastore);
		this.backend_loaded = true;
	}

	@Override
	public void add_refreshable(Refreshable refreshable_component) {
	}

	@Override
	public void on_start() {
		setLayout(new GridBagLayout());
		constraints = SystemThemes.get_grid_constraints();
		
		constraints.anchor = GridBagConstraints.NORTH;
		constraints.ipady = SystemThemes.HEADER_PADDING;
		constraints.gridwidth = 2;
		PanelHeader panel_header = new PanelHeader("Calculated Values: ", SystemThemes.MAIN);
		panel_header.on_start();
		add(panel_header, constraints);
		
		constraints.gridwidth = 1;
		constraints.gridy = 1;
		constraints.gridx = 0;
		constraints.weightx = 0;
		add(selection_dropdown, constraints);
		
		constraints.gridy = 1;
		constraints.gridx = 1;
		constraints.weightx = 1;
		add(header, constraints);
		
		header.set_panels(header_panels);
		header.on_start();

		constraints.gridwidth = 2;
		constraints.gridx = 0;
		constraints.ipady = 0;
		constraints.gridy = 2;
		constraints.weighty = 1;

		pane = SystemThemes.get_scrollable_panel(set_list);
		
		set_list.on_start();
		add(pane, constraints);
		
		selection_dropdown.addActionListener(new ActionListener () {
		    public void actionPerformed(ActionEvent e) {
		    	datastore.set_model_data_element((Element)selection_dropdown.getSelectedItem());
		    }
		});
	
		refresh();
		header.setVisible(true);
	}

}
