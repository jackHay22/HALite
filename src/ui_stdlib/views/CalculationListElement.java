package ui_stdlib.views;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import system_utils.DataStore;
import system_utils.Element;
import ui_framework.Refreshable;

@SuppressWarnings("serial")
public class CalculationListElement extends ui_framework.SystemPanel {
	private DataStore datastore;
	private boolean backend_loaded = false;
	private ArrayList<CalcPairSet> graphical_associations;
	private JComboBox<Element> selection_dropdown;
	
	public CalculationListElement() {
		super();
		
		this.setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
		
		graphical_associations = new ArrayList<CalcPairSet>();
		selection_dropdown = new JComboBox<Element>(Element.values());
		selection_dropdown.addActionListener (new ActionListener () {
		    public void actionPerformed(ActionEvent e) {
		        if (backend_loaded) {
		        	//element selection updated
		        	datastore.notify_update();
		        }
		    }
		});
	}
	@Override
	public void refresh() {
		// TODO
		
	}
	
	private Element get_current_selected() {
		return (Element) selection_dropdown.getSelectedItem();
	}

	@Override
	public void set_datastore(DataStore datastore) {
		this.datastore = datastore;
		this.backend_loaded = true;
	}

	@Override
	public void add_refreshable(Refreshable refreshable_component) {
	}

	@Override
	public void on_start() {
		this.add(selection_dropdown);
		this.setVisible(true);
		selection_dropdown.setVisible(true);
	}

}
