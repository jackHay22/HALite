package system_main;

import ui_framework.SystemWindow;
import ui_graphlib.GraphPanel;
import ui_stdlib.SettingsPanel;
import system_utils.DataStore;

public class RockAnalysis {

	public static void main(String[] args) {
		int window_width = 1200;
		int window_height = 700;
		SystemWindow test = new SystemWindow("Ablation Analysis", window_width, window_height);
		test.set_minimum_size(window_width, window_height);
		
		SettingsPanel test_settings = new SettingsPanel();
		test_settings.set_datastore(new DataStore());
		test.add_system_panel(test_settings);
		test.add_system_panel(new GraphPanel());
		for (int i = 0; i < 2; i++) {
			test.add_system_panel(new GraphPanel());
		}
		test.start_window();
	}
}
