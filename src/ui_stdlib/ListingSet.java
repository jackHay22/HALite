package ui_stdlib;

import java.util.ArrayList;
import system_utils.DataStore;
import ui_framework.Refreshable;

public class ListingSet implements Refreshable {
	//private ArrayList<SetElement> all_elements;
	//private ArrayList<SetElement> displayed_elements;
	
	public ListingSet() {
		//all_elements = ArrayList<SetElement>();
		//displayed_elements = ArrayList<SetElement>();
	}
	
	public void display_new_element() {
		//int next_index = displayed_elements.size() + 1;
//		if (next_index <= all_elements.size();) {
//			displayed_elements.add(all_elements.get(next_index))
//		}
	}
	
	public void remove_element_at(int index) {
//		if (index > 0 && index < all_elements.size()) {
//			all_elements.remove(index);
//		}
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

}
