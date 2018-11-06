package ui_stdlib.views;

import java.util.ArrayList;
import javax.swing.BoxLayout;
import system_utils.DataStore;
import system_utils.Element;
import ui_framework.Refreshable;

@SuppressWarnings("serial")
public class CalculatedValsScrollingSet extends ui_framework.SystemPanel<DataStore> {
	private DataStore datastore;
	private ArrayList<StdsListElement> views;
	private boolean backend_loaded;
	
	public CalculatedValsScrollingSet() {
		super();
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		views = new ArrayList<StdsListElement>();
		backend_loaded = false;
	}

	@Override
	public void refresh() {
		if (backend_loaded) {
			clear_views();
			
			ArrayList<String> standards = datastore.get_STDlist();
			
			
			ArrayList<String> header_labels = datastore.get_WM_headers();
			ArrayList<Element> elems = datastore.get_WM_elems();
			
			for (String std : standards) {
				StdsListElement graphical_elem = new StdsListElement(std, elems, header_labels);
				views.add(graphical_elem);
				
				graphical_elem.set_datastore(datastore);
				graphical_elem.on_start();
				
				add(graphical_elem);
				graphical_elem.refresh();
				graphical_elem.setVisible(true);
			}
			
			ArrayList<String> unknowns = datastore.get_unknown_list();
			
			for (String un : unknowns) {
				StdsListElement graphical_elem = new StdsListElement(un, elems, header_labels);
				views.add(graphical_elem);
				
				graphical_elem.set_datastore(datastore);
				graphical_elem.on_start();
				
				add(graphical_elem);
				graphical_elem.refresh();
				graphical_elem.setVisible(true);
			}

			revalidate();
		}
	}

	private void clear_views() {
		for (StdsListElement v : views) {
			remove(v);
		}
		views.clear();
	}

	@Override
	public void set_datastore(DataStore datastore) {
		this.datastore = datastore;
		backend_loaded = true;
	}

	@Override
	public void add_refreshable(Refreshable<DataStore> refreshable_component) {

	}

	@Override
	public void on_start() {
		setVisible(true);
	}
}
