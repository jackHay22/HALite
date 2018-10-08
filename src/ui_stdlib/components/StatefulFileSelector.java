package ui_stdlib.components;

import javax.swing.JButton;
import system_utils.DataStore;
import ui_framework.Refreshable;
import ui_framework.SystemPanel;

@SuppressWarnings("serial")
public class StatefulFileSelector extends SystemPanel {
	private JButton selector_button;
	
	public StatefulFileSelector(String label) {
		this.selector_button = new JButton(label);
	}

	@Override
	public void refresh() {
		// TODO Auto-generated method stub

	}

	@Override
	public void set_datastore(DataStore datastore) {
		// TODO Auto-generated method stub

	}

	@Override
	public void add_refreshable(Refreshable refreshable_component) {
		// TODO Auto-generated method stub

	}
	
	public JButton get_button() {
		return this.selector_button;
	}

	@Override
	public void on_start() {
		this.add(selector_button);
		selector_button.setVisible(true);
		this.setVisible(true);
	}

}
