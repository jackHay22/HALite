package ui_stdlib.dialogwindows;

import ui_framework.SetupCoordinator;
import ui_framework.StateResult;

@SuppressWarnings("serial")
public class SaveDialog extends SystemDialog implements ui_framework.ScheduledState {
	
	public SaveDialog(String title) {
		super(title);
	}

	@Override
	public void on_scheduled(SetupCoordinator callback, StateResult prev_state) {
		show_dialog();
	}

	@Override
	public void on_rollback(SetupCoordinator callback) {
	}
}
