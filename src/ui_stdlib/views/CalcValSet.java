package ui_stdlib.views;

import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JLabel;

import system_utils.DataStore;
import system_utils.Element;
import ui_framework.Refreshable;
import ui_framework.SystemPanel;
import ui_stdlib.SystemThemes;
import ui_stdlib.components.SingleViewPanel;
import ui_stdlib.components.SplitViewPanel;

@SuppressWarnings("serial")
public class CalcValSet extends SystemPanel {
	private ArrayList<Element> elements;
	
	public CalcValSet(ArrayList<Element> elements, double wm, double actual) {
		super();
		this.setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
		this.elements = elements;
	}

	@Override
	public void refresh() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void set_datastore(DataStore datastore) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void add_refreshable(Refreshable refreshable_component) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void on_start() {
		// TODO Auto-generated method stub
		setVisible(true);
		SingleViewPanel temp;
		for (int i=0; i<elements.size();i++) {
			temp = new SingleViewPanel(elements.get(i).toString(), SystemThemes.MAIN, SystemThemes.BACKGROUND);
			temp.on_start();
			add(temp);
		}
	}

}
