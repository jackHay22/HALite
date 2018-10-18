package ui_stdlib.views;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import system_utils.DataStore;
import ui_framework.Refreshable;
import ui_stdlib.SystemThemes;
import ui_stdlib.components.SplitViewPanel;
import system_utils.Element;

public class RSqrdAssocSet implements Refreshable{
	private DataStore datastore;
	private SplitViewPanel graphical_view;
	
	public RSqrdAssocSet(Element element1, Element element2, Double val, boolean element_selected, boolean value_selected) {
		
		graphical_view = new SplitViewPanel(element2.toString(),
											SystemThemes.get_display_number(val),
											SystemThemes.HIGHLIGHT,
											SystemThemes.MAIN,
											SystemThemes.BACKGROUND);
		
		graphical_view.toggle_color_top(element_selected);
		graphical_view.toggle_color_bot(value_selected);

		graphical_view.get_top().addActionListener (new ActionListener () {
		    public void actionPerformed(ActionEvent e) {
		    	if (!element_selected) {	
					datastore.set_correlation_graph_elements(element1, element2);
		    	}
		    }
		});
		
		graphical_view.get_bottom().addActionListener (new ActionListener () {
		    public void actionPerformed(ActionEvent e) {
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
	
	public SplitViewPanel get_graphical_element() {
		return graphical_view;
	}

	@Override
	public void add_refreshable(Refreshable refreshable_component) {
	}

	@Override
	public void on_start() {
		graphical_view.on_start();
	}
}
