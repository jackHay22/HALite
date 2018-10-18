package ui_framework;

import system_main.ViewBuilder;

public class StateManager implements Runnable {

	@Override
	public void run() {
		ScheduledState starting_window = ViewBuilder.create_new_default_window(this);
		starting_window.init();
	}
	
	public void release_to(ScheduledState target, StateResult res) {
		target.on_scheduled(this, null, res);
	}
}
