package system_main;

import java.awt.EventQueue;

public class RockAnalysis  {
	public static void main(String[] args) {
		new RockAnalysis();
	}
	public RockAnalysis() {
        EventQueue.invokeLater(new ui_framework.WindowLoader());
	}
}
