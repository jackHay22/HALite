package ui_framework;

public interface ScheduledState<Backend extends DataBackend> {
	//passes backend to state when gaining system focus
	public void on_scheduled(Backend backend);
}
