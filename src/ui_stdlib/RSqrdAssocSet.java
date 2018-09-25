package ui_stdlib;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.border.Border;
import system_utils.DataStore;
import ui_framework.Refreshable;
import ui_framework.SystemPanel;

@SuppressWarnings("serial")
public class RSqrdAssocSet extends SystemPanel {
	private JButton element;
	private JButton value;
	private boolean element_selected = false;
	
	public RSqrdAssocSet(system_utils.Element element2, String val) {
		super();
		this.setLayout(new GridLayout(2,0));
		Border border = BorderFactory.createLineBorder(SystemThemes.BACKGROUND);

		element = new JButton(element2.toString());
		value = new JButton(val);
		
		element.setBorder(BorderFactory.createCompoundBorder(border, 
	            BorderFactory.createEmptyBorder(4, 10, 4, 10)));
		element.setBackground(SystemThemes.HIGHLIGHT);

		value.setBorder(BorderFactory.createCompoundBorder(border, 
	            BorderFactory.createEmptyBorder(4, 10, 4, 10))); 
		
		element.addActionListener (new ActionListener () {
		    public void actionPerformed(ActionEvent e) {
		    	if (!element_selected) {	
					element.setOpaque(true);
					element_selected = !element_selected;
					//TODO: notify selected
		    	} else {
		    		element.setOpaque(false);
		    		element_selected = !element_selected;
		    		//TODO notify selected
		    	}
		    }
		});
	}
	public void try_toggle_value() {
		//TODO: toggle this value, untoggle others (DataStore)
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
		this.add(element);
		this.add(value);
		this.setVisible(true);
	}

}
