package system_drift_correction;

import system_utils.DataStore;
import ui_framework.Refreshable;

@SuppressWarnings("serial")
public class DriftCorrectionSettings extends ui_framework.SystemPanel {
	
	public DriftCorrectionSettings() {
		super();
	}

	@Override
	public void refresh() {
	}

	@Override
	public void set_datastore(DataStore datastore) {
	}

	@Override
	public void add_refreshable(Refreshable refreshable_component) {

	}

	@Override
	public void on_start() {
		setVisible(true);
	}

}
