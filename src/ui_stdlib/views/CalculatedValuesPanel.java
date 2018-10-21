package ui_stdlib.views;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
		selection_dropdown = new JComboBox<Element>(Element.values());
		calc_val_set = new ArrayList<CalcValSet>();
		
		//TODO get elements
	}
	
	@Override
	public void refresh() {
		graphical_purge();
		for (int i=0; i < calc_val_set.size(); i++) {
			constraints.gridy ++;
			constraints.anchor = GridBagConstraints.NORTH;
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
		constraints = SystemThemes.get_grid_constraints();
		
		constraints.anchor = GridBagConstraints.NORTH;
		constraints.ipady = SystemThemes.HEADER_PADDING;
		//constraints.weighty = 1;
		PanelHeader header = new PanelHeader("Calculated Values: ", SystemThemes.MAIN);

		constraints.gridy = 0;
		this.add(header, constraints);
		header.on_start();
		
		constraints.ipady = 0;
		constraints.gridy = 1;
		constraints.gridx = 0;
		constraints.weightx = 0;
		constraints.weighty = 1;
		add(selection_dropdown, constraints);
		
		constraints.gridx = 1;
		constraints.weightx = 1;
		add(new JLabel("Placeholder"), constraints);

		constraints.gridy = 2;
		
		selection_dropdown.addActionListener(new ActionListener () {
		    public void actionPerformed(ActionEvent e) {
		    	datastore.set_model_data_element((Element)selection_dropdown.getSelectedItem());
		    }
		});
		
		ArrayList<Element> temp_elements = new ArrayList<Element>();
		temp_elements.add(Element.Ag);
		temp_elements.add(Element.Ag);
		temp_elements.add(Element.Ag);
		calc_val_set.add(new CalcValSet(temp_elements, 0.0, 0.0));
		for (int i=0; i < calc_val_set.size(); i++) {
			calc_val_set.get(i).on_start();
		}
		refresh();
		header.setVisible(true);
	}

}
