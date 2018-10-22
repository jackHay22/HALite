package ui_stdlib.views;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.JComboBox;
import system_utils.DataStore;
import system_utils.Element;
import ui_framework.Refreshable;
import ui_stdlib.SystemThemes;
import ui_stdlib.components.PanelHeader;
import ui_stdlib.components.SingleViewPanel;

@SuppressWarnings("serial")
public class CalculatedValuesPanel extends ui_framework.SystemPanel {
	private DataStore datastore;
	private JComboBox<Element> selection_dropdown;
	private GridBagConstraints constraints;
	ArrayList<SingleViewPanel> header_panels;

	private CalculatedHeader header;
	private CalculatedValsScrollingSet set_list;
	private boolean backend_loaded = false;
	
	public CalculatedValuesPanel() {
		super();
		selection_dropdown = new JComboBox<Element>(Element.values());
		set_list = new CalculatedValsScrollingSet();
		header_panels = new ArrayList<SingleViewPanel>();
		header = new CalculatedHeader(header_panels);
		
		selection_dropdown.addActionListener (new ActionListener () {
		    public void actionPerformed(ActionEvent e) {
		        if (backend_loaded) {
		        	//element selection updated
		        	datastore.notify_update();
		        }
		    }
		});
	}
	
	@Override
	public void refresh() {
		set_list.refresh();
		remove(header);
		header_panels.clear();
		
		Color highlight = SystemThemes.HIGHLIGHT;
		Color main = SystemThemes.MAIN;
		Color bg = SystemThemes.BACKGROUND;
		
		
		header_panels.add(new SingleViewPanel("placeholder",main,bg));
		header_panels.add(new SingleViewPanel("placeholder",main,bg));
		header_panels.add(new SingleViewPanel("placeholder",main,bg));
		header_panels.add(new SingleViewPanel("wm",main,bg));
		header_panels.add(new SingleViewPanel("actual",main,bg));
		
		header = new CalculatedHeader(header_panels);
		
		//get stuff
		constraints.gridx = 1;
		constraints.gridy = 1;
		constraints.weightx = 1;
		constraints.weighty = 0;
		add(header, constraints);
		header.on_start();

	}

	@Override
	public void set_datastore(DataStore datastore) {
		this.datastore = datastore;
		set_list.set_datastore(datastore);
		this.backend_loaded = true;
	}

	@Override
	public void add_refreshable(Refreshable refreshable_component) {
	}

	@Override
	public void on_start() {
		setLayout(new GridBagLayout());
		constraints = SystemThemes.get_grid_constraints();
		
		constraints.anchor = GridBagConstraints.NORTH;
		constraints.ipady = SystemThemes.HEADER_PADDING;
		//constraints.weighty = 1;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.gridwidth = 2;
		PanelHeader header = new PanelHeader("Calculated Values: ", SystemThemes.MAIN);

		constraints.gridy = 0;
		add(header, constraints);
		header.on_start();
		constraints.gridwidth = 1;
		
		constraints.ipady = 0;
		constraints.gridy = 1;
		constraints.gridx = 0;
		constraints.weightx = 0;
		constraints.weighty = 1;
		add(selection_dropdown, constraints);

		
		constraints.gridwidth = 2;
		constraints.gridx = 0;
		constraints.gridy = 2;
		constraints.weighty = 1;
		add(set_list, constraints);
		
		selection_dropdown.addActionListener(new ActionListener () {
		    public void actionPerformed(ActionEvent e) {
		    	datastore.set_model_data_element((Element)selection_dropdown.getSelectedItem());
		    }
		});
		
		refresh();
		header.setVisible(true);
	}

}
