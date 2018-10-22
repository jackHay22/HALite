package ui_stdlib.views;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.ArrayList;
import system_utils.DataStore;
import ui_framework.Refreshable;
import ui_stdlib.SystemThemes;
import ui_stdlib.components.SingleViewBar;
import ui_stdlib.components.SingleViewPanel;

@SuppressWarnings("serial")
public class CalculatedHeader extends SingleViewBar {
	private ArrayList<SingleViewPanel> panels;
	private GridBagConstraints constraints;
	private SingleViewPanel wm;
	private SingleViewPanel actual;

	public CalculatedHeader(SingleViewPanel wm, SingleViewPanel actual) {
		super();
		setLayout(new GridBagLayout());
		panels = new ArrayList<SingleViewPanel>();
		this.wm = wm;
		this.actual = actual;
		add(wm);
		add(actual);
	}
	
	@Override
	public void refresh() {
	}
	
	public void set_panels(ArrayList<SingleViewPanel> panels) {
		clear_views();
		remove(wm);
		remove(actual);
		for (SingleViewPanel p : panels) {
			add_single_view(p);
		}
		show_views();
		add(wm, constraints);
		add(actual, constraints);
		
	}

	@Override
	public void set_datastore(DataStore datastore) {

	}

	@Override
	public void add_refreshable(Refreshable refreshable_component) {
	}

	@Override
	public void on_start() {
		constraints = SystemThemes.get_grid_constraints();
		for (SingleViewPanel p : panels) {
			add_single_view(p);
		}
		show_views();
	}

}
