package system_main;

import ui_framework.SystemWindow;
import ui_graphlib.CorrelationGraph;
import ui_graphlib.GraphPanel;
import ui_stdlib.views.SettingsPanel;

import java.awt.EventQueue;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import system_utils.DataStore;

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
        		
        		ArrayList<String> means = new ArrayList<String>();
        		ArrayList<String> xrf = new ArrayList<String>();
        		ArrayList<String> standards = new ArrayList<String>();
        		
        		means.add("/test_data/means.csv");
        		means.add("means");
        		
        		xrf.add("/test_data/xrf.csv");
        		xrf.add("XRF_DATA_RUN_229");
        		
        		standards.add("/test_data/standards.csv");
        		standards.add("standards");
        		
        		try {
					loaded_datastore.import_data(xrf, standards, means);
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
        		
        		SettingsPanel test_settings = new SettingsPanel();
        		test_settings.set_datastore(loaded_datastore);
        		
        		SettingsPanel test_settings_two = new SettingsPanel();
        		test_settings_two.set_datastore(loaded_datastore);
        		
        		//GraphPanel test_graph = new GraphPanel(450, 250);
        		//test_graph.set_datastore(loaded_datastore);
        		CorrelationGraph test_correlation = new CorrelationGraph();
        		test_correlation.set_datastore(loaded_datastore);
        		
        		CorrelationGraph test_correlation2 = new CorrelationGraph();
        		test_correlation2.set_datastore(loaded_datastore);
        		
        		main_window.add_system_panel(test_settings);	
        		main_window.add_system_panel(test_correlation);
        		main_window.add_system_panel(test_settings_two);
        		main_window.add_system_panel(test_correlation2);
        		
        		main_window.run();
            }
        });
	}
}
