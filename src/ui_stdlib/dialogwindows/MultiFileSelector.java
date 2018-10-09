package ui_stdlib.dialogwindows;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.JButton;
import system_utils.DataStore;
import system_utils.FileChooser;
import ui_framework.SetupCoordinator;
import ui_framework.SystemWindow;
import ui_stdlib.SystemThemes;

@SuppressWarnings("serial")
public class MultiFileSelector extends SystemDialog implements ui_framework.ScheduledState {
	private JButton continue_button;
	private FileChooser file_chooser;
	private boolean xrf_chosen = false;
	private boolean means_chosen = false;
	private boolean standards_chosen = false;
	private int path_display_length = 40;
	private SystemWindow main_window;
	private DataStore loaded_datastore;
	
	private ArrayList<JButton> added_buttons;
	
	public MultiFileSelector(String title, SystemWindow main_window) {
		super(title);	
		
		this.main_window = main_window;
		
		this.setLayout(new GridLayout(4,0));
		
		continue_button = new JButton("Continue");
		continue_button.setEnabled(false);
		
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
	public void on_scheduled(SetupCoordinator callback, ui_framework.StateResult previous) {
		file_chooser = new FileChooser(this);
		continue_button.setEnabled(false);
		
		xrf_chosen = false;
		means_chosen = false;
		standards_chosen = false;
		
		JButton xrf_chooser = new JButton("XRF");
		xrf_chooser.setBackground(SystemThemes.BACKGROUND);
		xrf_chooser.setOpaque(true);
		this.add(xrf_chooser);
		added_buttons.add(xrf_chooser);
		
		JButton means_chooser = new JButton("Means");
		means_chooser.setBackground(SystemThemes.BACKGROUND);
		means_chooser.setOpaque(true);
		this.add(means_chooser);
		added_buttons.add(means_chooser);
		
		JButton stds_chooser = new JButton("Standards");
		stds_chooser.setBackground(SystemThemes.BACKGROUND);
		stds_chooser.setOpaque(true);
		this.add(stds_chooser);
		added_buttons.add(stds_chooser);
		
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
		
		continue_button.addActionListener(new ActionListener () {
			public void actionPerformed(ActionEvent e) {
				callback.release(loaded_datastore);
				close_dialog();
			}
        }); 
		
		this.add(continue_button);
		continue_button.setBackground(SystemThemes.MAIN);
		continue_button.setOpaque(true);
		
		show_dialog();
	}
	
	private void graphical_purge() {
		for (JButton button: added_buttons) {
			this.remove(button);
		}
	}

	@Override
	public void on_rollback(SetupCoordinator callback) {
		graphical_purge();
		on_scheduled(callback, null);
	}
}
