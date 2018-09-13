package ui_stdlib;

import java.util.ArrayList;
import javax.swing.GroupLayout;
import javax.swing.JPanel;
import system_utils.DataStore;
import ui_framework.Refreshable;

public class ListingSet<E extends ui_framework.SystemPanel> extends ui_framework.SystemPanel {
	private ArrayList<E> all_elements;
	private DataStore storage_ref;
	private final Class<E> element_class;
	private GroupLayout layout;
    private GroupLayout.ParallelGroup parallel;
    private GroupLayout.SequentialGroup sequential;
    private boolean backend_loaded;
	//private ArrayList<SetElement> displayed_elements;
	
	public ListingSet(Class<E> element_class) {
		super();
		//create vertical listing layout
		layout = new GroupLayout(this);
		this.setLayout(layout);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);
        parallel = layout.createParallelGroup();
        layout.setHorizontalGroup(layout.createSequentialGroup().addGroup(parallel));
        sequential = layout.createSequentialGroup();
        layout.setVerticalGroup(sequential);
        
		this.element_class = element_class;
		all_elements = new ArrayList<E>();
	}
	
	public void display_new_element() throws InstantiationException, IllegalAccessException {
		if (this.backend_loaded) {
			E new_list_element = element_class.newInstance();
			new_list_element.set_datastore(this.storage_ref);
			all_elements.add(new_list_element);
			parallel.addGroup(layout.createSequentialGroup().
					 addComponent(new_list_element));
	        sequential.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).
	        		   addComponent(new_list_element));
	        this.storage_ref.notify_update();
		}
	}
	
	public void remove_element_at(int index) {
		if (index > 0 && index < all_elements.size()) {
			all_elements.remove(index);
			this.storage_ref.notify_update();
		}
	}
	
	@Override
	public void refresh() {
		E current_element;
		for (int i = 0; i < all_elements.size(); i++) {
			current_element = all_elements.get(i);
			current_element.refresh();
			//this.add(current_element);
		}
	}

	@Override
	public void set_datastore(DataStore datastore) {
		this.storage_ref = datastore;
		this.backend_loaded = true;
		for (int i = 0; i < all_elements.size(); i++) {
			all_elements.get(i).set_datastore(datastore);		
		}

	}

	@Override
	public void add_refreshable(Refreshable refreshable_component) {
		// TODO Auto-generated method stub

	}

}
