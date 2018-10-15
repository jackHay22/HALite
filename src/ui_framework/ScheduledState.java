package ui_framework;

public interface ScheduledState {
	public void on_scheduled(StateManager callback, ScheduledState previous, StateResult prev_res);
	public void init();
}
