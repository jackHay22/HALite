package system_main;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import system_drift_correction.DriftCorrectionDS;
import system_drift_correction.DriftCorrectionGraph;
import system_drift_correction.DriftCorrectionSettings;
import system_utils.DataStore;
import system_utils.HelpWindow;
import system_utils.io_tools.SystemFileDialog;
import ui_framework.SystemWindow;
import ui_graphlib.CorrelationGraph;
import ui_graphlib.ModelGraph;
import ui_stdlib.CrashReporter;
import ui_stdlib.SystemKeybindings;
import ui_stdlib.SystemThemes;
import ui_stdlib.dialogwindows.ErrorDialog;
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


	private static void set_close_window_status(JMenuItem close_window) {
		close_window.setEnabled(OPEN_VIEWS > 1);
	}
	
	private static void open_help_window(String title, String html_file) {
		JFrame help_frame = new JFrame(title);
		help_frame.setMinimumSize(new Dimension(600,400));
		HelpWindow help_pane = new HelpWindow(html_file);
		help_pane.show();
		help_frame.add(SystemThemes.get_scrollable_panel(help_pane));
		help_frame.setVisible(true);
		
	}
	
	private static JMenu get_help_menu() {
		JMenu help = new JMenu("Help");
		
		JMenuItem help_menu_general = new JMenuItem("User Guide...");
		help_menu_general.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				open_help_window("User Guide","/docs/help.html");
			}
		});
		
		JMenuItem help_menu_format = new JMenuItem("Format Guide...");
		help_menu_format.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				open_help_window("Formatting Guide","/docs/format.html");
			}
		});
		
		help.add(help_menu_general);
		help.add(help_menu_format);
		return help;
	}
	
	private static void open_example_data(SystemWindow<DataStore> current_window) {
    	//open dialog, set return state to main
		
    	//import example data to new datastore
    	DataStore ds = new DataStore(current_window);
    	ds.import_test_data(TEST_XRF, TEST_STANDARDS, TEST_MEANS);

    	//schedule window on datastore
    	current_window.on_scheduled(ds);
	}

	private static SystemWindow<DataStore> get_app_view() {
    	SystemWindow<DataStore> main_window = new SystemWindow<DataStore>("HALite Analysis",
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
		SystemWindow<DriftCorrectionDS> main_window = new SystemWindow<DriftCorrectionDS>("HALite Drift Correction",
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
		return get_app_view();
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
				SystemFileDialog<DriftCorrectionDS> open_dialog = new SystemFileDialog<DriftCorrectionDS>(drift_window, "Drift Correction", "csv");

				if (open_dialog.init_backend_on_path(dc_backend)) {
					//new Backend was able to init on new file
					drift_window.on_scheduled(dc_backend);
				} else {
					//new ErrorDialog<DriftCorrectionDS>("Error (Error msg placeholder)", "Bad Drift Correction File").show_dialog();
				}
		    }
		});
		
		JMenuItem export = new JMenuItem("Export");
		//export.setAccelerator(SystemKeyBindings.);
		export.addActionListener(new ActionListener () {
			public void actionPerformed(ActionEvent e) {

				DriftCorrectionDS ds = window.get_datastore();
				SystemFileDialog<DriftCorrectionDS> dialog = new SystemFileDialog<DriftCorrectionDS>(window, "Export to file...", "csv");
				
				if (!dialog.export_on_path(ds,SystemThemes.CSV_DRIFT_CORRECTION)) {
					//new ErrorDialog<DriftCorrectionDS>("Error","Failed to export").show_dialog();
				}
		    }
		});
		
		JMenuItem export_analysis = new JMenuItem("Proceed to analysis...");
		//open_new.setAccelerator(SystemKeybindings.NEW);
		export_analysis.addActionListener(new ActionListener () {
			public void actionPerformed(ActionEvent e) {
				
				SystemWindow<DataStore> new_analysis_window = get_app_view();
				
				DriftCorrectionDS ds = window.get_datastore();
				SystemFileDialog<DriftCorrectionDS> dialog = new SystemFileDialog<DriftCorrectionDS>(window, "Export to file...", "csv");
				
				if (!dialog.export_on_path(ds,SystemThemes.CSV_DRIFT_CORRECTION)) {
					//new ErrorDialog<DriftCorrectionDS>("Error","Failed to export").show_dialog();
				} else {
					
					//TODO: set with means file selected
					NewDialog file_selector = new NewDialog("Select Files", new_analysis_window, dialog.last_path());
		    		
		    		DataStore new_ds = new DataStore(new_analysis_window);
		    		file_selector.on_scheduled(new_ds);
		    		
		    		window.setVisible(false);
		    		window.dispose();
				}
		    }
		});
		

		JMenuItem separate_subpanels = new JMenuItem("Split Windows");
		JMenuItem regroup_subpanels = new JMenuItem("Regroup Windows");
		
		separate_subpanels.addActionListener(new ActionListener () {
		    public void actionPerformed(ActionEvent e) {
				SystemWindow<DriftCorrectionDS> temp = window;
		    	temp.split_panels();
		    }
		});

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
		    	
		    	set_close_window_status(close_window);
		    }
		});
		
		JMenuBar bar = new JMenuBar();
		
		//MENUS
		JMenu file = new JMenu("File");
		
		//add state listener on dropdown
		file.addMenuListener(new MenuListener() {
			@Override
			public void menuSelected(MenuEvent e) {
			    boolean can_export = window.get_datastore().can_export();
			    export.setEnabled(can_export);
			    export_analysis.setEnabled(can_export);
			}

			@Override
			public void menuDeselected(MenuEvent e) {}

			@Override
			public void menuCanceled(MenuEvent e) {}
		});
		
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
		
		
		//disable splitting if backend not loaded, otherwise toggle between options
		window_menu.addMenuListener(new MenuListener() {
			@Override
			public void menuSelected(MenuEvent e) {
			    boolean ds_loaded = window.datastore_set();
			    boolean is_split = window.windows_split();	    
			    separate_subpanels.setEnabled(ds_loaded & !is_split);
			    regroup_subpanels.setEnabled(ds_loaded & is_split);
			}

			@Override
			public void menuDeselected(MenuEvent e) {}

			@Override
			public void menuCanceled(MenuEvent e) {}
		});
		
		bar.add(get_help_menu());
		
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
				SystemWindow<DataStore> window_update;
				
