package ui_stdlib.components;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import system_utils.DataStore;
import ui_framework.Refreshable;
import ui_stdlib.SystemThemes;

@SuppressWarnings("serial")
public class ListingSet<E extends ui_framework.ListingPanel<DataStore>> extends ui_framework.SystemPanel<DataStore> {
	private ArrayList<E> all_elements;
	private DataStore storage_ref;
	private final Class<E> element_class;
	private GridBagConstraints constraints;
    private boolean backend_loaded;
    private ImageButton new_element_button;
    private int current_element_row = 0;
	
	public ListingSet(Class<E> element_class) {
		super();
		this.element_class = element_class;
		all_elements = new ArrayList<E>();
	}
	
	public void display_new_element() throws InstantiationException, IllegalAccessException {
		if (this.backend_loaded) {
			E new_list_element = element_class.newInstance();
			all_elements.add(new_list_element);
			
			ImageButton new_minus_button = new ImageButton("/buttons/minus_button.png", 20);
			new_minus_button.addActionListener(new ActionListener() {
			    @Override
			    public void actionPerformed(ActionEvent e) {
			    	new_list_element.on_remove();
			        remove_element(new_list_element);
			        remove(new_minus_button);
			    }
			});
			
			constraints.gridy = current_element_row;
			constraints.gridx = 0;
			constraints.weightx = 1.0;
			constraints.anchor = GridBagConstraints.NORTH;
		    add(new_list_element, constraints); 
		    
		    constraints.gridx = 1;
		    constraints.weightx = 0.0;
		    constraints.anchor = GridBagConstraints.EAST;
		    add(new_minus_button, constraints); 
	        
		    //TODO
	        new_list_element.set_datastore(this.storage_ref);
	        new_list_element.on_start();
	        this.storage_ref.notify_update();

	        this.revalidate();
		}
	}
	
	public void remove_element(E elem) {
		remove(elem);
		this.storage_ref.notify_update();
	}
	
	@Override
	public void refresh() {
		for (int i = 0; i < all_elements.size(); i++) {
			all_elements.get(i).refresh();
		}
	}

	@Override
	public void set_datastore(DataStore datastore) {
		this.storage_ref =datastore;
		this.backend_loaded = true;
	}

	@Override
	public void add_refreshable(Refreshable<DataStore> refreshable_component) {
	}
	
	private void add_plus_to_bottom() {
		constraints.gridy = current_element_row + 1;
		add(new_element_button, constraints);
	}

	@Override
	public void on_start() {
		setLayout(new GridBagLayout());
		constraints = SystemThemes.get_grid_constraints();
		
		new_element_button = new ImageButton("/buttons/plus_button.png", 20);
		constraints.gridx = 1;
		constraints.anchor = GridBagConstraints.SOUTH;
	    add(new_element_button, constraints); 
	    
		new_element_button.addActionListener(new ActionListener() {
		    @Override
		    public void actionPerformed(ActionEvent e) {
		    	try {
		    		current_element_row++;
		    		remove(new_element_button);
					display_new_element();
					add_plus_to_bottom();
				} catch (InstantiationException | IllegalAccessException e1) {
					e1.printStackTrace();
				}
		    }
		});
	}
}
