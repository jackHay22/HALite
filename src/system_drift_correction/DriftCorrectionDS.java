package system_drift_correction;

import system_utils.Element;
import ui_framework.DataBackend;
import ui_framework.Refreshable;
import ui_framework.SystemWindow;

public class DriftCorrectionDS extends DataBackend implements Refreshable<DriftCorrectionDS> {
	private Element element;
	private int degree;

	public DriftCorrectionDS(SystemWindow<DataBackend> window_parent) {
		super(window_parent);
		
		//default degree
		degree = 6;
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
	
	public void set_element(Element element) {
		this.element = element;
	}
	
	public Element get_element() {
		return element;
	}
	
	public int get_degree() {
		return degree;
	}
	
	public void set_degree(int degree) {
		this.degree = degree;
	}

	@Override
	public void set_datastore(DriftCorrectionDS datastore) {

	}

	@Override
	public void add_refreshable(Refreshable<DriftCorrectionDS> refreshable_component) {
		
	}

}
