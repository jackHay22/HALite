package ui_stdlib.views;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import system_utils.DataStore;
import ui_framework.Refreshable;
import ui_stdlib.components.ComponentSplitView;
import system_utils.Element;
import system_utils.Pair;

@SuppressWarnings("serial")
public class RSquaredListElement extends ui_framework.ListingPanel<DataStore> {
	private JComboBox<Element> selection_dropdown;
	private DataStore datastore;
	private boolean backend_loaded = false;
	private ArrayList<RSqrdAssocSet> graphical_associations;
	private JComboBox<Element> more_elements_dropdown;
	private Element current_secondary_selected = null;
	private ComponentSplitView swap_split_view;
	
	public RSquaredListElement() {
		super();
		graphical_associations = new ArrayList<RSqrdAssocSet>();
		
		//set layout manager
		this.setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));

		//create a combo box for element values
		selection_dropdown = new JComboBox<Element>(Element.values());
		
		//add an action listener to notify the backend when there is activity
		selection_dropdown.addActionListener (new ActionListener () {
		    public void actionPerformed(ActionEvent e) {
		        if (backend_loaded) {
		        	//element selection updated
		        	datastore.notify_update();
		        }
		    }
		});
		
		JButton do_swap = new JButton("Swap");
		do_swap.addActionListener (new ActionListener () {
		    public void actionPerformed(ActionEvent e) {
		        if (backend_loaded && current_secondary_selected != null) {
		        	//element selection updated
		        	//(Element) more_elements_dropdown.getSelectedItem());
		        	//TODO set dropdown to prev
		        	datastore.swap_out_elem_in_r2(get_current_selected(), 
		        								  current_secondary_selected, 
		        								  (Element) more_elements_dropdown.getSelectedItem());
		        	more_elements_dropdown.setSelectedItem(current_secondary_selected);
		        	//TODO: temporary
		        	datastore.notify_update();
		        }
		    }
		});
		
		//create a dropdown for swapping elements
		//TODO: use AnnotatedElement to display value
		more_elements_dropdown = new JComboBox<Element>(Element.values());
		
		swap_split_view = new ComponentSplitView(more_elements_dropdown, do_swap);
	}

	@Override
	public void refresh() {
		
		//purge list on panel
		graphical_clean();
		
		//create new list for graphical assocs
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
			
			//get relevant information about selected vals from backend
			secondary_selected = datastore.check_selected_rsqrd_assocs(current_elem_self, elem_temp);
			if (secondary_selected) {
				//keep track for swap
				current_secondary_selected = elem_temp;
			}
			
			secondary_value_selected = datastore.is_pair_value_selected(current_elem_self, elem_temp);
			
			temp_r2_set = new RSqrdAssocSet(current_elem_self, elem_temp, r2_temp, 
											secondary_selected, secondary_value_selected);
			//start new set
			temp_r2_set.set_datastore(datastore);
			temp_r2_set.on_start();
			
			//add as a graphical element
			graphical_associations.add(temp_r2_set);
			
			//get graphical component of assoc set elem
			this.add(temp_r2_set.get_graphical_element());
		}
		
		add(swap_split_view);
		
		this.revalidate();
	}
	
	private void graphical_clean() {
		//clean graphical elements currently displayed
		for (int i=0;i<graphical_associations.size();i++) {
			remove(graphical_associations.get(i).get_graphical_element());
		}
		remove(swap_split_view);
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
	public void add_refreshable(Refreshable<DataStore> refreshable_component) {
	}
	
	@Override
	public void on_start() {
		this.add(selection_dropdown);
		this.setVisible(true);
		selection_dropdown.setVisible(true);
		more_elements_dropdown.setVisible(true);
	}

	@Override
	public void on_remove() {

	}
}
