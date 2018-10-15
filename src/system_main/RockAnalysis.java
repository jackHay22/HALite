package system_main;

import java.awt.EventQueue;
import ui_framework.StateManager;

public class RockAnalysis  {
	public static void main(String[] args) {
		new RockAnalysis();
	}
	public RockAnalysis() {
        EventQueue.invokeLater(new StateManager());
	}
}
