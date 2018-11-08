package ui_framework;

import system_main.ViewBuilder;
import system_utils.DataStore;

public class WindowLoader implements Runnable {

	@Override
	public void run() {
		SystemWindow<DataStore> starting_window = ViewBuilder.create_new_default_window();
		starting_window.on_start();
	}
}