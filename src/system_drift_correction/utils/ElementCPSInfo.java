package system_drift_correction.utils;

import java.util.ArrayList;
import java.util.HashMap;

import system_drift_correction.DriftCorrectionDS;
import system_utils.Element;
import ui_framework.Refreshable;
import ui_graphlib.Point;
import ui_graphlib.PointSet;
import ui_stdlib.SystemThemes;

public class ElementCPSInfo implements Refreshable<DriftCorrectionDS> {
	
	private Element element;
	private HashMap<String, PointSet<DriftCorrectionDS>> all_point_sets;

	private PointSet<DriftCorrectionDS> drift_points;
	private ElementDriftInfo drift_info;
	
	private DriftCorrectionDS datastore;
	
	public ElementCPSInfo(Element elem) {
		this.element = elem;
		this.drift_points = new PointSet<DriftCorrectionDS>(new ArrayList<Point>(), SystemThemes.HIGHLIGHT, "time", element.toString(), "Drift data fit", true);
	}
	
	public void set_pair(String name, Point pt) {
		if (name.contains("DRFIT") ) {
			drift_points.add_point(pt);
		} else {
			PointSet<DriftCorrectionDS> points = all_point_sets.get(name);
			if (points != null) {
				points.add_point(pt);
			} else {
				points = new PointSet<DriftCorrectionDS>(new ArrayList<Point>(), SystemThemes.MAIN, "time", element.toString(), name, false);
				points.add_point(pt);
			}
		}
	}

	private HashMap<String, PointSet<DriftCorrectionDS>> correct_info() {
		
	}
	
	@Override
	public void refresh() {
		// TODO Auto-generated method stub
		this.drift_info = new ElementDriftInfo(drift_points);
		this.drift_info.set_datastore(datastore);
	}

	@Override
	public void set_datastore(DriftCorrectionDS datastore) {
		// TODO Auto-generated method stub
		this.datastore = datastore;
		this.refresh();
	}

	@Override
	public void add_refreshable(Refreshable<DriftCorrectionDS> refreshable_component) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void on_start() {
		// TODO Auto-generated method stub
		
	}
	
	
	
}
