package system_drift_correction;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import system_drift_correction.utils.DriftCorrectionCSVReader;
import system_drift_correction.utils.ElementCPSInfo;
import system_drift_correction.utils.ElementDriftInfo;
import system_utils.Element;
import system_utils.io_tools.CSVWriter;
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

	public DriftCorrectionDS(SystemWindow<DriftCorrectionDS> window_parent) {
		super(window_parent);
		
		//default degree
		degree = 3;
		loaded_cps_info = false;
		standards = new ArrayList<String>();
	}
	
	@Override
	public void refresh() {
		if (loaded_cps_info) {
			ElementCPSInfo elem_info = cps_info.get(this.element);
			if (elem_info != null) {
				elem_info.set_datastore(this);
				elem_info.refresh();
			}
		}
	}
	
	@Override
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
			this.refresh();
			
		} catch (ValExpectedException | IOException e) {
			//IO ERROR OR DATE/TIME NOT FOUND
			return false;
		}
		return loaded_cps_info;
	}
	
	private ArrayList<Double> sorted_times(String s) {
		
		ArrayList<Double> times = new ArrayList<Double>();
		
		for (Element e : Element.values()) {
			if (cps_info.containsKey(e)) {
				times = cps_info.get(e).get_sorted_times();
				break;
			}
		}	
		return times;
	}
	
	public String get_full_report() {
		StringBuilder sb = new StringBuilder();
		sb.append(this.get_header());
		
		for (String s : this.get_sample_list()) {
			ArrayList<Double> times = this.sorted_times(s);
			
			for (int i = 0; i < times.size(); i++) {
				Double d = times.get(i);
				sb.append(s);
				sb.append(',');
				sb.append(d);
				sb.append(',');
				
				for (Element elem : Element.values()) {
					ElementCPSInfo info = cps_info.get(elem);
					if (info != null) {
						sb.append(info.get_sorted_points(s).get(i).get_y());
						sb.append(',');
					}
				}
				sb.append('\n');
			}
			
			sb.append("mean,");
			
			for (Element elem : Element.values()) {
				ElementCPSInfo info = cps_info.get(elem);
				if (info != null) {
					sb.append(info.get_stats(s).get("mean"));
					sb.append(',');
				}
			}

			sb.append('\n');
			sb.append("std dev,");
			
			for (Element elem : Element.values()) {
				ElementCPSInfo info = cps_info.get(elem);
				if (info != null) {
					sb.append(info.get_stats(s).get("std dev"));
					sb.append(',');
				}
			}

			sb.append('\n');
			sb.append("see,");
			
			for (Element elem : Element.values()) {
				ElementCPSInfo info = cps_info.get(elem);
				if (info != null) {
					sb.append(info.get_stats(s).get("see"));
					sb.append(',');
				}
			}

			sb.append('\n');
			sb.append('\n');
		}

		return sb.toString();
		
	}
	
	public String get_means() {
		StringBuilder sb = new StringBuilder();
		sb.append("#Means, , \n");
		sb.append(this.get_header());
		
		for (String s : this.get_sample_list()) {
			sb.append(s);
			sb.append(",mean,");
			for (Element elem : Element.values()) {
				ElementCPSInfo info = cps_info.get(elem);
				if (info != null) {
					sb.append(info.get_mean(s));
					sb.append(',');
				}
			}
			sb.append('\n');
		}

		sb.append("#END");
		return sb.toString();
	}
	
	private String get_header() {
		StringBuilder sb = new StringBuilder();
		sb.append("sourcefile,note,");
		
		for (Element elem : Element.values()) {
			if (cps_info.get(elem) != null) {
				sb.append(elem.toString());
				sb.append(',');
			}
		}
		
		sb.append('\n');
		
		return sb.toString();
	}
	
	@Override
	public void notify_update() {
		//on changes to data
		refresh();
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
	
	public ElementDriftInfo get_plot_info() {
		if (this.cps_info != null) {
			ElementCPSInfo elem_info = this.cps_info.get(this.get_element());
			if (elem_info != null) {
				ElementDriftInfo info = elem_info.get_drift_info();
				return info;
			}
		}
		return null;
	}
	
	public ArrayList<String> get_sample_list() {
		return this.standards;
	}
	
	public void set_degree(int degree) {
		this.degree = degree;
	}
	
	public String get_eqn() {
		//TODO: use system themes for superscript
		if (this.get_plot_info() != null) {
			if (this.get_plot_info().get_equation() != null) {
				return this.get_plot_info().get_equation().get_str_rep();
			}
		}
		return "---";
	}
	
	public String get_rsqrd() {
		if (this.get_plot_info() != null) {
			if (this.get_plot_info().get_equation() != null) {
				return Double.toString(this.get_plot_info().get_equation().get_r2());
			}
		}
		return "---";
	}

	@Override
	public void set_datastore(DriftCorrectionDS datastore) {

	}

	@Override
	public void add_refreshable(Refreshable<DriftCorrectionDS> refreshable_component) {
		
	}
	
	@Override
	public boolean on_export(String file_path, int export_type) {
		//TODO: write to output file
		
		CSVWriter.writeToCSV(file_path + "_full_report", get_full_report());
		
		return CSVWriter.writeToCSV(file_path, get_means());
		
	}
}
