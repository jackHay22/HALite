package system_drift_correction;

import ui_framework.Refreshable;

@SuppressWarnings("serial")
public class DriftCorrectionSettings<Backend extends DriftCorrectionDS> extends ui_framework.SystemPanel<Backend> implements Refreshable<Backend> {
	
	public DriftCorrectionSettings() {
		super();
	}

	@Override
	public void refresh() {
	}

	@Override
	public void set_datastore(Backend datastore) {
	}

	@Override
	public void add_refreshable(Refreshable<Backend> refreshable_component) {

	}

	@Override
	public void on_start() {
		setVisible(true);
	}

}
