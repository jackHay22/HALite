package ui_stdlib;

import java.awt.BorderLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import system_utils.DataStore;
import ui_framework.Refreshable;

@SuppressWarnings("serial")
public class SettingsPanel extends ui_framework.SystemPanel{
	ListingSet<RSquaredListElement> r_sqrd_list;
	private DataStore data_store;
	private boolean backend_loaded;
	
	public SettingsPanel() {
		super();
		r_sqrd_list = new ListingSet<RSquaredListElement>(RSquaredListElement.class);
		setLayout(new BorderLayout());

		addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
            	add_new_element();
            }
            @Override
            public void mouseReleased(MouseEvent e) {  
            }
        });
	}
	
	@Override
	public void refresh() {
		r_sqrd_list.refresh();
	}
	
	public void add_new_element() {
		if (this.backend_loaded) {
			try {
				r_sqrd_list.display_new_element();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
			this.data_store.notify_update();
		}
	}

	@Override
	public void set_datastore(DataStore datastore) {
		this.data_store = datastore;
		this.backend_loaded = true;
		r_sqrd_list.set_datastore(datastore);
	}

	@Override
	public void add_refreshable(Refreshable refreshable_window) {
	}

	@Override
	public void on_start() {
		this.add(r_sqrd_list, BorderLayout.WEST);
		r_sqrd_list.on_start();
		r_sqrd_list.setVisible(true);
		add_new_element();
	}
}
