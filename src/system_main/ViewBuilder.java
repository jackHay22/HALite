package system_main;

import java.awt.Dimension;
import java.awt.FileDialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JCheckBoxMenuItem;
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
	
	private static int CLOSE_APP = JFrame.EXIT_ON_CLOSE;
	private static int CLOSE_WINDOW = JFrame.DISPOSE_ON_CLOSE;
	
	//don't access directly, use static method
	private static boolean DIALOG_OPENED = false;
	private static final Object DIALOG_STATE_MUTEX = new Object();


	public static void update_dialog_status(boolean dialog_state) {
		//keeps track of dialog status to avoid multiple dialogs opening at once
		synchronized (DIALOG_STATE_MUTEX) {
			DIALOG_OPENED = dialog_state;
		}	
	}
	
	public static synchronized boolean get_dialog_status() {
		return DIALOG_OPENED;
	}
	
	private static void set_close_window_status(JMenuItem close_window) {
		//determine close behavior based on number of open windows
		close_window.setEnabled(OPEN_VIEWS > 1);
	}
	
	private static void open_help_window(String title, String html_file) {
		//create a new window and populate it with html doc
		JFrame help_frame = new JFrame(title);
		help_frame.setMinimumSize(new Dimension(600,400));
		HelpWindow help_pane = new HelpWindow(html_file);
		help_pane.show();
		
		//set scrollable
		help_frame.add(SystemThemes.get_scrollable_panel(help_pane));
		help_frame.setVisible(true);
		
	}
	
	private static JMenu get_help_menu() {
		JMenu help = new JMenu("Help");
		
		JMenu docs_submenu = new JMenu("Guides...");
		
		
		//ADD HELP MENU GUIDES
		
		JMenuItem help_menu_general = new JMenuItem("User Guide");
		help_menu_general.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				open_help_window("User Guide","/docs/help.html");
			}
		});
		
		JMenuItem help_menu_format = new JMenuItem("Format Guide");
		help_menu_format.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				open_help_window("Formatting Guide","/docs/format.html");
			}
		});
		
		JMenuItem help_menu_drift = new JMenuItem("Drift Output");
		help_menu_drift.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				open_help_window("Drift Correction Output Guide","/docs/drift_output.html");
			}
		});
		
		JMenuItem help_menu_analysis = new JMenuItem("Analysis Output");
		help_menu_analysis.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				open_help_window("Analysis Output Guide","/docs/analysis_output.html");
			}
		});
		
		//Add guide action listeners to submenu
		
		help.add(docs_submenu);
		docs_submenu.add(help_menu_general);
		docs_submenu.add(help_menu_format);
		docs_submenu.add(help_menu_drift);
		docs_submenu.add(help_menu_analysis);
		
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
		
		//determine current close behavior
		int close_behavior;
		if (OPEN_VIEWS >= 1) {
			close_behavior = CLOSE_WINDOW;
		} else {
			close_behavior = CLOSE_APP;
		}
		
		//set window size
    	SystemWindow<DataStore> main_window = new SystemWindow<DataStore>("HALite Analysis",
														ui_stdlib.SystemThemes.MAIN_WINDOW_WIDTH,
														ui_stdlib.SystemThemes.MAIN_WINDOW_HEIGHT, close_behavior);

    	main_window.set_minimum_size(ui_stdlib.SystemThemes.MAIN_WINDOW_WIDTH,
    								 ui_stdlib.SystemThemes.MAIN_WINDOW_HEIGHT);

    	//add main view panels
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
		
		//define window closing behavior based on view count
		int close_behavior;
		if (OPEN_VIEWS >= 1) {
			close_behavior = CLOSE_WINDOW;
		} else {
			close_behavior = CLOSE_APP;
		}
		
		//create window
		SystemWindow<DriftCorrectionDS> main_window = new SystemWindow<DriftCorrectionDS>("HALite Drift Correction",
															ui_stdlib.SystemThemes.MAIN_WINDOW_WIDTH,
															ui_stdlib.SystemThemes.MAIN_WINDOW_HEIGHT, close_behavior);

		//set min size
    	main_window.set_minimum_size(ui_stdlib.SystemThemes.MAIN_WINDOW_WIDTH,
    								 ui_stdlib.SystemThemes.MAIN_WINDOW_HEIGHT);

    	//add dc panels
    	main_window.add_system_panel(new DriftCorrectionSettings());
    	main_window.add_system_panel(new DriftCorrectionGraph());

    	//add menu items
		main_window.setJMenuBar(get_dc_menu_items(main_window));

		//update window counter
		OPEN_VIEWS++;
    	return main_window;
	}

	public static SystemWindow<DataStore> create_new_default_window() {
		//default is analysis window (without loaded components)
		return get_app_view();
	}


	//MENU ITEMS FOR DRIFT CORRECTION SYSTEM VIEW
	private static JMenuBar get_dc_menu_items(SystemWindow<DriftCorrectionDS> window) {
		
		JMenuItem open_new = new JMenuItem("New Analysis...");
		
		//add keybinding
		open_new.setAccelerator(SystemKeybindings.NEW);
		open_new.addActionListener(new ActionListener () {
			public void actionPerformed(ActionEvent e) {
				//if a new dialog is not currently open, open a new dialog
				if (!get_dialog_status()) {
					SystemWindow<DataStore> new_window = get_app_view();
		    		new_window.on_start();
		    		
		    		NewDialog file_selector = new NewDialog("Select Files", new_window);
		    		
		    		DataStore new_ds = new DataStore(new_window);
		    		
		    		//new dialog is open, schedule file selector
		    		update_dialog_status(true);
		    		file_selector.on_scheduled(new_ds);
				}
		    }
		});
		
		JMenuItem open_new_dc = new JMenuItem("New Drift Correction...");
		//open_new.setAccelerator(SystemKeybindings.NEW);
		
		open_new_dc.addActionListener(new ActionListener () {
			public void actionPerformed(ActionEvent e) {
				SystemWindow<DriftCorrectionDS> drift_window = get_drift_correction_view();
				DriftCorrectionDS dc_backend = new DriftCorrectionDS(drift_window);
				SystemFileDialog<DriftCorrectionDS> open_dialog = new SystemFileDialog<DriftCorrectionDS>(drift_window, "Drift Correction", "csv");
				
				//attempt to load backend on file
				if (open_dialog.init_backend_on_path(dc_backend)) {
					//new Backend was able to init on new file
					drift_window.on_scheduled(dc_backend);
				} else {
					//show error dialog
					new ErrorDialog<DriftCorrectionDS>("Import Error", "Bad Drift Correction File").show_dialog();
				}
				
		    }
		});
		
		JMenuItem export = new JMenuItem("Export");
		//export.setAccelerator(SystemKeyBindings.);
		export.addActionListener(new ActionListener () {
			public void actionPerformed(ActionEvent e) {
				//create file dialog to export through
				DriftCorrectionDS ds = window.get_datastore();
				SystemFileDialog<DriftCorrectionDS> dialog = new SystemFileDialog<DriftCorrectionDS>(window, "Export to file...", "csv");
				
				//attempt export on file path
				if (!dialog.export_on_path(ds,SystemThemes.CSV_DRIFT_CORRECTION)) {
					new ErrorDialog<DriftCorrectionDS>("Error","Failed to export").show_dialog();
				}
		    }
		});
		
		JMenuItem export_analysis = new JMenuItem("Proceed to analysis...");
		//open_new.setAccelerator(SystemKeybindings.NEW);
		export_analysis.addActionListener(new ActionListener () {
			public void actionPerformed(ActionEvent e) {
					//get an analysis app view
					SystemWindow<DataStore> new_analysis_window = get_app_view();
					
					DriftCorrectionDS ds = window.get_datastore();
					SystemFileDialog<DriftCorrectionDS> dialog = new SystemFileDialog<DriftCorrectionDS>(window, "Export to file...", "csv");
					
					//try export
					if (!dialog.export_on_path(ds,SystemThemes.CSV_DRIFT_CORRECTION)) {
						new ErrorDialog<DriftCorrectionDS>("Error","Failed to export").show_dialog();
					} else {
						
						NewDialog file_selector = new NewDialog("Select Files", new_analysis_window, dialog.last_path());
			    		
						//schedule file dialog with new backend
			    		DataStore new_ds = new DataStore(new_analysis_window);
			    		file_selector.on_scheduled(new_ds);
			    		
			    		//remove old drift correction window
			    		window.setVisible(false);
			    		window.dispose();
					}
		    }
		});

		JMenuItem close_window = new JMenuItem("Close Window");
		close_window.setAccelerator(SystemKeybindings.CLOSE_WINDOW);
		close_window.addActionListener(new ActionListener () {
		    public void actionPerformed(ActionEvent e) {

		    	//determine close behavior for future windows
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
				//determine action behavior when menu selected
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
		file.addSeparator();
		file.add(export);
		file.add(export_analysis);
		bar.add(file);
		
		JMenu window_menu = new JMenu("Window");
		window_menu.add(close_window);
		bar.add(window_menu);
		
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
		    	
		    	if (window.datastore_set()) {
		    		SaveDialog save_dialog = new SaveDialog("Save", window);
		    		save_dialog.save(window.get_datastore());
		    	}
		    }
		});


		JMenuItem save_as = new JMenuItem("Save as...");
		save_as.addActionListener(new ActionListener () {
			public void actionPerformed(ActionEvent e) {
				//open dialog, set return state to main

		    	if (window.datastore_set()) {
		    		SaveDialog save_dialog = new SaveDialog("Save as", window);
		    		save_dialog.save_as(window.get_datastore());
		    	}
			}
		});

		JMenuItem open_new = new JMenuItem("New...");
		open_new.setAccelerator(SystemKeybindings.NEW);
		open_new.addActionListener(new ActionListener () {
			public void actionPerformed(ActionEvent e) {
		    	//open dialog, set return state to main
				SystemWindow<DataStore> window_update;

				if (!get_dialog_status()) {
					
					if (window.datastore_set()) {
						//create new window
			    		window_update = get_app_view();
			    	} else {
			    		window_update = window;
			    	}
		    		
		    		NewDialog file_selector = new NewDialog("Select Files", window_update);
		    		
		    		DataStore new_ds = new DataStore(window_update);
		    		
		    		update_dialog_status(true);
		    		
		    		//schedule file dialog
		    		file_selector.on_scheduled(new_ds);
				}
		    }
		});

		JMenuItem open_saved = new JMenuItem("Saved...");
		open_saved.addActionListener(new ActionListener () {
			public void actionPerformed(ActionEvent e) {
		    	//open dialog, set return state to main
					SystemWindow<DataStore> window_update;
					
					if (window.datastore_set()) {
						//ds already set, create new window
			    		window_update = get_app_view();
			    	} else {
			    		window_update = window;
			    	}
					
			    	DataStore backend = new DataStore(window_update);
			    	OpenDialog open_dialog = new OpenDialog("Open Files", window_update);
			    	
			    	//start open dialog
			    	open_dialog.on_scheduled(backend);
		    }
		});

		JMenuItem drift_correction = new JMenuItem("Drift Correction");
		drift_correction.setAccelerator(SystemKeybindings.DRIFT_CORRECTION);
		drift_correction.addActionListener(new ActionListener () {
		    public void actionPerformed(ActionEvent e) {

		    	//create new dc window
				SystemWindow<DriftCorrectionDS> drift_window = get_drift_correction_view();
				DriftCorrectionDS dc_backend = new DriftCorrectionDS(drift_window);
				SystemFileDialog<DriftCorrectionDS> open_dialog = new SystemFileDialog<DriftCorrectionDS>(drift_window, "Drift Correction", "csv");

				if (open_dialog.init_backend_on_path(dc_backend)) {
					drift_window.on_scheduled(dc_backend);
				} else {
					//show dialog if init fails
					new ErrorDialog<DriftCorrectionDS>("Import Error", "Bad Drift Correction File").show_dialog();
				}
		    }
		});


		JMenuItem open_test_data = new JMenuItem("Example Data");
		open_test_data.setAccelerator(SystemKeybindings.EX_DATA);
		open_test_data.addActionListener(new ActionListener () {
			public void actionPerformed(ActionEvent e) {
				if (!get_dialog_status()) {
					SystemWindow<DataStore> update_window;
					//create a new window if the current window already has datastore set
			    	if (window.datastore_set()) {
			    		update_window = get_app_view();
			    	} else {
			    		update_window = window;
			    	}
			    	//open example data in window (or new window)
					open_example_data(update_window);
				}
		    }
		});

		JMenuItem export_response_graphs = new JMenuItem("Response Graphs (PDF)");
		export_response_graphs.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//open dialog, set return state to main

				if (window.datastore_set()) {
		    		SystemFileDialog<DataStore> save_dialog = new SystemFileDialog<DataStore>(window, "Export", "pdf");
		    		
		    		//try to export on user-selected path
		    		if (!save_dialog.export_on_path(window.get_datastore(),SystemThemes.PDF_RESPONSE_GRAPHS)) {
		    			new ErrorDialog<DataStore>("Export Error", "Unable to export response graphs").show_dialog();
		    		}
		    	}
			}
		});

		JMenuItem export_calibration_graphs = new JMenuItem("Calibration Models & Graphs (PDF)");
		export_calibration_graphs.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				if (window.datastore_set()) {
		    		SystemFileDialog<DataStore> save_dialog = new SystemFileDialog<DataStore>(window, "Export", "pdf");
		    		
		    		//try to export on path
		    		if (!save_dialog.export_on_path(window.get_datastore(),SystemThemes.PDF_CALIBRATION_GRAPHS)) {
		    			new ErrorDialog<DataStore>("Export Error", "Unable to export calibration pdf").show_dialog();
		    		}
		    	}
			}
		});

		JMenuItem export_model_data = new JMenuItem("Model Data (CSV)");
		export_model_data.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//open dialog, set return state to main

				if (window.datastore_set()) {
		    		SystemFileDialog<DataStore> save_dialog = new SystemFileDialog<DataStore>(window, "Export Model Data", "csv");
		    		
		    		//try to export on path
		    		if (!save_dialog.export_on_path(window.get_datastore(),SystemThemes.CSV_MODEL_DATA)) {
		    			new ErrorDialog<DataStore>("Export Error", "Unable to export model data").show_dialog();
		    		}
		    	}
			}
		});

		JMenuItem export_detailed_data = new JMenuItem("Full Model Report (CSV)");
		export_detailed_data.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//open dialog, set return state to main
		    	if (window.datastore_set()) {
		    		SystemFileDialog<DataStore> save_dialog = new SystemFileDialog<DataStore>(window, "Export", "csv");
		    		
		    		//try to export on path
		    		if (!save_dialog.export_on_path(window.get_datastore(),SystemThemes.CSV_FULL_REPORT)) {
		    			new ErrorDialog<DataStore>("Export Error", "Unable to export full model report").show_dialog();
		    		}
		    	}
			}
		});

		//build menu collections
		JMenu open_submenu = new JMenu("Open...");

		open_submenu.add(open_saved);
		open_submenu.add(open_test_data);

		JMenu export_submenu = new JMenu("Export...");

		export_submenu.add(export_response_graphs);
		export_submenu.add(export_calibration_graphs);
		export_submenu.add(export_model_data);
		export_submenu.add(export_detailed_data);
		
		JMenu element_choices = new JMenu("Element choices...");
		
		JMenuItem element_choices_import = new JMenuItem("Import");
		element_choices_import.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//open dialog
		    	if (window.datastore_set()) {
		    		SystemFileDialog<DataStore> import_dialog = new SystemFileDialog<DataStore>(window, "Import", "hle");
		    		
		    		String abs_path = import_dialog.get_path(FileDialog.LOAD);
		    		
		    		if ((abs_path != null) && !window.get_datastore().import_element_choices(abs_path)) {
		    			new ErrorDialog<DataStore>("Import Error", "Unable to import element choices").show_dialog();
		    		}
		    	}
			}
		});
		
		JMenuItem element_choices_export = new JMenuItem("Export");
		element_choices_export.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//open dialog
		    	if (window.datastore_set()) {
		    		SystemFileDialog<DataStore> export_dialog = new SystemFileDialog<DataStore>(window, "Export", "hle");
		    		
		    		String abs_path = export_dialog.get_path(FileDialog.SAVE);
		    		
		    		//enforce the extension
		    		if (!abs_path.endsWith(".hle")) {
		    			abs_path += ".hle";
		    		}
		    		
		    		if ((abs_path != null) && !window.get_datastore().export_element_choices(abs_path)) {
		    			new ErrorDialog<DataStore>("Export Error", "Unable to export element choices").show_dialog();
		    		}
		    	}
			}
		});
		
		element_choices.add(element_choices_import);
		element_choices.add(element_choices_export);

		file.add(open_new);
		file.addSeparator();
		file.add(open_submenu);
		file.addSeparator();
		file.add(drift_correction);
		file.addSeparator();
		file.add(export_submenu);
		file.addSeparator();
		file.add(element_choices);
		file.addSeparator();
		file.add(save_as);
		file.add(save);
		
		file.addMenuListener(new MenuListener() {
			@Override
			public void menuSelected(MenuEvent e) {
				//on menu selected, determine if components enabled
				boolean can_proceed = window.datastore_set();
				export_submenu.setEnabled(can_proceed);
				element_choices.setEnabled(can_proceed);
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
		JCheckBoxMenuItem truncate_stds_vals = new JCheckBoxMenuItem("Truncate model values");
		
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
		
		truncate_stds_vals.addActionListener(new ActionListener () {
		    public void actionPerformed(ActionEvent e) {
		    	//set flag to truncate values in calc val panel
		    	SystemThemes.TRUNCATE_STDS_VALS = ! SystemThemes.TRUNCATE_STDS_VALS;
		    	
		    	//set update flag
		    	window.get_datastore().calculated_vals_updated = true;
		    	window.refresh();
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
		window_menu.add(truncate_stds_vals);
		window_menu.addSeparator();
		window_menu.add(close_window);
		
		//disable splitting if backend not loaded, otherwise toggle between options
		window_menu.addMenuListener(new MenuListener() {
			@Override
			public void menuSelected(MenuEvent e) {
				//on menu selected, determine if components enabled
			    boolean ds_loaded = window.datastore_set();
			    boolean is_split = window.windows_split();	    
			    separate_subpanels.setEnabled(ds_loaded & !is_split);
			    regroup_subpanels.setEnabled(ds_loaded & is_split);
			    truncate_stds_vals.setEnabled(ds_loaded);
			    
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
