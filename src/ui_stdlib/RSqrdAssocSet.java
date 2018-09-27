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
import system_utils.Element;

@SuppressWarnings("serial")
public class RSqrdAssocSet extends SystemPanel {
	private JButton element;
	private JButton value;
	private boolean element_selected = false;
	private DataStore datastore;
	
	public RSqrdAssocSet(Element element1, Element element2, String val) {
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
					datastore.set_selected_rsqrd_assocs(element1, element2);
		    	} else {
		    		element.setOpaque(false);
		    		element_selected = !element_selected;
		    		datastore.remove_selected_rsqrd_assocs(element1, element2);
		    	}
		    }
		});
	}
	
	public void try_toggle_value() {
	}

	@Override
	public void refresh() {
	}
	
	public void set(system_utils.Element element2, String val) {
		element.setText(element2.toString());
		value.setText(val);
	}

	@Override
	public void set_datastore(DataStore datastore) {
		this.datastore = datastore;

	}

	@Override
	public void add_refreshable(Refreshable refreshable_component) {
	}

	@Override
	public void on_start() {
		this.add(element);
		this.add(value);
		this.setVisible(true);
	}
}
