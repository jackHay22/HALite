package ui_stdlib;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.JComboBox;
import system_utils.DataStore;
import ui_framework.Refreshable;

@SuppressWarnings("serial")
public class RSquaredListElement extends ListingSetElement {
	private JComboBox<String> selection_dropdown;
	//private ArrayList<RSquaredValues> r_sqrd;
	private DataStore datastore;
	
	public RSquaredListElement() {
		super();
		String[] test_list = {"Al", "Mg", "Fe"};
		selection_dropdown = new JComboBox<String>(test_list);
		selection_dropdown.addActionListener (new ActionListener () {
		    public void actionPerformed(ActionEvent e) {
		        refresh();
		    }
		});
		selection_dropdown.setVisible(true);
	    this.add(selection_dropdown);
	}
	
	public void add_rsqrd_set() {
		String element = String.valueOf(selection_dropdown.getSelectedItem());
	}

	@Override
	public void refresh() {
		// TODO Auto-generated method stub
		System.out.println("refreshing list element");
	}

	@Override
	public void set_datastore(DataStore datastore) {
		this.datastore = datastore;
	}

	@Override
	public void add_refreshable(Refreshable refreshable_component) {
		// TODO Auto-generated method stub
		
	}

}
