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
	private boolean backend_loaded = false;
	
	public CalculatedValsScrollingSet() {
		super();
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		views = new ArrayList<StdsListElement>();
	}

	@Override
	public void refresh() {
		if (backend_loaded) {
			clear_views();
			ElementCorrelationInfo element_corr = datastore.get_model_data_corr();
			Element elem = element_corr.get_element();
			//pull from datastore and add
			//TODO: temp layout
			ArrayList<String> standards = datastore.get_STDlist();
			for (String std : standards) {
				add_view(new StdsListElement(std, elem));
			}	
			
			revalidate();
		}
	}
	
	private void add_view(StdsListElement view) {
		views.add(view);
		view.on_start();
		view.set_datastore(datastore);
		view.refresh();
		add(view);
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
