package system_main;

import ui_framework.SystemWindow;
import ui_graphlib.CorrelationGraph;
import ui_graphlib.ModelGraph;
import ui_stdlib.views.CalculatedValuesPanel;
import ui_stdlib.views.R2SettingsPanel;
import java.awt.EventQueue;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import system_utils.DataStore;
import system_utils.FileChooser;

public class RockAnalysis  {
	public static void main(String[] args) {
		new RockAnalysis();
	}
	public RockAnalysis() {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
        		int window_width = 1200;
        		int window_height = 750; 
        		
        		SystemWindow main_window = new SystemWindow("Ablation Analysis", window_width, window_height);
        		main_window.set_minimum_size(window_width, window_height);
        		
        		DataStore loaded_datastore = new DataStore(main_window);
        		
        		FileChooser file_chooser = new FileChooser(main_window);
        		
        		file_chooser.import_files();
        		ArrayList<String> means = file_chooser.get_means();
        		ArrayList<String> xrf = file_chooser.get_xrf();
        		ArrayList<String> standards = file_chooser.get_standards();
        		
        		try {
					loaded_datastore.import_data(xrf, standards, means);
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
        		
        		R2SettingsPanel test_settings = new R2SettingsPanel();
        		test_settings.set_datastore(loaded_datastore);
        		
        		CalculatedValuesPanel test_settings_two = new CalculatedValuesPanel();
        		test_settings_two.set_datastore(loaded_datastore);
        		
        		//GraphPanel test_graph = new GraphPanel(450, 250);
        		//test_graph.set_datastore(loaded_datastore);
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
        });
	}
}
