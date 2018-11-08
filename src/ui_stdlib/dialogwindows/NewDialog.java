package ui_stdlib.dialogwindows;

import java.awt.GridLayout;
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
	private int path_display_length = 40;
	private SystemWindow<DataStore> main_window;
	
	private ArrayList<JButton> added_buttons;
	
	public NewDialog(String title, SystemWindow<DataStore> main_window) {
		super(title);	
		
		this.main_window = main_window;
		
		this.setLayout(new GridLayout(4,2));
		
		this.setBackground(SystemThemes.BACKGROUND);
		
		added_buttons = new ArrayList<JButton>();
	}
	
	private void can_continue() {
		if (xrf_chosen && standards_chosen && means_chosen) {
			continue_button.setEnabled(true);
			continue_button.setBackground(SystemThemes.MAIN);
			
			//String xrf_selected = xrf_table_selection.getSelectedItem().toString();
			
			//loaded_datastore.set_tables_in_use(xrf_selected, standards_selected, means_selected);
			
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
		continue_button.setBackground(SystemThemes.MAIN);
		continue_button.setOpaque(true);
		
		continue_button.addActionListener(new ActionListener () {
			public void actionPerformed(ActionEvent e) {
				remove(continue_button);
				main_window.on_scheduled(backend);
				close_dialog();
			}
        }); 
		
		xrf_chosen = false;
		means_chosen = false;
		standards_chosen = false;
		
		file_chooser = new SystemFileDialog<DataStore>(this, "Open new project");
		
		JButton xrf_chooser = new JButton("XRF");
		//Causes windows graphical bug
			//xrf_chooser.setBackground(SystemThemes.BACKGROUND);
			//xrf_chooser.setOpaque(true);
		this.add(xrf_chooser);
		added_buttons.add(xrf_chooser);
		
		JComboBox<String> xrf_table_selection = new JComboBox<String>();
		xrf_table_selection.setBorder(BorderFactory.createTitledBorder("Select XRF Table"));
		this.add(xrf_table_selection);
		xrf_table_selection.setEnabled(false);
		xrf_table_selection.setBackground(SystemThemes.MAIN);
		xrf_table_selection.setOpaque(true);
		
		JButton stds_chooser = new JButton("Standards");
		//Causes windows graphical bug
			//stds_chooser.setBackground(SystemThemes.BACKGROUND);
			//stds_chooser.setOpaque(true);
		this.add(stds_chooser);
		added_buttons.add(stds_chooser);
		
		JComboBox<String> stds_table_selection = new JComboBox<String>();
		stds_table_selection.setBorder(BorderFactory.createTitledBorder("Select Standards Table"));
		this.add(stds_table_selection);
		stds_table_selection.setEnabled(false);
		stds_table_selection.setBackground(SystemThemes.MAIN);
		stds_table_selection.setOpaque(true);
		
		JButton means_chooser = new JButton("Means");
		//Causes windows graphical bug
			//means_chooser.setBackground(SystemThemes.BACKGROUND);
			//means_chooser.setOpaque(true);
		this.add(means_chooser);
		added_buttons.add(means_chooser);
		
		JComboBox<String> means_table_selection = new JComboBox<String>();
		means_table_selection.setBorder(BorderFactory.createTitledBorder("Select Means Table"));
		this.add(means_table_selection);
		means_table_selection.setEnabled(false);
		means_table_selection.setBackground(SystemThemes.MAIN);
		means_table_selection.setOpaque(true);
		
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
		    	
		    	can_continue();
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
		    	
		    	can_continue();
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
		    	
		    	can_continue();
		    }
		});
		add(continue_button);
		
		show_dialog();
	}
	
}
