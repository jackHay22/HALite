package ui_framework;

public interface ScheduledState<Backend extends DataBackend> {
	public void on_scheduled(Backend backend);
}
