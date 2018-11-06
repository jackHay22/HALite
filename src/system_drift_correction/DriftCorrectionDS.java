package system_drift_correction;

import ui_framework.DataBackend;
import ui_framework.Refreshable;
import ui_framework.SystemWindow;

public class DriftCorrectionDS extends DataBackend implements Refreshable<DriftCorrectionDS> {

	public DriftCorrectionDS(SystemWindow<DataBackend> window_parent) {
		super(window_parent);
	}
	
	@Override
	public void refresh() {

		
	}
	
	@Override
	public void notify_update() {
		//on changes to data
		super.notify_update();
	}


	@Override
	public void on_start() {

		
	}
	
//	public void notify_update() {
//		internal_refresh();
//		super.notify_update();
//	}

	@Override
	public void set_datastore(DriftCorrectionDS datastore) {

	}

	@Override
	public void add_refreshable(Refreshable<DriftCorrectionDS> refreshable_component) {
		
	}

}
