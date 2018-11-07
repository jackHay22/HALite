package system_drift_correction;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import system_drift_correction.utils.DriftCorrectionCSVReader;
import system_drift_correction.utils.ElementCPSInfo;
import system_utils.Element;
import system_utils.io_tools.ValExpectedException;
import ui_framework.DataBackend;
import ui_framework.Refreshable;
import ui_framework.SystemWindow;

public class DriftCorrectionDS extends DataBackend implements Refreshable<DriftCorrectionDS> {
	private Element element;
	private int degree;
	private boolean loaded_cps_info;
	private HashMap<Element, ElementCPSInfo> cps_info;
	private ArrayList<String> standards;

	public DriftCorrectionDS(SystemWindow<DataBackend> window_parent) {
		super(window_parent);
		
		//default degree
		degree = 6;
		loaded_cps_info = false;
		standards = new ArrayList<String>();
	}
	
	@Override
	public void refresh() {
		if (loaded_cps_info) {
			for (ElementCPSInfo elem_info : cps_info.values()) {
				elem_info.set_datastore(this);
			}
		}
	}
	
	public boolean init_from_file(String file_path) {
		DriftCorrectionCSVReader csv_reader = new DriftCorrectionCSVReader();
		BufferedReader file_reader;
		
		try {
			file_reader = new BufferedReader(new FileReader(file_path));
		} catch (FileNotFoundException e) {
			return false;
		}
		
		try {
			cps_info = csv_reader.load_dc_file(file_reader, standards);
			loaded_cps_info = true;
			
		} catch (ValExpectedException | IOException e) {
			//IO ERROR OR DATE/TIME NOT FOUND
			return false;
		}
		return loaded_cps_info;
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
	
	public ArrayList<String> get_sample_list() {
		return this.standards;
	}
	
	public void set_degree(int degree) {
		this.degree = degree;
	}
	
	public String get_eqn() {
		//TODO: use system themes for superscript
		return "";
	}
	
	public String get_rsqrd() {
		return "";
	}

	@Override
	public void set_datastore(DriftCorrectionDS datastore) {

	}

	@Override
	public void add_refreshable(Refreshable<DriftCorrectionDS> refreshable_component) {
		
	}

}
