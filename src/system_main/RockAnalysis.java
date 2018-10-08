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
            	main_window.set_minimum_size(1200, 750);

    			main_window.add_system_panel(new R2SettingsPanel());	
    			main_window.add_system_panel(new CorrelationGraph());
    			main_window.add_system_panel(new CalculatedValuesPanel());
    			main_window.add_system_panel(new ModelGraph());
    			
            	states.add(main_window);
            	
        		SetupCoordinator system_setup = new SetupCoordinator(states);
        		system_setup.start_schedule();
            }
        });
	}
}
