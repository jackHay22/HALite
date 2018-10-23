package ui_stdlib.views;

import java.util.ArrayList;
import system_utils.DataStore;
import ui_framework.Refreshable;
import ui_stdlib.components.SingleViewBar;
import ui_stdlib.components.SingleViewPanel;

@SuppressWarnings("serial")
public class CalculatedContent extends SingleViewBar {

	public CalculatedContent() {
		super();
	}
	
	@Override
	public void refresh() {
	}
	
	public void set_panels(ArrayList<SingleViewPanel> panels) {
		clear_views();
		for (SingleViewPanel p : panels) {
			add_single_view(p);
		}
		show_views();
	}

	@Override
	public void set_datastore(DataStore datastore) {

	}

	@Override
	public void add_refreshable(Refreshable refreshable_component) {
	}

	@Override
	public void on_start() {
		setVisible(true);
	}

}
