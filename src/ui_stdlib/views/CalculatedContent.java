package ui_stdlib.views;

import java.util.ArrayList;
import system_utils.DataStore;
import ui_framework.Refreshable;
import ui_stdlib.SystemThemes;
import ui_stdlib.components.SingleViewBar;
import ui_stdlib.components.SingleViewPanel;

@SuppressWarnings("serial")
public class CalculatedContent extends SingleViewBar<DataStore> {

	public CalculatedContent() {
		super();
	}
	
	@Override
	public void refresh() {
	}
	
	public void set_panels(ArrayList<SingleViewPanel<DataStore>> panels) {
		//clear current panels
		clear_views();
		
		//add from list of panels
		for (SingleViewPanel<DataStore> p : panels) {
			p.setPreferredSize(SystemThemes.get_std_cell_dim());
			add_single_view(p);
		}
		
		//show updated panels
		show_views();
	}

	@Override
	public void set_datastore(DataStore datastore) {

	}

	@Override
	public void add_refreshable(Refreshable<DataStore> refreshable_component) {
	}

	@Override
	public void on_start() {
		setVisible(true);
	}

}
