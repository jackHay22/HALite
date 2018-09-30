package ui_stdlib.views;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JScrollPane;

import system_utils.DataStore;
import ui_framework.Refreshable;
import ui_stdlib.SystemThemes;
import ui_stdlib.components.ListingSet;
import ui_stdlib.components.PanelHeader;

@SuppressWarnings("serial")
public class CalculatedValuesPanel extends ui_framework.SystemPanel {
	private ListingSet<CalculationListElement> calculated_elements_list;
	private DataStore datastore;
	
	public CalculatedValuesPanel() {
		super();
		calculated_elements_list = new ListingSet<CalculationListElement>(CalculationListElement.class);
	}
	
	@Override
	public void refresh() {
		calculated_elements_list.refresh();
	}

	@Override
	public void set_datastore(DataStore datastore) {
		this.datastore = datastore;
		calculated_elements_list.set_datastore(datastore);
	}

	@Override
	public void add_refreshable(Refreshable refreshable_component) {
	}

	@Override
	public void on_start() {
		setLayout(new GridBagLayout());
		GridBagConstraints constraints = SystemThemes.get_grid_constraints();
		constraints.anchor = GridBagConstraints.NORTH;
		constraints.ipady = SystemThemes.HEADER_PADDING;
		PanelHeader header = new PanelHeader("Calculated Values: ", SystemThemes.MAIN);

		this.add(header, constraints);
		header.on_start();
		
		constraints.ipady = 0;
		constraints.gridy = 1;
		constraints.weighty = 1;

		JScrollPane pane = new JScrollPane(calculated_elements_list, 
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		
		//prevent pane from disappearing when shrunk
		pane.setMinimumSize(new Dimension(200, 600));
		this.add(pane, constraints);
		
		header.setVisible(true);
		
		calculated_elements_list.on_start();
		calculated_elements_list.setVisible(true);
	}

}
