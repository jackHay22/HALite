package ui_stdlib.views;

import java.util.ArrayList;
import javax.swing.BoxLayout;
import system_utils.DataStore;
import system_utils.Element;
import system_utils.ElementCorrelationInfo;
import ui_framework.Refreshable;

@SuppressWarnings("serial")
public class CalculatedValsScrollingSet extends ui_framework.SystemPanel {
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
			
			ElementCorrelationInfo element_corr = datastore.get_model_data_corr();
			Element elem = element_corr.get_element();
			
			ArrayList<String> standards = datastore.get_STDlist();
			
			for (String std : standards) {
				StdsListElement graphical_elem = new StdsListElement(std, elem);
				views.add(graphical_elem);
				
				graphical_elem.set_datastore(datastore);
				graphical_elem.on_start();
				
				add(graphical_elem);
				graphical_elem.setVisible(true);
				graphical_elem.refresh();
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
	public void add_refreshable(Refreshable refreshable_component) {

	}

	@Override
	public void on_start() {
		setVisible(true);
	}
}
