package system_utils.io_tools;

import java.io.FileNotFoundException;
import java.util.ArrayList;

import system_utils.DataStore;
import system_utils.FileChooser;
import ui_framework.SystemWindow;
import ui_graphlib.CorrelationGraph;
import ui_graphlib.ModelGraph;
import ui_stdlib.views.CalculatedValuesPanel;
import ui_stdlib.views.R2SettingsPanel;

public class SetupCoordinator {
	private DataStore loaded_datastore;
	private SystemWindow main_window;
	
	public SetupCoordinator(int width, int height) {
		main_window = new SystemWindow("Ablation Analysis", width, height);
		main_window.set_minimum_size(width, height);
		loaded_datastore = new DataStore(main_window);
	}

	public void do_file_load() {
		MultiFileSelector selector = new MultiFileSelector("Select Files", this);	
		
		Thread file_selection_thread = new Thread(selector);
		file_selection_thread.start();
	}
	
	public void do_system_setup(FileChooser file_chooser) {
		
		ArrayList<String> means = file_chooser.get_means();
		ArrayList<String> xrf = file_chooser.get_xrf();
		ArrayList<String> standards = file_chooser.get_standards();
		
		try {
			loaded_datastore.import_data(xrf, standards, means);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		R2SettingsPanel test_settings = new R2SettingsPanel();
		test_settings.set_datastore(loaded_datastore);
		
		CalculatedValuesPanel test_settings_two = new CalculatedValuesPanel();
		test_settings_two.set_datastore(loaded_datastore);

		CorrelationGraph test_correlation = new CorrelationGraph();
		test_correlation.set_datastore(loaded_datastore);
		
		ModelGraph model_graph = new ModelGraph();
		model_graph.set_datastore(loaded_datastore);
		
		main_window.add_system_panel(test_settings);	
		main_window.add_system_panel(test_correlation);
		main_window.add_system_panel(test_settings_two);
		main_window.add_system_panel(model_graph);
		
		main_window.run();
	}

}
