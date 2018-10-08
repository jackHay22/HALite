package ui_framework;

public interface ScheduledState {
	public void on_scheduled(SetupCoordinator callback, StateResult prev_state);
}
