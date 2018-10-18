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

@SuppressWarnings("serial")
public class CalcValSet extends SystemPanel {
	private ArrayList<Element> elements;
	private double wm;
	private double actual;
	private DataStore datastore;
	
	public CalcValSet(ArrayList<Element> elements, double wm, double actual) {
		super();
		this.setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
		this.elements = elements;
		this.wm = wm;
		this.actual = actual;
	}

	@Override
	public void refresh() {
		//add elements to panel
	}

	@Override
	public void set_datastore(DataStore datastore) {
		this.datastore = datastore;
	}

	@Override
	public void add_refreshable(Refreshable refreshable_component) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void on_start() {
		// TODO Auto-generated method stub
		SingleViewPanel temp;
		for (int i=0; i<elements.size();i++) {
			temp = new SingleViewPanel(elements.get(i).toString(), SystemThemes.MAIN, SystemThemes.BACKGROUND);
			temp.on_start();
			add(temp);
		}
		
		add(new JLabel());
		SingleViewPanel wm_panel = new SingleViewPanel(SystemThemes.get_display_number(wm), 
													   SystemThemes.MAIN,
													   SystemThemes.BACKGROUND);
		wm_panel.on_start();
		SingleViewPanel actual_panel = new SingleViewPanel(SystemThemes.get_display_number(actual), 
				   									 	   SystemThemes.MAIN,
				   									 	   SystemThemes.BACKGROUND);
		actual_panel.on_start();
		add(wm_panel);
		add(actual_panel);
		setVisible(true);
	}

}
