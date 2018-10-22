package ui_stdlib.views;

import java.util.ArrayList;
import javax.swing.BoxLayout;
import system_utils.DataStore;
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
	}
	
	private void add_view(StdsListElement view) {
		//TODO call this in on_start()
		views.add(view);
		view.on_start();
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
		
		//TODO: placeholder
		add_view(new StdsListElement("Test"));
		add_view(new StdsListElement("Test"));
		add_view(new StdsListElement("Test"));
		add_view(new StdsListElement("Test"));
		add_view(new StdsListElement("Test"));
		add_view(new StdsListElement("Test"));
		add_view(new StdsListElement("Test"));
		add_view(new StdsListElement("Test"));
		add_view(new StdsListElement("Test"));
		add_view(new StdsListElement("Test"));
		add_view(new StdsListElement("Test"));
		setVisible(true);
		if (backend_loaded) {
			//pull from datastore and add
		}
	}

}
