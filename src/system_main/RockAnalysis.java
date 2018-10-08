package system_main;

import java.awt.EventQueue;
import java.util.ArrayList;
import system_utils.io_tools.MultiFileSelector;
import ui_framework.ScheduledState;
import ui_framework.SetupCoordinator;
import ui_framework.SystemWindow;
import ui_graphlib.CorrelationGraph;
import ui_graphlib.ModelGraph;
import ui_stdlib.views.CalculatedValuesPanel;
import ui_stdlib.views.R2SettingsPanel;

public class RockAnalysis  {
	public static void main(String[] args) {
		new RockAnalysis();
	}
	public RockAnalysis() {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
            	ArrayList<ScheduledState> states = new ArrayList<ScheduledState>();
            	states.add(new MultiFileSelector("Select Files"));
            	
            	SystemWindow main_window = new SystemWindow("Ablation Analysis", 1200, 750);

    			R2SettingsPanel test_settings = new R2SettingsPanel();
    			
    			CalculatedValuesPanel test_settings_two = new CalculatedValuesPanel();

    			CorrelationGraph test_correlation = new CorrelationGraph();
    			
    			ModelGraph model_graph = new ModelGraph();
    			
    			main_window.add_system_panel(test_settings);	
    			main_window.add_system_panel(test_correlation);
    			main_window.add_system_panel(test_settings_two);
    			main_window.add_system_panel(model_graph);
    			
            	states.add(main_window);
            	
        		SetupCoordinator system_setup = new SetupCoordinator(states);
        		system_setup.start_schedule();
            }
        });
	}
}
