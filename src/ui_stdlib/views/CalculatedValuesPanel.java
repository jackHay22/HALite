package ui_stdlib.views;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.ArrayList;
import javax.swing.JComboBox;
import javax.swing.JLabel;

import system_utils.DataStore;
import system_utils.Element;
import ui_framework.Refreshable;
import ui_stdlib.SystemThemes;
import ui_stdlib.components.PanelHeader;

@SuppressWarnings("serial")
public class CalculatedValuesPanel extends ui_framework.SystemPanel {
	private DataStore datastore;
	private JComboBox<Element> selection_dropdown;
	private ArrayList<CalcValSet> calc_val_set;
	private GridBagConstraints constraints;
	
	public CalculatedValuesPanel() {
		super();
		selection_dropdown = new JComboBox<Element>();
		calc_val_set = new ArrayList<CalcValSet>();
		constraints = SystemThemes.get_grid_constraints();
		//TODO get elements
	}
	
	@Override
	public void refresh() {
		graphical_purge();
		for (int i=0; i < calc_val_set.size(); i++) {
			constraints.gridy ++;
			add(calc_val_set.get(i), constraints);
		}
	}
	
	private void graphical_purge() {
		for (int i=0; i < calc_val_set.size(); i++) {
			remove(calc_val_set.get(i));
		}
	}

	@Override
	public void set_datastore(DataStore datastore) {
		this.datastore = datastore;
		
	}

	@Override
	public void add_refreshable(Refreshable refreshable_component) {
	}

	@Override
	public void on_start() {
		setLayout(new GridBagLayout());
		
		constraints.anchor = GridBagConstraints.NORTH;
		constraints.ipady = SystemThemes.HEADER_PADDING;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		PanelHeader header = new PanelHeader("Calculated Values: ", SystemThemes.MAIN);

		constraints.gridy = 0;
		header.on_start();
		this.add(header, constraints);
		
		constraints.ipady = 0;
		constraints.gridy = 1;
		constraints.weighty = 1;
		constraints.gridx = 0;
		add(selection_dropdown, constraints);
		
		constraints.gridx = 1;
		add(new JLabel("Placeholder"), constraints);
		

//		JScrollPane pane = new JScrollPane(this, 
//                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
//                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		
		//prevent pane from disappearing when shrunk
//		pane.setMinimumSize(new Dimension(200, 600));
//		this.add(pane, constraints);
		calc_val_set.add(new CalcValSet(Element.Ag));
		for (int i=0; i < calc_val_set.size(); i++) {
			calc_val_set.get(i).on_start();
		}
		refresh();
		header.setVisible(true);
	}

}
