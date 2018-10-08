package system_main;

import java.awt.EventQueue;
import system_utils.io_tools.SetupCoordinator;

public class RockAnalysis  {
	public static void main(String[] args) {
		new RockAnalysis();
	}
	public RockAnalysis() {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
        		SetupCoordinator system_setup = new SetupCoordinator(1200, 750);
        		system_setup.do_file_load();
            }
        });
	}
}
