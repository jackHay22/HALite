package ui_stdlib;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import system_utils.DataStore;
import ui_framework.Refreshable;
import system_utils.Element;

@SuppressWarnings("serial")
public class RSquaredListElement extends ui_framework.SystemPanel {
	private JComboBox<Element> selection_dropdown;
	//private ArrayList<RSquaredValues> r_sqrd;
	private DataStore datastore;
	private boolean backend_loaded = false;
	private int rsqrd_total = 5;
	
	public RSquaredListElement() {
		super();
		this.setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
		selection_dropdown = new JComboBox<Element>(Element.values());
		selection_dropdown.addActionListener (new ActionListener () {
		    public void actionPerformed(ActionEvent e) {
		        if (backend_loaded) {
		        	datastore.notify_update();
		        }
		    }
		});
	}
	
	public void add_rsqrd_set(int size) {
		Element element = (Element) selection_dropdown.getSelectedItem();
		RSqrdAssocSet sqrd;
		for (int i=0; i < size; i++) {
			sqrd = new RSqrdAssocSet(element, "test");
			this.add(sqrd);
			sqrd.on_start();
		}
	}

	@Override
	public void refresh() {
		//TODO: refresh rsqrd set
	}

	@Override
	public void set_datastore(DataStore datastore) {
		this.datastore = datastore;
		this.backend_loaded = true;
	}

	@Override
	public void add_refreshable(Refreshable refreshable_component) {
	}
	
	public void set_rsqrd_total(int total) {
		this.rsqrd_total = total;
	}

	@Override
	public void on_start() {
		this.add(selection_dropdown);
		add_rsqrd_set(this.rsqrd_total);
		this.setVisible(true);
		selection_dropdown.setVisible(true);
	}

}
