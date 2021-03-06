package ui_stdlib.views;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import system_utils.DataStore;
import ui_framework.Refreshable;
import ui_stdlib.SystemThemes;
import ui_stdlib.components.SplitViewPanel;
import system_utils.Element;

public class RSqrdAssocSet implements Refreshable<DataStore>{
	private DataStore datastore;
	private SplitViewPanel<DataStore> graphical_view;
	
	public RSqrdAssocSet(Element element1, Element element2, Double val, boolean element_selected, boolean value_selected) {
		
		//create a split view panel to store two buttons
		graphical_view = new SplitViewPanel<DataStore>(element2.toString(),
											SystemThemes.get_display_number(val),
											SystemThemes.HIGHLIGHT,
											SystemThemes.MAIN,
											SystemThemes.BACKGROUND);
		
		//add color to show when toggled on
		graphical_view.toggle_color_top(element_selected);
		graphical_view.toggle_color_bot(value_selected);

		//add action listener to top panel to notify backend
		graphical_view.get_top().addActionListener (new ActionListener () {
		    public void actionPerformed(ActionEvent e) {
		    	//check if selected
		    	if (!element_selected) {	
					datastore.set_correlation_graph_elements(element1, element2);
		    	}
		    }
		});
		
		//add an action listener to the bottom
		graphical_view.get_bottom().addActionListener (new ActionListener () {
		    public void actionPerformed(ActionEvent e) {
		    	//check if selected
		    	if (!value_selected) {	
					datastore.set_selected_rsqrd_assocs(element1, element2);
		    	} else {
		    		datastore.remove_selected_rsqrd_assocs(element1, element2);
		    	}
		    }
		});
	}

	@Override
	public void refresh() {
	}

	@Override
	public void set_datastore(DataStore datastore) {
		this.datastore = datastore;

	}
	
	public SplitViewPanel<DataStore> get_graphical_element() {
		//return the split view panel 
		return graphical_view;
	}

	@Override
	public void add_refreshable(Refreshable<DataStore> refreshable_component) {
	}

	@Override
	public void on_start() {
		graphical_view.on_start();
	}
}
