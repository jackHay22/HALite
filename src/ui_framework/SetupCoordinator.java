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
		if (current_state + 1 < states.size()) {
			current_state++;
			states.get(current_state).on_scheduled(this, res);
		}
	}
	
	public void release_to(int index, StateResult res) {
		if (index < states.size() && index > 0) {
			current_state = index;
			states.get(current_state).on_scheduled(this, res);
		}
	}
	
	public void release_to(ScheduledState target) {
		target.on_scheduled(this, null);
	}
	
	public void state_crashed() {
		//TODO: figure out how to make this work with system window
		if (current_state - 1 >= 0) {
			states.get(current_state - 1).on_rollback(this);
		}
	}
}
