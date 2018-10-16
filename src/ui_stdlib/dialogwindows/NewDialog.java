package ui_stdlib.dialogwindows;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.JButton;

import javax.swing.JComboBox;
import javax.swing.BorderFactory;

import system_utils.DataStore;
import system_utils.FileChooser;
import ui_framework.ScheduledState;
import ui_framework.StateManager;
import ui_framework.SystemWindow;
import ui_stdlib.SystemThemes;

@SuppressWarnings("serial")
public class NewDialog extends SystemDialog implements ui_framework.ScheduledState {
	private JButton continue_button;
	private FileChooser file_chooser;
	private boolean xrf_chosen = false;
	private boolean means_chosen = false;
	private boolean standards_chosen = false;
	private int path_display_length = 40;
	private SystemWindow main_window;
	private DataStore loaded_datastore;
	
	private ArrayList<JButton> added_buttons;
	
	public NewDialog(String title, SystemWindow main_window) {
		super(title);	
		
		this.main_window = main_window;
		
		this.setLayout(new GridLayout(4,2));
		
		this.setBackground(SystemThemes.BACKGROUND);
		
		added_buttons = new ArrayList<JButton>();
	}
	
	private void can_continue() {
		if (xrf_chosen && standards_chosen && means_chosen) {
			
			String[] means = file_chooser.get_means();
			String[] xrf = file_chooser.get_xrf();
			String[] standards = file_chooser.get_standards();
			
			loaded_datastore = new DataStore(main_window);
			try {
				loaded_datastore.import_data(xrf, standards, means);
				continue_button.setEnabled(true);
				continue_button.setBackground(SystemThemes.MAIN);
			} catch (Exception e) {
				continue_button.setEnabled(false);
				continue_button.setBackground(SystemThemes.HIGHLIGHT);
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
	public void on_scheduled(StateManager callback, ScheduledState previous, ui_framework.StateResult previous_res) {
		
		continue_button = new JButton("Continue");
		continue_button.setEnabled(false);
		continue_button.setBackground(SystemThemes.MAIN);
		continue_button.setOpaque(true);
		
		continue_button.addActionListener(new ActionListener () {
			public void actionPerformed(ActionEvent e) {
				remove(continue_button);
				callback.release_to(previous, loaded_datastore);
				close_dialog();
			}
        }); 
		add(continue_button);
		
		show_dialog();
	}
	@Override
	public void init() {
		
		file_chooser = new FileChooser(this);
		
		xrf_chosen = false;
		means_chosen = false;
		standards_chosen = false;
		
		JButton xrf_chooser = new JButton("XRF");
		//Causes windows graphical bug
			//xrf_chooser.setBackground(SystemThemes.BACKGROUND);
			//xrf_chooser.setOpaque(true);
		this.add(xrf_chooser);
		added_buttons.add(xrf_chooser);
		
		JComboBox xrf_table_selection = new JComboBox();
		xrf_table_selection.setBorder(BorderFactory.createTitledBorder("Select XRF Table"));
		this.add(xrf_table_selection);
		xrf_table_selection.setEnabled(false);
		xrf_table_selection.setBackground(SystemThemes.MAIN);
		xrf_table_selection.setOpaque(true);
		
		JButton means_chooser = new JButton("Means");
		//Causes windows graphical bug
			//means_chooser.setBackground(SystemThemes.BACKGROUND);
			//means_chooser.setOpaque(true);
		this.add(means_chooser);
		added_buttons.add(means_chooser);
		
		JComboBox means_table_selection = new JComboBox();
		means_table_selection.setBorder(BorderFactory.createTitledBorder("Select Means Table"));
		this.add(means_table_selection);
		means_table_selection.setEnabled(false);
		means_table_selection.setBackground(SystemThemes.MAIN);
		means_table_selection.setOpaque(true);
		
		JButton stds_chooser = new JButton("Standards");
		//Causes windows graphical bug
			//stds_chooser.setBackground(SystemThemes.BACKGROUND);
			//stds_chooser.setOpaque(true);
		this.add(stds_chooser);
		added_buttons.add(stds_chooser);
		
		JComboBox stds_table_selection = new JComboBox();
		stds_table_selection.setBorder(BorderFactory.createTitledBorder("Select Standards Table"));
		this.add(stds_table_selection);
		stds_table_selection.setEnabled(false);
		stds_table_selection.setBackground(SystemThemes.MAIN);
		stds_table_selection.setOpaque(true);
		
		xrf_chooser.addActionListener(new ActionListener () {
		    public void actionPerformed(ActionEvent e) {
		    	String file = file_chooser.import_files(file_chooser.xrf, "XRF_DATA_RUN_229");
		    	xrf_chooser.setText(
		    			get_file_display("XRF", file));
		    	if (!file.isEmpty()) {
		    		xrf_chosen = true;
		    	}
		    	can_continue();
		    }
		});
		
		means_chooser.addActionListener(new ActionListener () {
		    public void actionPerformed(ActionEvent e) {
		    	String file = file_chooser.import_files(file_chooser.means, "means");
		    	means_chooser.setText(
		    			get_file_display("Means", file));
		    	if (!file.isEmpty()) {
			    	means_chosen = true;
		    	}
		    	can_continue();
		    }
		});
		
		stds_chooser.addActionListener(new ActionListener () {
		    public void actionPerformed(ActionEvent e) {
		    	String file = file_chooser.import_files(file_chooser.standards, "standards");
		    	stds_chooser.setText(
		    			get_file_display("Standards", file));
		    	if (!file.isEmpty()) {
		    		standards_chosen = true;
		    	}
		    	can_continue();
		    }
		});
	}
}
