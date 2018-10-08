package ui_framework;

import java.util.ArrayList;

public class SetupCoordinator {

	private ArrayList<ScheduledState> states;
	private int current_state = 0;
	
	public SetupCoordinator(ArrayList<ScheduledState> states) {
		this.states = states;
	}

	public void start_schedule() {
		this.states.get(current_state).on_scheduled(this, null);
	}
	
	public void release(StateResult res) {
		int next_state = current_state + 1;
		if (next_state < states.size()) {
			states.get(next_state).on_scheduled(this, res);
		}
	}
}
