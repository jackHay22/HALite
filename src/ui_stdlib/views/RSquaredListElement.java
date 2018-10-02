package ui_stdlib.views;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import system_utils.DataStore;
import ui_framework.Refreshable;
import system_utils.Element;
import system_utils.Pair;

@SuppressWarnings("serial")
public class RSquaredListElement extends ui_framework.SystemPanel {
	private JComboBox<Element> selection_dropdown;
	private DataStore datastore;
	private boolean backend_loaded = false;
	private ArrayList<RSqrdAssocSet> graphical_associations;
	
	public RSquaredListElement() {
		super();
		graphical_associations = new ArrayList<RSqrdAssocSet>();
		
		this.setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));

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
		
		//purge list on panel
		graphical_clean();
		
		graphical_associations = new ArrayList<RSqrdAssocSet>();
		
		Element current_elem_self = get_current_selected();
		ArrayList<Pair> pair_list = datastore.get_rsqrd_assoc_list(current_elem_self);
		
		Element elem_temp;
		Double r2_temp;
		Pair current_pair;
		RSqrdAssocSet temp_r2_set;
		boolean secondary_selected;
		boolean secondary_value_selected;
		
		for (int i=0;i<pair_list.size();i++) {
			
			current_pair = pair_list.get(i);
			elem_temp = current_pair.get_elem();
			r2_temp = current_pair.get_r2();
			
			secondary_selected = datastore.check_selected_rsqrd_assocs(current_elem_self, elem_temp);
			secondary_value_selected = datastore.is_pair_value_selected(current_elem_self, elem_temp);
			
			temp_r2_set = new RSqrdAssocSet(current_elem_self, elem_temp, r2_temp, 
											secondary_selected, secondary_value_selected);
			temp_r2_set.set_datastore(datastore);
			temp_r2_set.on_start();
			graphical_associations.add(temp_r2_set);
			
			this.add(temp_r2_set.get_graphical_element());
		}
		
		this.revalidate();
	}
	
	private void graphical_clean() {
		for (int i=0;i<graphical_associations.size();i++) {
			this.remove(graphical_associations.get(i).get_graphical_element());
		}
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
