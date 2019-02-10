package system_main;

public class HALite  {
	public static void main(String[] args) {
		new HALite();
	}
	public HALite() {     
		java.awt.EventQueue.invokeLater(new ui_framework.WindowLoader());
	}
}
