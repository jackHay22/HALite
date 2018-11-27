package system_main;

import java.awt.EventQueue;

public class HALite  {
	public static void main(String[] args) {
		new HALite();
	}
	public HALite() {
        EventQueue.invokeLater(new ui_framework.WindowLoader());
	}
}
