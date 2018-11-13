package ui_stdlib.dialogwindows;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.BorderFactory;
import system_utils.DataStore;
import system_utils.DataTable;
import system_utils.io_tools.SystemFileDialog;
import ui_framework.SystemWindow;
import ui_stdlib.SystemThemes;

@SuppressWarnings("serial")
public class NewDialog extends SystemDialog implements ui_framework.ScheduledState<DataStore> {
	private JButton continue_button;
	private SystemFileDialog<DataStore> file_chooser;
	private boolean xrf_chosen = false;
	private boolean means_chosen = false;
	private boolean standards_chosen = false;
	private int path_display_length = 20;
	private SystemWindow<DataStore> main_window;
	
	private ArrayList<JButton> added_buttons;
	private JButton means_chooser;
	
	private String means_override;
	
	private GridBagConstraints constraints;
	
	public NewDialog(String title, SystemWindow<DataStore> main_window) {
		super(title);	
		
		this.main_window = main_window;
		
		this.setLayout(new GridBagLayout());
		constraints = SystemThemes.get_grid_constraints();
		
		this.setBackground(SystemThemes.BACKGROUND);
		
		added_buttons = new ArrayList<JButton>();
		
		file_chooser = new SystemFileDialog<DataStore>(this, "Open new project", "csv");
		
		means_override = null;
		
		means_chooser = new JButton("Means");
		
		means_chosen = false;
	}
	
	public NewDialog(String title, SystemWindow<DataStore> main_window, String pre_selected_means) {
		this(title, main_window);
		means_override = pre_selected_means + ".csv";
		means_chooser.setText(get_file_display("Means", means_override));
		means_chosen = true;
	}
	
	private void can_continue(DataStore backend) {
		//means set by dc export

		if (xrf_chosen && standards_chosen) {
			
			if (means_override != null) {
				means_chosen = backend.add_component_filepath(means_override, "means");	
			}
			
			if (means_chosen) {
				continue_button.setEnabled(true);
			}
		}
	}
	
	private String get_file_display(String label, String path) {
		if (path.length() == 0) {
			return label;
		} else if (path.length() <= path_display_length) {
			 return label + ": " + path;
		} else {
			 return label + ": ..." + path.substring(path.length() - path_display_length);
		}
	}

	

