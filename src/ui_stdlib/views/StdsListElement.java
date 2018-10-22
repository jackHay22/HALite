package ui_stdlib.views;

import system_utils.DataStore;
import ui_framework.Refreshable;
import ui_stdlib.SystemThemes;
import ui_stdlib.components.SingleViewBar;
import ui_stdlib.components.SingleViewPanel;

@SuppressWarnings("serial")
public class StdsListElement extends SingleViewBar {
	private SingleViewPanel weighted_mean;
	private SingleViewPanel actual;
	private DataStore datastore;
	private String standard;
	
	public StdsListElement(String standard) {
		super();
		
		this.standard = standard;
	}

	@Override
	public void refresh() {
		
	}

	@Override
	public void set_datastore(DataStore datastore) {
		this.datastore = datastore;
	}

	@Override
	public void add_refreshable(Refreshable refreshable_component) {
		
	}

	@Override
	public void on_start() {
		double wm_val = 0;
		double actual_val = 0;
		weighted_mean = new SingleViewPanel(SystemThemes.get_display_number(wm_val), 
											SystemThemes.HIGHLIGHT, SystemThemes.BACKGROUND);
		weighted_mean.on_start();
		actual = new SingleViewPanel(SystemThemes.get_display_number(actual_val), 
									 SystemThemes.HIGHLIGHT, SystemThemes.BACKGROUND);	
		actual.on_start();
		add_single_view(new SingleViewPanel("Ag", SystemThemes.MAIN, SystemThemes.BACKGROUND));
		
		show_views();
		add(weighted_mean);
		add(actual);
		
	}

}
