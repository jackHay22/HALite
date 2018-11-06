package system_drift_correction;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import system_utils.Element;
import ui_framework.Refreshable;
import ui_stdlib.SystemThemes;
import ui_stdlib.components.PanelHeader;

@SuppressWarnings("serial")
public class DriftCorrectionSettings<Backend extends DriftCorrectionDS> extends ui_framework.SystemPanel<Backend> implements Refreshable<Backend> {
	private GridBagConstraints constraints;
	private JComboBox<Element> element_selection;
	private JComboBox<Integer> degree_selection;
	private int max_degree = 10;
	private int current_elem_index;
	private int total_elems;
	private JButton next_element;
	private JButton prev_element;
	private JButton calc_fit;
	
	public DriftCorrectionSettings() {
		super();
		setLayout(new GridBagLayout());
		constraints = SystemThemes.get_grid_constraints();
		
		element_selection = new JComboBox<Element>(Element.values());
		
		degree_selection = new JComboBox<Integer>();
		current_elem_index = 0;
		total_elems = SystemThemes.TOTAL_ELEMENTS;
		
		next_element = new JButton("Next Element");
		prev_element = new JButton("Previous Element");
		calc_fit = new JButton("Calculate Fit");
		prev_element.setEnabled(false);
		
		element_selection.addActionListener(new ActionListener () {
		    public void actionPerformed(ActionEvent e) {
				current_elem_index = element_selection.getSelectedIndex();
				update_button_state();
		    }
		});
		
	}

	@Override
	public void refresh() {
	}
	
	private void get_next_element() {
		if (current_elem_index + 1 < total_elems) {
			current_elem_index++;
			element_selection.setSelectedIndex(current_elem_index);
		}
		update_button_state();
	}
	
	private void update_button_state() {
		if (current_elem_index == total_elems - 1) {
			next_element.setEnabled(false);
		} else {
			next_element.setEnabled(true);
		}
		if (current_elem_index == 0) {
			prev_element.setEnabled(false);
		} else {
			prev_element.setEnabled(true);
		}
	}
	
	private void get_prev_element() {
		if (current_elem_index - 1 >= 0) {
			current_elem_index--;
			element_selection.setSelectedIndex(current_elem_index);
		}
		
		update_button_state();
	}

	@Override
	public void set_datastore(Backend datastore) {
	}

	@Override
	public void add_refreshable(Refreshable<Backend> refreshable_component) {

	}

	@Override
	public void on_start() {
		constraints.gridwidth = 3;
		constraints.anchor = GridBagConstraints.NORTH;
		constraints.ipady = SystemThemes.HEADER_PADDING;
		
		PanelHeader<DriftCorrectionDS> panel_header = new PanelHeader<DriftCorrectionDS>("Drift Correction", SystemThemes.MAIN);
		panel_header.on_start();
		add(panel_header, constraints);
		
		constraints.gridx = 0;
		constraints.gridy = 1;
		constraints.gridwidth = 2;
		constraints.weightx = 1;
		constraints.weighty = 0;
		
		PanelHeader<DriftCorrectionDS> element_label = new PanelHeader<DriftCorrectionDS>("Current Element: ", SystemThemes.MAIN, SystemThemes.INSET);
		element_label.on_start();
		element_selection.setSelectedIndex(current_elem_index);
		element_label.add(element_selection);
		
		add(element_label, constraints);
		
		for (int i = 0; i < max_degree; i++) {
			degree_selection.addItem((Integer) i);
		}
		degree_selection.setSelectedIndex(6);
		
		constraints.gridx = 2;
		constraints.weightx = 0;
		constraints.gridy = 1;
		constraints.gridwidth = 1;
		
		constraints.weighty = 0;
		PanelHeader<DriftCorrectionDS> degree_label = new PanelHeader<DriftCorrectionDS>("Degree: ", SystemThemes.MAIN, SystemThemes.INSET);
		degree_label.on_start();
		degree_label.add(degree_selection);
		
		add(degree_label, constraints);
		
		constraints.gridy = 2;
		constraints.gridx = 0;
		constraints.weightx = 0.25;
		
		prev_element.addActionListener(new ActionListener () {
		    public void actionPerformed(ActionEvent e) {
				get_prev_element();
		    }
		});
		
		add(prev_element, constraints);
		
		
		constraints.weightx = 0.5;
		constraints.gridx = 1;
		
		add(calc_fit, constraints);
		
		constraints.gridx = 2;
		constraints.weightx = 0.25;
		
		next_element.addActionListener(new ActionListener () {
		    public void actionPerformed(ActionEvent e) {
				get_next_element();
		    }
		});
		
		add(next_element, constraints);
		
		setVisible(true);
	}
}