	@Override
	public void on_scheduled(DataStore backend) {
		continue_button = new JButton("Continue");
		continue_button.setEnabled(false);
		//continue_button.setBackground(SystemThemes.MAIN);
		continue_button.setOpaque(true);
		
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.fill = GridBagConstraints.BOTH;
		constraints.weighty = 0.2;
		
		continue_button.addActionListener(new ActionListener () {
			public void actionPerformed(ActionEvent e) {
				remove(continue_button);
				main_window.on_scheduled(backend);
				close_dialog();
			}
        }); 
		
		xrf_chosen = false;
		standards_chosen = false;
		
		
		JButton xrf_chooser = new JButton("XRF");
		//Causes windows graphical bug
			//xrf_chooser.setBackground(SystemThemes.BACKGROUND);
			//xrf_chooser.setOpaque(true);
		add(xrf_chooser, constraints);
		added_buttons.add(xrf_chooser);
		
		JComboBox<String> xrf_table_selection = new JComboBox<String>();
		xrf_table_selection.setBorder(BorderFactory.createTitledBorder("Select XRF Table"));
		
		constraints.gridx = 1;
		add(xrf_table_selection, constraints);
		xrf_table_selection.setEnabled(false);
		//xrf_table_selection.setBackground(SystemThemes.MAIN);
		xrf_table_selection.setOpaque(true);
		
		JButton stds_chooser = new JButton("Standards");
		//Causes windows graphical bug
			//stds_chooser.setBackground(SystemThemes.BACKGROUND);
			//stds_chooser.setOpaque(true);
		
		constraints.gridy = 1;
		constraints.gridx = 0;
		add(stds_chooser, constraints);
		added_buttons.add(stds_chooser);
		
		JComboBox<String> stds_table_selection = new JComboBox<String>();
		stds_table_selection.setBorder(BorderFactory.createTitledBorder("Select Standards Table"));
		
		constraints.gridx = 1;
		add(stds_table_selection, constraints);
		stds_table_selection.setEnabled(false);
		//stds_table_selection.setBackground(SystemThemes.MAIN);
		stds_table_selection.setOpaque(true);
		
		
		//Causes windows graphical bug
			//means_chooser.setBackground(SystemThemes.BACKGROUND);
			//means_chooser.setOpaque(true);
		
		
		added_buttons.add(means_chooser);
		
		JComboBox<String> means_table_selection = new JComboBox<String>();
		means_table_selection.setBorder(BorderFactory.createTitledBorder("Select Means Table"));
		means_table_selection.setEnabled(false);
		//means_table_selection.setBackground(SystemThemes.MAIN);
		means_table_selection.setOpaque(true);
		
		if (means_override == null) {
			constraints.gridx = 0;
			constraints.gridy = 2;
			add(means_chooser, constraints);
			
			constraints.gridx = 1;
			add(means_table_selection, constraints);
		}	
		
		xrf_chooser.addActionListener(new ActionListener () {
		    public void actionPerformed(ActionEvent e) {
		    	
		    	// Load xrf file and check result
		    	xrf_chosen = file_chooser.add_component_path(backend, "xrf");
		    	String file = backend.xrf_path;
		    	
		    	// Check if xrf file has been imported successfully
				if (xrf_chosen && SystemThemes.valid_csv(file)) {
		    		
			    	xrf_chooser.setText(get_file_display("XRF", file));
		    		
		    		// Clear all previous elements from dropdown
		    		xrf_table_selection.removeAllItems();
		    		
		    		ArrayList<DataTable> all_tables = backend.all_tables("xrf");
		    		
		    		for (DataTable table : all_tables) {
		    			String table_name = table.name();
		    			xrf_table_selection.addItem(table_name);
		    		}
		    		
		    		xrf_table_selection.setEnabled(true);
		    		xrf_table_selection.setBackground(SystemThemes.MAIN);
		    		xrf_table_selection.setOpaque(false);
			    	
		    	}
		    	
		    	can_continue(backend);
		    }
		});
	
		
		means_chooser.addActionListener(new ActionListener () {
		    public void actionPerformed(ActionEvent e) {
		    	
		    	means_chosen = file_chooser.add_component_path(backend, "means");
		    	String file = backend.means_path;
		    	
		    	// Check if means file has been imported successfully
		    	if (means_chosen && SystemThemes.valid_csv(file)) {
		    		
			    	means_chooser.setText(get_file_display("Means", file));
			    	
			    	// Clear all previous elements from dropdown
			    	means_table_selection.removeAllItems();
			    	
			    	ArrayList<DataTable> all_tables = backend.all_tables("means");
					
		    		for (DataTable table : all_tables) {
		    			String table_name = table.name();
		    			means_table_selection.addItem(table_name);
		    		}
			    	
		    		means_table_selection.setEnabled(true);
		    		means_table_selection.setBackground(SystemThemes.MAIN);
		    		means_table_selection.setOpaque(false);
			    		
		    	}
		    	
		    	can_continue(backend);
		    }
		});
		
		stds_chooser.addActionListener(new ActionListener () {
		    public void actionPerformed(ActionEvent e) {
		    	
		    	standards_chosen = file_chooser.add_component_path(backend, "standards");
		    	String file = backend.standards_path;
		    	
		    	// Check if standards file has been imported successfully
		    	if (standards_chosen && SystemThemes.valid_csv(file)) {
		    		
			    	stds_chooser.setText(get_file_display("Standards", file));
		    		
		    		// Clear all previous elements from dropdown
		    		stds_table_selection.removeAllItems();
		    		
		    		ArrayList<DataTable> all_tables = backend.all_tables("standards");
					
		    		for (DataTable table : all_tables) {
		    			String table_name = table.name();
		    			stds_table_selection.addItem(table_name);
		    		}
		    		
		    		stds_table_selection.setEnabled(true);
		    		stds_table_selection.setBackground(SystemThemes.MAIN);
		    		stds_table_selection.setOpaque(false);
		    	}
		    	
		    	can_continue(backend);
		    }
		});
		
		constraints.gridwidth = 2;
		constraints.gridy = 3;
		constraints.gridx = 0;
		constraints.weighty = 0.4;
		
		add(continue_button, constraints);
		
		show_dialog();
	}
	
}
