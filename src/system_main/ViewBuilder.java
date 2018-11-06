package system_main;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import system_drift_correction.DriftCorrectionGraph;
import system_utils.DataStore;
import ui_framework.ScheduledState;
import ui_framework.StateManager;
import ui_framework.StateResult;
import ui_framework.SystemWindow;
import ui_graphlib.CorrelationGraph;
import ui_graphlib.ModelGraph;
import ui_stdlib.dialogwindows.ErrorDialog;
import ui_stdlib.dialogwindows.ExportDialog;
import ui_stdlib.dialogwindows.NewDialog;
import ui_stdlib.dialogwindows.OpenDialog;
import ui_stdlib.dialogwindows.SaveDialog;
import ui_stdlib.views.CalculatedValuesPanel;
import ui_stdlib.views.R2SettingsPanel;

public class ViewBuilder {
	
	public static final String TEST_XRF = "/test_data/xrf.csv";
	public static final String TEST_MEANS = "/test_data/means.csv";
	public static final String TEST_STANDARDS = "/test_data/standards.csv";
	public static int OPEN_VIEWS = 0;
	
	private static SystemWindow get_app_view() {
    	SystemWindow main_window = new SystemWindow("Ablation Analysis", 
				ui_stdlib.SystemThemes.MAIN_WINDOW_WIDTH, 
				ui_stdlib.SystemThemes.MAIN_WINDOW_HEIGHT);

    	main_window.set_minimum_size(ui_stdlib.SystemThemes.MAIN_WINDOW_WIDTH, 
    								 ui_stdlib.SystemThemes.MAIN_WINDOW_HEIGHT);

    	main_window.add_system_panel(new R2SettingsPanel());	
    	main_window.add_system_panel(new CorrelationGraph());
    	main_window.add_system_panel(new CalculatedValuesPanel());
    	main_window.add_system_panel(new ModelGraph());
    	
    	return main_window;
	}
	
	private static SystemWindow get_drift_correction_view() {
		SystemWindow main_window = new SystemWindow("Drift Correction", 
				ui_stdlib.SystemThemes.MAIN_WINDOW_WIDTH, 
				ui_stdlib.SystemThemes.MAIN_WINDOW_HEIGHT);

    	main_window.set_minimum_size(ui_stdlib.SystemThemes.MAIN_WINDOW_WIDTH, 
    								 ui_stdlib.SystemThemes.MAIN_WINDOW_HEIGHT);
    	
    	main_window.add_system_panel(new DriftCorrectionGraph());	
    	main_window.add_system_panel(new DriftCorrectionGraph());
    	
    	return main_window;
	}

	
	private static ScheduledState create_new_window(SystemWindow window, StateManager manager) {
		
		ScheduledState main_app_view = window;
		
		window.setJMenuBar(get_menu_items(manager, main_app_view));
		OPEN_VIEWS++;
		
		return main_app_view;
	}
	
	public static ScheduledState create_new_default_window(StateManager manager) {
		return create_new_window(get_app_view(), manager);
	}
	
