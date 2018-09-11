package ui_graphlib;

import java.awt.Graphics;

import system_utils.DataStore;
import ui_framework.Refreshable;
import ui_framework.SystemPanel;

public class DrawablePanel extends SystemPanel {
	private DataStore data_store;
	
	public DrawablePanel() {
		super();
	}
	
	@Override
	public void refresh() {
		// TODO Auto-generated method stub

	}

	@Override
	public void set_datastore(DataStore datastore) {
		this.data_store = datastore;

	}

	@Override
	public void add_refreshable(Refreshable refreshable_component) {
		// TODO Auto-generated method stub

	}
	
	@Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.drawString("Graph Label", 20, 20);
        g.drawRect(200, 200, 200, 200);
    }

}
