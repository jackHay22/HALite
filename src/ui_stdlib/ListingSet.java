package ui_stdlib;

import java.util.ArrayList;

import javax.swing.JPanel;

import system_utils.DataStore;
import ui_framework.Refreshable;

public class ListingSet<E extends ui_framework.SystemPanel> extends ui_framework.SystemPanel {
	private ArrayList<E> all_elements;
	private DataStore storage_ref;
	private final Class<E> element_class;
	//private ArrayList<SetElement> displayed_elements;
	
	public ListingSet(Class<E> element_class) {
		super();
		//all_elements = ArrayList<SetElement>();
		//displayed_elements = ArrayList<SetElement>();
		this.element_class = element_class;
		all_elements = new ArrayList<E>();
	}
	
	public void display_new_element() throws InstantiationException, IllegalAccessException {
		E new_list_element = element_class.newInstance();
		all_elements.add(new_list_element);
	}
	
	public void remove_element_at(int index) {
		if (index > 0 && index < all_elements.size()) {
			all_elements.remove(index);
		}
	}
	
	@Override
	public void refresh() {
		E current_element;
		for (int i = 0; i < all_elements.size(); i++) {
			current_element = all_elements.get(i);
			current_element.refresh();
			this.add(current_element);
		}
	}

	@Override
	public void set_datastore(DataStore datastore) {
		for (int i = 0; i < all_elements.size(); i++) {
			all_elements.get(i).set_datastore(datastore);		
		}

	}

	@Override
	public void add_refreshable(Refreshable refreshable_component) {
		// TODO Auto-generated method stub

	}

}
