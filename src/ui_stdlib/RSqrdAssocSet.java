package ui_stdlib;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;

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
	private Element this_element;
	private Element this_element2;
	
	public RSqrdAssocSet(Element element1, Element element2, Double val) {
		super();
		this.this_element = element1;
		this.this_element2 = element2;
		this.setLayout(new GridLayout(2,0));
		Border border = BorderFactory.createLineBorder(SystemThemes.BACKGROUND);

		element = new JButton(this.this_element2.toString());
		value = new JButton(get_display_number(val));
		
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
					datastore.set_selected_rsqrd_assocs(this_element, this_element2);
		    	} else {
		    		element.setOpaque(false);
		    		element_selected = !element_selected;
		    		datastore.remove_selected_rsqrd_assocs(this_element, this_element2);
		    	}
		    }
		});
	}
	
	public void try_toggle_value() {
	}

	@Override
	public void refresh() {
	}
	
	private String get_display_number(Double val) {
		DecimalFormat df = new DecimalFormat("#.00");
		return df.format(val);
	}
	
	public void set(Element element1, Element element2, Double val) {
		this.this_element = element1;
		this.this_element2 = element2;
		element.setText(element2.toString());
		value.setText(get_display_number(val));
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
