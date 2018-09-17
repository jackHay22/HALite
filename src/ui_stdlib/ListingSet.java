package ui_stdlib;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import system_utils.DataStore;
import ui_framework.Refreshable;

@SuppressWarnings("serial")
public class ListingSet<E extends ui_framework.SystemPanel> extends ui_framework.SystemPanel {
	private ArrayList<E> all_elements;
	private DataStore storage_ref;
	private final Class<E> element_class;
	private GridBagConstraints constraints;
    private boolean backend_loaded;
    private final int button_row = 2;
	
	public ListingSet(Class<E> element_class) {
		super();
		this.element_class = element_class;
		all_elements = new ArrayList<E>();
	}
	
	public void display_new_element() throws InstantiationException, IllegalAccessException {
		if (this.backend_loaded) {
			E new_list_element = element_class.newInstance();
			all_elements.add(new_list_element);
			
			ImageButton new_button = new ImageButton("/buttons/minus_button.png", 20);
			new_button.addActionListener(new ActionListener() {
			    @Override
			    public void actionPerformed(ActionEvent e) {
			        remove_element(new_list_element);
			        remove(new_button);
			    }
			});
			
			constraints.gridx = 0;
			constraints.anchor = GridBagConstraints.NORTH;
		    add(new_list_element, constraints); 
		    constraints.gridx = button_row;
		    add(new_button, constraints); 
	        
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
		this.storage_ref = datastore;
		this.backend_loaded = true;
	}

	@Override
	public void add_refreshable(Refreshable refreshable_component) {
	}

	@Override
	public void on_start() {
		setLayout(new GridBagLayout());
		constraints = SystemThemes.get_grid_constraints();
		ImageButton new_button = new ImageButton("/buttons/plus_button.png", 20);
		constraints.gridx = button_row;

		constraints.anchor = GridBagConstraints.NORTH;
	    add(new_button, constraints); 
	    
		new_button.addActionListener(new ActionListener() {
		    @Override
		    public void actionPerformed(ActionEvent e) {
		    	try {
					display_new_element();
				} catch (InstantiationException | IllegalAccessException e1) {
					e1.printStackTrace();
				}
		    }
		});
	}
}
