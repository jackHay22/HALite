package ui_framework;

public class WindowLoader implements Runnable {
	@Override
	public void run() {
		//create a starting window from viewbuilder and schedule
		SystemWindow<system_utils.DataStore> starting_window = system_main.ViewBuilder.create_new_default_window();
		starting_window.on_start();
	}
}