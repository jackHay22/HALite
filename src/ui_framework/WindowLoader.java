package ui_framework;

import javax.swing.UIManager;

public class WindowLoader implements Runnable {
	@Override
	public void run() {
		
		try{
			UIManager.setLookAndFeel( UIManager.getSystemLookAndFeelClassName());
			if (System.getProperty("os.name").startsWith("Mac")) {
				System.setProperty("com.apple.mrj.application.apple.menu.about.name", "WikiTeX");
				System.setProperty("apple.laf.useScreenMenuBar", "true");
			}	
		}
		catch(Exception e){}
		
		//create a starting window from viewbuilder and schedule
		SystemWindow<system_utils.DataStore> starting_window = system_main.ViewBuilder.create_new_default_window();
		starting_window.on_start();
	}
}