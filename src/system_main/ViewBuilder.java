package system_main;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import system_drift_correction.DriftCorrectionDS;
import system_drift_correction.DriftCorrectionGraph;
import system_drift_correction.DriftCorrectionSettings;
import system_utils.DataStore;
import system_utils.io_tools.SystemFileDialog;
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


	private static void open_example_data(SystemWindow<DataStore> current_window) {
    	//open dialog, set return state to main
		
		//create a new window if the current window already has datastore set
    	if (current_window.datastore_set()) {
    		current_window = get_app_view();
    	}

    	//import example data to new datastore
    	DataStore ds = new DataStore(current_window);
    	ds.import_test_data(TEST_XRF, TEST_STANDARDS, TEST_MEANS);

    	//schedule window on datastore
    	current_window.on_scheduled(ds);
	}

	private static SystemWindow<DataStore> get_app_view() {
    	SystemWindow<DataStore> main_window = new SystemWindow<DataStore>("Ablation Analysis",
														ui_stdlib.SystemThemes.MAIN_WINDOW_WIDTH,
														ui_stdlib.SystemThemes.MAIN_WINDOW_HEIGHT);

    	main_window.set_minimum_size(ui_stdlib.SystemThemes.MAIN_WINDOW_WIDTH,
    								 ui_stdlib.SystemThemes.MAIN_WINDOW_HEIGHT);

    	main_window.add_system_panel(new R2SettingsPanel());
    	main_window.add_system_panel(new CorrelationGraph());
    	main_window.add_system_panel(new CalculatedValuesPanel());
    	main_window.add_system_panel(new ModelGraph());

    	//add menu items
    	main_window.setJMenuBar(get_main_menu_items(main_window));

		//update window counter
		OPEN_VIEWS++;
    	return main_window;
	}

	private static SystemWindow<DriftCorrectionDS> get_drift_correction_view() {
		SystemWindow<DriftCorrectionDS> main_window = new SystemWindow<DriftCorrectionDS>("Drift Correction",
															ui_stdlib.SystemThemes.MAIN_WINDOW_WIDTH,
															ui_stdlib.SystemThemes.MAIN_WINDOW_HEIGHT);

    	main_window.set_minimum_size(ui_stdlib.SystemThemes.MAIN_WINDOW_WIDTH,
    								 ui_stdlib.SystemThemes.MAIN_WINDOW_HEIGHT);

    	main_window.add_system_panel(new DriftCorrectionSettings());
    	main_window.add_system_panel(new DriftCorrectionGraph());

    	//add menu items
		main_window.setJMenuBar(get_dc_menu_items(main_window));

		//update window counter
		OPEN_VIEWS++;
    	return main_window;
	}

	public static SystemWindow<DataStore> create_new_default_window() {
		SystemWindow<DataStore> new_window = get_app_view();
		return new_window;
	}


	//MENU ITEMS FOR DRIFT CORRECTION SYSTEM VIEW
	private static JMenuBar get_dc_menu_items(SystemWindow<DriftCorrectionDS> window) {
		
		JMenuItem open_new = new JMenuItem("New Analysis...");
		open_new.setAccelerator(SystemKeybindings.NEW);
		open_new.addActionListener(new ActionListener () {
			public void actionPerformed(ActionEvent e) {

	    		SystemWindow<DataStore> new_window = get_app_view();
	    		new_window.on_start();
	    		
	    		NewDialog file_selector = new NewDialog("Select Files", new_window);
	    		
	    		DataStore new_ds = new DataStore(new_window);
	    		file_selector.on_scheduled(new_ds);
		    }
		});
		
		JMenuItem open_new_dc = new JMenuItem("New Drift Correction...");
		//open_new.setAccelerator(SystemKeybindings.NEW);
		open_new_dc.addActionListener(new ActionListener () {
			public void actionPerformed(ActionEvent e) {

				SystemWindow<DriftCorrectionDS> drift_window = get_drift_correction_view();
				DriftCorrectionDS dc_backend = new DriftCorrectionDS(drift_window);
				SystemFileDialog<DriftCorrectionDS> open_dialog = new SystemFileDialog<DriftCorrectionDS>(drift_window, "Drift Correction");

				if (open_dialog.init_backend_on_path(dc_backend)) {
					drift_window.on_scheduled(dc_backend);
				} else {
					new ErrorDialog<DriftCorrectionDS>("Error (Error msg placeholder)", "Bad Drift Correction File").show_dialog();
				}
		    }
		});
		
		JMenuItem export = new JMenuItem("Export");
		//open_new.setAccelerator(SystemKeybindings.NEW);
		export.addActionListener(new ActionListener () {
			public void actionPerformed(ActionEvent e) {

				DriftCorrectionDS ds = window.get_datastore();
				
				if (ds.on_export()) {
					
				}
				
				//TODO: write to file
//				SystemWindow<DriftCorrectionDS> drift_window = get_drift_correction_view();
//				DriftCorrectionDS dc_backend = new DriftCorrectionDS(drift_window);
//				SystemFileDialog<DriftCorrectionDS> open_dialog = new SystemFileDialog<DriftCorrectionDS>(drift_window, "Drift Correction");
//
//				if (open_dialog.init_backend_on_path(dc_backend)) {
//					drift_window.on_scheduled(dc_backend);
//				} else {
//					new ErrorDialog<DriftCorrectionDS>("Error (Error msg placeholder)", "Bad Drift Correction File").show_dialog();
//				}
		    }
		});
		
		JMenuItem export_analysis = new JMenuItem("Proceed to analysis...");
		//open_new.setAccelerator(SystemKeybindings.NEW);
		export_analysis.addActionListener(new ActionListener () {
			public void actionPerformed(ActionEvent e) {
				
				SystemWindow<DataStore> new_analysis_window = get_app_view();
//				SystemWindow<DriftCorrectionDS> drift_window = get_drift_correction_view();
//				DriftCorrectionDS dc_backend = new DriftCorrectionDS(drift_window);
//				SystemFileDialog<DriftCorrectionDS> open_dialog = new SystemFileDialog<DriftCorrectionDS>(drift_window, "Drift Correction");
//
//				if (open_dialog.init_backend_on_path(dc_backend)) {
//					drift_window.on_scheduled(dc_backend);
//				} else {
//					new ErrorDialog<DriftCorrectionDS>("Error (Error msg placeholder)", "Bad Drift Correction File").show_dialog();
//				}
		    }
		});
		

		JMenuItem separate_subpanels = new JMenuItem("Split Windows");
		separate_subpanels.addActionListener(new ActionListener () {
		    public void actionPerformed(ActionEvent e) {
				SystemWindow<DriftCorrectionDS> temp = window;
		    	temp.split_panels();
		    }
		});

		JMenuItem regroup_subpanels = new JMenuItem("Regroup Windows");
		regroup_subpanels.addActionListener(new ActionListener () {
		    public void actionPerformed(ActionEvent e) {
		    	SystemWindow<DriftCorrectionDS> temp = window;
		    	temp.regroup_panels();
		    }
		});

		JMenuItem close_window = new JMenuItem("Close Window");
		close_window.setAccelerator(SystemKeybindings.CLOSE_WINDOW);
		close_window.addActionListener(new ActionListener () {
		    public void actionPerformed(ActionEvent e) {

		    	if (OPEN_VIEWS > 1) {
	    			OPEN_VIEWS--;
		    		window.setVisible(false);
		    		window.dispose();
	    		}
		    }
		});
		
		JMenuBar bar = new JMenuBar();
		
		//MENUS
		JMenu file = new JMenu("File");
		file.add(open_new);
		file.add(open_new_dc);
		file.addSeparator();
		file.add(export);
		file.add(export_analysis);
		bar.add(file);
		
		JMenu window_menu = new JMenu("Window");
		window_menu.add(separate_subpanels);
		window_menu.add(regroup_subpanels);
		window_menu.addSeparator();
		window_menu.add(close_window);
		bar.add(window_menu);
		
		JMenu help = new JMenu("Help");
		bar.add(help);
		
		return bar;
	}
	
	
	//MENU ITEMS FOR MAIN SYSTEM VIEW
	private static JMenuBar get_main_menu_items(SystemWindow<DataStore> window) {
		JMenuBar bar = new JMenuBar();

		//MENUS
		JMenu file = new JMenu("File");
		bar.add(file);

		
		//FUNCTION ITEMS
		JMenuItem save = new JMenuItem("Save");
		save.setAccelerator(SystemKeybindings.SAVE);

		save.addActionListener(new ActionListener () {
		    public void actionPerformed(ActionEvent e) {
		    	//open dialog, set return state to main
		    	//save_dialog.on_scheduled(manager, main_app_view, datastore);
		    }
		});


		JMenuItem save_as = new JMenuItem("Save as...");
		save_as.addActionListener(new ActionListener () {
			public void actionPerformed(ActionEvent e) {
				//open dialog, set return state to mai

		    	if (window.datastore_set()) {
		    		SaveDialog<DataStore> save_dialog = new SaveDialog<DataStore>("Save as");
		    		save_dialog.on_scheduled(window.get_datastore());
		    	}
			}
		});

		JMenuItem open_new = new JMenuItem("New...");
		open_new.setAccelerator(SystemKeybindings.NEW);
		open_new.addActionListener(new ActionListener () {
			public void actionPerformed(ActionEvent e) {
		    	//open dialog, set return state to main

//		    	if (window.datastore_set()) {
				//TODO: by default always create new window
	    		SystemWindow<DataStore> new_window = get_app_view();
	    		new_window.on_start();
	    		
	    		NewDialog file_selector = new NewDialog("Select Files", new_window);
	    		
	    		DataStore new_ds = new DataStore(new_window);
	    		file_selector.on_scheduled(new_ds);
		    }
		});

		JMenuItem open_saved = new JMenuItem("Saved...");
		open_saved.addActionListener(new ActionListener () {
			public void actionPerformed(ActionEvent e) {
		    	//open dialog, set return state to main

		    	//if (window.datastore_set()) {
		    	SystemWindow<DataStore> new_window = get_app_view();
		    	DataStore backend = new DataStore(new_window);
		    	OpenDialog<DataStore> open_dialog = new OpenDialog<DataStore>("Open Files", new_window);
		    	
		    	open_dialog.on_scheduled(backend);

		    }
		});

		JMenuItem drift_correction = new JMenuItem("Drift Correction");
		drift_correction.setAccelerator(SystemKeybindings.DRIFT_CORRECTION);
		drift_correction.addActionListener(new ActionListener () {
		    public void actionPerformed(ActionEvent e) {

				SystemWindow<DriftCorrectionDS> drift_window = get_drift_correction_view();
				DriftCorrectionDS dc_backend = new DriftCorrectionDS(drift_window);
				SystemFileDialog<DriftCorrectionDS> open_dialog = new SystemFileDialog<DriftCorrectionDS>(drift_window, "Drift Correction");

				if (open_dialog.init_backend_on_path(dc_backend)) {
					drift_window.on_scheduled(dc_backend);
				} else {
					new ErrorDialog<DriftCorrectionDS>("Error (Error msg placeholder)", "Bad Drift Correction File").show_dialog();
				}
		    }
		});


		JMenuItem open_test_data = new JMenuItem("Example Data");
		open_test_data.setAccelerator(SystemKeybindings.EX_DATA);
		open_test_data.addActionListener(new ActionListener () {
			public void actionPerformed(ActionEvent e) {
				open_example_data(get_app_view());
		    }
		});

		JMenuItem export_response_graphs = new JMenuItem("Response Graphs (PDF)");
		export_response_graphs.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//open dialog, set return state to main

		    	if (window.datastore_set()) {
		    		ExportDialog export_dialog = new ExportDialog("Exporting", "response");
		    		//export_dialog.init();
		    		//TODO
		    		//export_dialog.on_scheduled(manager, current_state, current_window.get_datastore());
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

		    	if (window.datastore_set()) {
		    		ExportDialog export_dialog = new ExportDialog("Export as", "model");
		    		//export_dialog.init();
		    		//TODO
		    		//export_dialog.on_scheduled(manager, current_state, current_window.get_datastore());
		    	}
		    	else {
					ErrorDialog err = new ErrorDialog("Export Error", "Empty project: Cannot export an empty project. Please open an existing project or create a new project.");
					err.show_dialog();
		    	}
			}
		});

		JMenuItem export_detailed_data = new JMenuItem("Full Model Report (CSV)");
		export_detailed_data.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//open dialog, set return state to main
		    	if (window.datastore_set()) {
		    		ExportDialog export_dialog = new ExportDialog("Export as", "report");
		    		
		    		//TODO this is only for datastore
		    		//export_dialog.on_scheduled(window.get_datastore());
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
		export_submenu.add(export_detailed_data);

		file.add(open_submenu);
		file.addSeparator();
		file.add(drift_correction);
		file.addSeparator();
		file.add(export_submenu);
		file.addSeparator();
		file.add(save_as);
		file.add(save);

		JMenu window_menu = new JMenu("Window");
		bar.add(window_menu);

		JMenuItem separate_subpanels = new JMenuItem("Split Windows");
		separate_subpanels.addActionListener(new ActionListener () {
		    public void actionPerformed(ActionEvent e) {
				SystemWindow<DataStore> temp = window;
		    	temp.split_panels();
		    }
		});

		JMenuItem regroup_subpanels = new JMenuItem("Regroup Windows");
		regroup_subpanels.addActionListener(new ActionListener () {
		    public void actionPerformed(ActionEvent e) {
		    	SystemWindow<DataStore> temp = window;
		    	temp.regroup_panels();
		    }
		});

		JMenuItem close_window = new JMenuItem("Close Window");
		close_window.setAccelerator(SystemKeybindings.CLOSE_WINDOW);
		close_window.addActionListener(new ActionListener () {
		    public void actionPerformed(ActionEvent e) {

		    	if (OPEN_VIEWS > 1) {
	    			OPEN_VIEWS--;
		    		window.setVisible(false);
		    		window.dispose();
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

		window_menu.add(separate_subpanels);
		window_menu.add(regroup_subpanels);
		window_menu.addSeparator();
		window_menu.add(close_window);

		JMenu help = new JMenu("Help");
		bar.add(help);

		return bar;
	}
}
