package system_main;

import ui_framework.SystemWindow;
import ui_graphlib.CorrelationGraph;
import ui_graphlib.ModelGraph;
import ui_stdlib.views.CalculatedValuesPanel;
import ui_stdlib.views.R2SettingsPanel;

public class ViewBuilder {
	public static SystemWindow get_app_view() {
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
}
