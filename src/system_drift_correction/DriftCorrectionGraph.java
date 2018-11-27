package system_drift_correction;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.ArrayList;
import system_drift_correction.utils.ElementDriftInfo;
import ui_framework.Refreshable;
import ui_graphlib.BaseGraph;
import ui_graphlib.GraphPanel;
import ui_graphlib.Point;
import ui_graphlib.PointSet;
import ui_stdlib.SystemThemes;

@SuppressWarnings("serial")
public class DriftCorrectionGraph extends BaseGraph<DriftCorrectionDS> {
	
	private ElementDriftInfo info_to_plot;
	private ArrayList<PointSet<DriftCorrectionDS>> points_to_plot;
	
	public DriftCorrectionGraph() {
		super();
		points_to_plot = new ArrayList<PointSet<DriftCorrectionDS>>();
		setLayout(new GridBagLayout());
		constraints = SystemThemes.get_grid_constraints();
		graph = new GraphPanel<DriftCorrectionDS>(450, 250);
		graph.setBackground(SystemThemes.BACKGROUND);
		graph.set_title("Drift Correction Graph");
	}

	private PointSet<DriftCorrectionDS> get_equation_points() {
		
		ArrayList<Point> points_for_line = new ArrayList<Point>();
		
		for (Double x = this.graph.get_min_x(); x < this.graph.get_max_x(); x += 0.0001) {
			Double y = this.eqn.get_y(x);
			points_for_line.add(new Point(x, y));
		}
		
		return new PointSet<DriftCorrectionDS>(points_for_line, SystemThemes.HIGHLIGHT2, "", "", "", true);
		
	}

	
	@Override
	public void refresh() {
		this.info_to_plot = this.data_store.get_plot_info();
		if (this.info_to_plot != null) {
			this.points_to_plot = info_to_plot.get_point_sets();
			this.eqn = info_to_plot.get_equation();
			this.graph.set_point_sets(this.points_to_plot);
			this.graph.refresh();
			this.points_to_plot.add(this.get_equation_points());
			set_labels();
			this.graph.refresh();
			this.revalidate();
		} else {
			this.points_to_plot.clear();
			this.graph.set_point_sets(this.points_to_plot);
			this.clear_labels();
			this.graph.refresh();
			this.revalidate();
		}
	}
	
	private void set_labels() {
		this.graph.set_r2_eqn_label(this.info_to_plot.get_element().toString());
		
		this.graph.set_y_label("Normalized CPS");
		
		this.graph.set_x_label("Time (out of tree days)");	
	}
	
	private void clear_labels() {
		this.graph.set_r2_eqn_label("No Data Found");
		
		this.graph.set_y_label("No Data Found");
		
		this.graph.set_x_label("No Data Found");
	}

	@Override
	public void set_datastore(DriftCorrectionDS datastore) {
		this.data_store = datastore;
		this.graph.set_datastore(datastore);
	}

	@Override
	public void add_refreshable(Refreshable<DriftCorrectionDS> refreshable_component) {	
	}

	@Override
	public void on_start() {
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.weighty = 1;
		constraints.gridwidth = 1;
		constraints.fill = GridBagConstraints.BOTH;
		this.add(graph, constraints);
		graph.on_start();
		setVisible(true);
	}

}