	private static JMenuBar get_menu_items(StateManager manager, ScheduledState main_app_view) {
		JMenuBar bar = new JMenuBar();
		
		//MENUS
		JMenu file = new JMenu("File");
		bar.add(file);
		
		//FUNCTION ITEMS
		JMenuItem save = new JMenuItem("Save");
		save.addActionListener(new ActionListener () {
		    public void actionPerformed(ActionEvent e) {
		    	//open dialog, set return state to main
		    	//save_dialog.on_scheduled(manager, main_app_view, datastore);
		    }
		});
		
		JMenuItem save_as = new JMenuItem("Save as...");
		save_as.addActionListener(new ActionListener () {
			public void actionPerformed(ActionEvent e) {
				//open dialog, set return state to main
		    	ScheduledState current_state = main_app_view;
		    	SystemWindow current_window = (SystemWindow) current_state;
		    	
		    	if (current_window.datastore_set()) {
		    		SaveDialog save_dialog = new SaveDialog("Save as");
		    		save_dialog.init();
		    		save_dialog.on_scheduled(manager, current_state, current_window.get_datastore());
		    	}
		    	else {
					ErrorDialog err = new ErrorDialog("Save Error", "Empty project: Cannot save an empty project. Please open an existing project or create a new project.");
					err.show_dialog();
		    	}
			}
		});
		
		JMenuItem open_new = new JMenuItem("New...");
		open_new.addActionListener(new ActionListener () {
		    public void actionPerformed(ActionEvent e) {
		    	//open dialog, set return state to main
		    	ScheduledState current_state = main_app_view;
		    	SystemWindow current_window = (SystemWindow) current_state;
		    	
		    	if (current_window.datastore_set()) {
		    		current_state = create_new_window(get_app_view(), manager);
		    		current_window = (SystemWindow) current_state;
		    	}
		    	
		    	NewDialog file_selector = new NewDialog("Select Files", current_window);
			    file_selector.init();
			    file_selector.on_scheduled(manager, current_state, null);
		    }
		});
		
		JMenuItem open_saved = new JMenuItem("Saved...");
		open_saved.addActionListener(new ActionListener () {
		    public void actionPerformed(ActionEvent e) {
		    	//open dialog, set return state to main
		    	ScheduledState current_state = main_app_view;
		    	SystemWindow current_window = (SystemWindow) current_state;
		    	
		    	if (current_window.datastore_set()) {
		    		current_state = create_new_window(get_app_view(), manager);
		    		current_window = (SystemWindow) current_state;
		    	}
		    	
		    	OpenDialog open_dialog = new OpenDialog("Open Files", current_window);
		    	open_dialog.init();
		    	open_dialog.on_scheduled(manager, current_window, null);
		    	
		    }
		});
		
		JMenuItem drift_correction = new JMenuItem("Drift Correction");
		drift_correction.addActionListener(new ActionListener () {
		    public void actionPerformed(ActionEvent e) {
		    	//open dialog, set return state to main
		    	ScheduledState drift_state = create_new_window(get_drift_correction_view(), manager);
		    	SystemWindow drift_window = (SystemWindow) drift_state;
		    	
		    	NewDialog file_selector = new NewDialog("Select Files", (SystemWindow) main_app_view);
			    file_selector.init();
			    file_selector.on_scheduled(manager, drift_state, null);
		    }
		});
		
		
		JMenuItem open_test_data = new JMenuItem("Example Data");
		open_test_data.addActionListener(new ActionListener () {
		    public void actionPerformed(ActionEvent e) {
		    	//open dialog, set return state to main
		    	ScheduledState current_state = main_app_view;
		    	SystemWindow current_window = (SystemWindow) current_state;
		    	
		    	if (current_window.datastore_set()) {
		    		current_state = create_new_window(get_app_view(), manager);
		    		current_window = (SystemWindow) current_state;
		    	}
		    	
		    	DataStore ds = new DataStore(current_window);
		    	try {
					ds.import_test_data(TEST_XRF, TEST_STANDARDS, TEST_MEANS);
				} catch (FileNotFoundException e1) {
					ErrorDialog err = new ErrorDialog("Import Error", "Import Error: Not able to import selected project.");
					err.show_dialog();
				}
		    	current_state.on_scheduled(manager, null, (StateResult) ds);
		    }
		});
		
		JMenuItem export_response_graphs = new JMenuItem("Response Graphs (PDF)");
		export_response_graphs.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//open dialog, set return state to main
		    	ScheduledState current_state = main_app_view;
		    	SystemWindow current_window = (SystemWindow) current_state;
		    	
		    	if (current_window.datastore_set()) {
		    		ExportDialog export_dialog = new ExportDialog("Exporting", "response");
		    		export_dialog.init();
		    		export_dialog.on_scheduled(manager, current_state, current_window.get_datastore());
		    	}
		    	else {
					ErrorDialog err = new ErrorDialog("Export Error", "Empty project: Cannot export an empty project. Please open an existing project or create a new project.");
					err.show_dialog();
		    	}
			}
		});
		
		JMenuItem export_calibration_graphs = new JMenuItem("Calibration Models & Graphs (PDF)");
		export_calibration_graphs.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		
		JMenuItem export_model_data = new JMenuItem("Model Data (CSV)");
		export_model_data.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//open dialog, set return state to main
		    	ScheduledState current_state = main_app_view;
		    	SystemWindow current_window = (SystemWindow) current_state;
		    	if (current_window.datastore_set()) {
		    		ExportDialog export_dialog = new ExportDialog("Export as", "model");
		    		export_dialog.init();
		    		export_dialog.on_scheduled(manager, current_state, current_window.get_datastore());
		    	}
		    	else {
					ErrorDialog err = new ErrorDialog("Export Error", "Empty project: Cannot export an empty project. Please open an existing project or create a new project.");
					err.show_dialog();
		    	}
			}
		});
		
		JMenu open_submenu = new JMenu("Open...");
		
		open_submenu.add(open_new);
		open_submenu.add(open_saved);
		open_submenu.add(open_test_data);

		JMenu export_submenu = new JMenu("Export...");
		
		export_submenu.add(export_response_graphs);
		export_submenu.add(export_calibration_graphs);
		export_submenu.add(export_model_data);
		
		file.add(open_submenu);
		file.addSeparator();
		file.add(drift_correction);
		file.addSeparator();
		file.add(export_submenu);
		file.addSeparator();
		file.add(save_as);
		file.add(save);
		
		JMenu edit = new JMenu("Edit");
		bar.add(edit);
		
		JMenu window = new JMenu("Window");
		bar.add(window);
		
		JMenuItem separate_subpanels = new JMenuItem("Split Windows");
		separate_subpanels.addActionListener(new ActionListener () {
		    public void actionPerformed(ActionEvent e) {
		    	SystemWindow temp = (SystemWindow) main_app_view;
		    	temp.split_panels();
		    }
		});
		
		JMenuItem regroup_subpanels = new JMenuItem("Regroup Windows");
		regroup_subpanels.addActionListener(new ActionListener () {
		    public void actionPerformed(ActionEvent e) {
		    	SystemWindow temp = (SystemWindow) main_app_view;
		    	temp.regroup_panels();
		    }
		});
		
		JMenuItem close_window = new JMenuItem("Close Window");
		close_window.addActionListener(new ActionListener () {
		    public void actionPerformed(ActionEvent e) {
		    	
		    	ScheduledState current_state = main_app_view;
		    	SystemWindow current_window = (SystemWindow) current_state;
		    	if (OPEN_VIEWS > 1) {
	    			OPEN_VIEWS--;
		    		current_window.setVisible(false);
		    		current_window.dispose();
	    		}
		    	//TODO: check if datastore set and state currently saved
		    	
//		    	if (current_window.datastore_set()) {
//		    		//TODO: warn if data unsaved
//		    		//if unsaved, open save dialog
//		    		//else close
//		    	} else {
//		    		if (OPEN_VIEWS > 1) {
//		    			OPEN_VIEWS--;
//			    		current_window.setVisible(false);
//			    		current_window.dispose();
//		    		}
//		    	}
		    }
		});
		
		window.add(separate_subpanels);
		window.add(regroup_subpanels);
		window.addSeparator();
		window.add(close_window);
		
		JMenu help = new JMenu("Help");
		bar.add(help);
		
		return bar;
	}
}
