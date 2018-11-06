package system_drift_correction;

public interface DCRefreshable {
	void refresh();
	void on_start();
	void set_datastore(DriftCorrectionDS datastore);
}
