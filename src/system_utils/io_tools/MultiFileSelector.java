package system_utils.io_tools;

import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.JFrame;
import ui_stdlib.components.StatefulFileSelector;

@SuppressWarnings("serial")
public class MultiFileSelector extends JFrame {
	private ArrayList<StatefulFileSelector> selector_list;
	
	public MultiFileSelector(String title, int total_selectors) {
		super(title);
		this.setLayout(new GridLayout(total_selectors,0));
	}
	public void add_selection_target(ActionListener on_selection) {
		StatefulFileSelector new_selector = new StatefulFileSelector();
		
	}
}
