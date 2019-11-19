package ui_stdlib.components;

import java.awt.Dimension;

import javax.swing.JScrollPane;
import system_utils.DataStore;
import ui_framework.Refreshable;
import ui_stdlib.SystemThemes;

@SuppressWarnings("serial")
public class SideScrollPanel extends ui_framework.SystemPanel<DataStore> {
	
	public JScrollPane get_horiz_scrollable(int width, int height) {
		JScrollPane horiz_scrollable = new JScrollPane(this);
		
		//scroll as needed
		horiz_scrollable.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		horiz_scrollable.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
		
		horiz_scrollable.setPreferredSize(new Dimension(width, height));
		
		//graphical
		horiz_scrollable.setBackground(SystemThemes.MAIN);
		horiz_scrollable.setBorder(null);
		horiz_scrollable.setOpaque(false);
		
		return horiz_scrollable;
	}

	@Override
	public void refresh() {}

	@Override
	public void set_datastore(DataStore datastore) {}

	@Override
	public void add_refreshable(Refreshable<DataStore> refreshable_component) {}

	@Override
	public void on_start() {}

}
