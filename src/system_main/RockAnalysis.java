package system_main;

import ui_framework.SystemWindow;
import ui_graphlib.GraphPanel;
import ui_framework.WindowSubPanel;

public class RockAnalysis {

	public static void main(String[] args) {
		int window_width = 1400;
		int window_height = 700;
		SystemWindow test = new SystemWindow("Test", window_width, window_height);
		test.set_minimum_size(window_width, window_height);
		
		for (int i = 0; i < 4; i++) {
			WindowSubPanel new_subpanel = new WindowSubPanel();
			new_subpanel.add_panel(new GraphPanel());
			test.add_subpanel(new_subpanel);
		}
		test.start_window();
	}

}
