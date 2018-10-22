package ui_stdlib.views;

import java.util.ArrayList;
import system_utils.DataStore;
import ui_framework.Refreshable;
import ui_stdlib.components.SingleViewBar;
import ui_stdlib.components.SingleViewPanel;

@SuppressWarnings("serial")
public class CalculatedHeader extends SingleViewBar {
	private ArrayList<SingleViewPanel> panels;

	public CalculatedHeader(ArrayList<SingleViewPanel> panels) {
		super();
		this.panels = panels;
	}
	
	@Override
	public void refresh() {
	}

	@Override
	public void set_datastore(DataStore datastore) {

	}

	@Override
	public void add_refreshable(Refreshable refreshable_component) {
	}

	@Override
	public void on_start() {
		for (SingleViewPanel p : panels) {
			add_single_view(p);
		}
		show_views();
	}

}
