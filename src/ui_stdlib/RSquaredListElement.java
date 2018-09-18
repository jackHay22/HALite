package ui_stdlib;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JComboBox;
import system_utils.DataStore;
import ui_framework.Refreshable;
import system_utils.Element;

@SuppressWarnings("serial")
public class RSquaredListElement extends ui_framework.SystemPanel {
	private JComboBox<String> selection_dropdown;
	//private ArrayList<RSquaredValues> r_sqrd;
	private DataStore datastore;
	private boolean backend_loaded = false;
	
	public RSquaredListElement() {
		super();
		
		Element[] elements_list = Element.class.getEnumConstants();
		String[] string_elements_list = new String[elements_list.length];
		
		for (int i = 0; i < elements_list.length; i++) {
			try {
				string_elements_list[i] = elements_list[i].toString();
			} catch (NullPointerException ex) {
			       ex.printStackTrace();
			}
		}
		
		selection_dropdown = new JComboBox<String>(string_elements_list);
		selection_dropdown.addActionListener (new ActionListener () {
		    public void actionPerformed(ActionEvent e) {
		        if (backend_loaded) {
		        	datastore.notify_update();
		        }
		    }
		});
	}
	
	public void add_rsqrd_set() {
		String element = String.valueOf(selection_dropdown.getSelectedItem());
	}

	@Override
	public void refresh() {
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
