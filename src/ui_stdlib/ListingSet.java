package ui_stdlib;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.GroupLayout;
import javax.swing.JButton;

import system_utils.DataStore;
import ui_framework.Refreshable;

@SuppressWarnings("serial")
public class ListingSet<E extends ui_framework.SystemPanel> extends ui_framework.SystemPanel {
	private ArrayList<E> all_elements;
	private DataStore storage_ref;
	private final Class<E> element_class;
	private GroupLayout layout;
    private GroupLayout.ParallelGroup parallel;
    private GroupLayout.SequentialGroup sequential;
    private boolean backend_loaded;
	
	public ListingSet(Class<E> element_class) {
		super();
		this.element_class = element_class;
		all_elements = new ArrayList<E>();
	}
	
	public void display_new_element() throws InstantiationException, IllegalAccessException {
		if (this.backend_loaded) {
			E new_list_element = element_class.newInstance();
			all_elements.add(new_list_element);
			
			ImageButtonToggle new_button = new ImageButtonToggle("/buttons/plus_button.png","/buttons/minus_button.png", 20);
			new_button.addActionListener(new ActionListener() {

			    @Override
			    public void actionPerformed(ActionEvent e) {
			        try {
						display_new_element();
						new_button.toggle();
					} catch (InstantiationException | IllegalAccessException e1) {
						e1.printStackTrace();
					}
			    }
			});
			
			parallel.addGroup(layout.createSequentialGroup().
					 addComponent(new_list_element).addGroup(
                             layout.createSequentialGroup().addComponent(new_button)));
	        sequential.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).
	        		   addComponent(new_list_element).addGroup(
                               layout.createSequentialGroup().addComponent(new_button)));
	        
	        new_list_element.set_datastore(this.storage_ref);
	        new_list_element.on_start();
	        this.storage_ref.notify_update();
	        this.revalidate();
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
		//create vertical listing layout
		layout = new GroupLayout(this);
		this.setLayout(layout);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);
        parallel = layout.createParallelGroup();
        layout.setHorizontalGroup(layout.createSequentialGroup().addGroup(parallel));
        sequential = layout.createSequentialGroup();
        layout.setVerticalGroup(sequential);
	}
}