//				try {
//					int x = 1 / 0;
//				} catch (Exception ex) {
//					CrashReporter.report_crash(window, ex);
//				}

		    	if (window.datastore_set()) {
		    		window_update = get_app_view();
		    	} else {
		    		window_update = window;
		    	}
	    		
	    		NewDialog file_selector = new NewDialog("Select Files", window_update);
	    		
	    		DataStore new_ds = new DataStore(window_update);
	    		file_selector.on_scheduled(new_ds);
		    }
		});

		JMenuItem open_saved = new JMenuItem("Saved...");
		open_saved.addActionListener(new ActionListener () {
			public void actionPerformed(ActionEvent e) {
		    	//open dialog, set return state to main
				SystemWindow<DataStore> window_update;
				
				if (window.datastore_set()) {
		    		window_update = get_app_view();
		    	} else {
		    		window_update = window;
		    	}
				
		    	DataStore backend = new DataStore(window_update);
		    	OpenDialog<DataStore> open_dialog = new OpenDialog<DataStore>("Open Files", window_update);
		    	
		    	open_dialog.on_scheduled(backend);

		    }
		});

		JMenuItem drift_correction = new JMenuItem("Drift Correction");
		drift_correction.setAccelerator(SystemKeybindings.DRIFT_CORRECTION);
		drift_correction.addActionListener(new ActionListener () {
		    public void actionPerformed(ActionEvent e) {

				SystemWindow<DriftCorrectionDS> drift_window = get_drift_correction_view();
				DriftCorrectionDS dc_backend = new DriftCorrectionDS(drift_window);
				SystemFileDialog<DriftCorrectionDS> open_dialog = new SystemFileDialog<DriftCorrectionDS>(drift_window, "Drift Correction", "csv");

				if (open_dialog.init_backend_on_path(dc_backend)) {
					drift_window.on_scheduled(dc_backend);
				} else {
					//new ErrorDialog<DriftCorrectionDS>("Error (Error msg placeholder)", "Bad Drift Correction File").show_dialog();
				}
		    }
		});


		JMenuItem open_test_data = new JMenuItem("Example Data");
		open_test_data.setAccelerator(SystemKeybindings.EX_DATA);
		open_test_data.addActionListener(new ActionListener () {
			public void actionPerformed(ActionEvent e) {
				SystemWindow<DataStore> update_window;
				//create a new window if the current window already has datastore set
		    	if (window.datastore_set()) {
		    		update_window = get_app_view();
		    	} else {
		    		update_window = window;
		    	}
				open_example_data(update_window);
		    }
		});

		JMenuItem export_response_graphs = new JMenuItem("Response Graphs (PDF)");
		export_response_graphs.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//open dialog, set return state to main

				if (window.datastore_set()) {
		    		SystemFileDialog<DataStore> save_dialog = new SystemFileDialog<DataStore>(window, "Export", "pdf");
		    		
		    		if (!save_dialog.export_on_path(window.get_datastore(),SystemThemes.PDF_RESPONSE_GRAPHS)) {
		    			//new ErrorDialog<DataStore>("Export Error", "Unable to export response graphs").show_dialog();
		    		}
		    	}
		    	else {
					//new ErrorDialog<DataStore>("Export Error", "Empty project: Cannot export an empty project. Please open an existing project or create a new project.").show_dialog();
		    	}
			}
		});

		JMenuItem export_calibration_graphs = new JMenuItem("Calibration Models & Graphs (PDF)");
		export_calibration_graphs.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				if (window.datastore_set()) {
		    		SystemFileDialog<DataStore> save_dialog = new SystemFileDialog<DataStore>(window, "Export", "pdf");
		    		
		    		if (!save_dialog.export_on_path(window.get_datastore(),SystemThemes.PDF_CALIBRATION_GRAPHS)) {
		    			//new ErrorDialog<DataStore>("Export Error", "Unable to export calibration pdf").show_dialog();
		    		}
		    	}
		    	else {
					//new ErrorDialog<DataStore>("Export Error", "Empty project: Cannot export an empty project. Please open an existing project or create a new project.").show_dialog();
		    	}
			}
		});

		JMenuItem export_model_data = new JMenuItem("Model Data (CSV)");
		export_model_data.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//open dialog, set return state to main

				if (window.datastore_set()) {
		    		SystemFileDialog<DataStore> save_dialog = new SystemFileDialog<DataStore>(window, "Export Model Data", "csv");
		    		
		    		if (!save_dialog.export_on_path(window.get_datastore(),SystemThemes.CSV_MODEL_DATA)) {
		    			//new ErrorDialog<DataStore>("Export Error", "Unable to export model data").show_dialog();
		    		}
		    	}
		    	else {
					//new ErrorDialog<DataStore>("Export Error", "Empty project: Cannot export an empty project. Please open an existing project or create a new project.").show_dialog();
		    	}
			}
		});

		JMenuItem export_detailed_data = new JMenuItem("Full Model Report (CSV)");
		export_detailed_data.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//open dialog, set return state to main
		    	if (window.datastore_set()) {
		    		SystemFileDialog<DataStore> save_dialog = new SystemFileDialog<DataStore>(window, "Export", "csv");
		    		
		    		if (!save_dialog.export_on_path(window.get_datastore(),SystemThemes.CSV_FULL_REPORT)) {
		    			//new ErrorDialog<DataStore>("Export Error", "Unable to export full model report").show_dialog();
		    		}
		    	}
		    	else {
					//new ErrorDialog<DataStore>("Export Error", "Empty project: Cannot export an empty project. Please open an existing project or create a new project.").show_dialog();
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
		
		file.addMenuListener(new MenuListener() {
			@Override
			public void menuSelected(MenuEvent e) {
				boolean can_proceed = window.datastore_set();
				export_submenu.setEnabled(can_proceed);
				save_as.setEnabled(can_proceed);
				save.setEnabled(can_proceed);
			}

			@Override
			public void menuDeselected(MenuEvent e) {}

			@Override
			public void menuCanceled(MenuEvent e) {}
		});

		JMenu window_menu = new JMenu("Window");
		bar.add(window_menu);

		JMenuItem separate_subpanels = new JMenuItem("Split Windows");
		JMenuItem regroup_subpanels = new JMenuItem("Regroup Windows");
		
		separate_subpanels.addActionListener(new ActionListener () {
		    public void actionPerformed(ActionEvent e) {
				SystemWindow<DataStore> temp = window;
		    	temp.split_panels();
		    }
		});

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
		    	
		    	set_close_window_status(close_window);
		    }
		});

		window_menu.add(separate_subpanels);
		window_menu.add(regroup_subpanels);
		window_menu.addSeparator();
		window_menu.add(close_window);
		
		//disable splitting if backend not loaded, otherwise toggle between options
		window_menu.addMenuListener(new MenuListener() {
			@Override
			public void menuSelected(MenuEvent e) {
			    boolean ds_loaded = window.datastore_set();
			    boolean is_split = window.windows_split();	    
			    separate_subpanels.setEnabled(ds_loaded & !is_split);
			    regroup_subpanels.setEnabled(ds_loaded & is_split);
			    
			    set_close_window_status(close_window);
			}

			@Override
			public void menuDeselected(MenuEvent e) {}

			@Override
			public void menuCanceled(MenuEvent e) {}
		});

		
		bar.add(get_help_menu());

		return bar;
	}
}
