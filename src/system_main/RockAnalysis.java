package system_main;

import ui_framework.SystemWindow;
import ui_graphlib.CorrelationGraph;
import ui_graphlib.GraphPanel;
import ui_stdlib.SettingsPanel;
import java.awt.EventQueue;
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
        		
        		SettingsPanel test_settings = new SettingsPanel();
        		test_settings.set_datastore(loaded_datastore);
        		
        		SettingsPanel test_settings_two = new SettingsPanel();
        		test_settings_two.set_datastore(loaded_datastore);
        		
        		//GraphPanel test_graph = new GraphPanel(450, 250);
        		//test_graph.set_datastore(loaded_datastore);
        		CorrelationGraph test_correlation = new CorrelationGraph();
        		test_correlation.set_datastore(loaded_datastore);
        		
        		GraphPanel test_graph_two = new GraphPanel(500, 300);
        		test_graph_two.set_datastore(loaded_datastore);
        		
        		main_window.add_system_panel(test_settings);	
        		main_window.add_system_panel(test_correlation);
        		main_window.add_system_panel(test_settings_two);
        		main_window.add_system_panel(test_graph_two);
        		
        		main_window.run();
            }
        });
	}
}
