package ui_stdlib.components;

import javax.swing.JButton;
import system_utils.DataStore;
import ui_framework.Refreshable;
import ui_framework.SystemPanel;

@SuppressWarnings("serial")
public class StatefulFileSelector extends SystemPanel {
	private JButton selector_button;
	//private ImageButton selector_button_two;
	
	public StatefulFileSelector(String label) {
		this.selector_button = new JButton(label);
		//this.selector_button_two = new ImageButton("/buttons/blank_button.png", 100, 40);
	}

	@Override
	public void refresh() {
	}

	@Override
	public void set_datastore(DataStore datastore) {
	}

	@Override
	public void add_refreshable(Refreshable refreshable_component) {
	}
	
	public JButton get_button() {
		return this.selector_button;
	}

	@Override
	public void on_start() {
		this.add(selector_button);
		//this.add(selector_button_two);
		//selector_button_two.setVisible(true);
		selector_button.setVisible(true);
		this.setVisible(true);
	}

}
