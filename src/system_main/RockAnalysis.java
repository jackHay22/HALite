package system_main;

import ui_framework.SystemWindow;
import ui_graphlib.GraphPanel;

public class RockAnalysis {

	public static void main(String[] args) {
		int window_width = 1200;
		int window_height = 700;
		SystemWindow test = new SystemWindow("Ablation Analysis", window_width, window_height);
		test.set_minimum_size(window_width, window_height);
		
		for (int i = 0; i < 4; i++) {
			test.add_system_panel(new GraphPanel());
		}
		test.start_window();
	}
}
