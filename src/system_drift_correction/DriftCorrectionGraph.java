package system_drift_correction;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.ArrayList;

import system_drift_correction.utils.ElementDriftInfo;
import system_utils.EquationPlot;
import ui_framework.Refreshable;
import ui_graphlib.GraphPanel;
import ui_graphlib.Point;
import ui_graphlib.PointSet;
import ui_stdlib.SystemThemes;

@SuppressWarnings("serial")
public class DriftCorrectionGraph extends ui_framework.SystemPanel<DriftCorrectionDS> {
	private GraphPanel<DriftCorrectionDS> graph;
	private GridBagConstraints constraints;
	private DriftCorrectionDS datastore;
	
	private double x_ratio;
	private double y_ratio;
	
	private int draw_width;
	private int draw_height;

	private double bottom_buffer_x;
	private double bottom_buffer_y;
	
	private EquationPlot equation;
	
	private ElementDriftInfo info_to_plot;
	private ArrayList<PointSet<DriftCorrectionDS>> points_to_plot;
	
	public DriftCorrectionGraph() {
		super();
		setLayout(new GridBagLayout());
		constraints = SystemThemes.get_grid_constraints();
		graph = new GraphPanel<DriftCorrectionDS>(450, 250);
		graph.setBackground(SystemThemes.BACKGROUND);
		graph.set_title("Drift Correction Graph");
	}

	private PointSet<DriftCorrectionDS> get_equation_points() {
		
		ArrayList<Point> points_for_line = new ArrayList<Point>();
		
		for (Double x = this.graph.get_min_x(); x < this.graph.get_max_x(); x += 0.005) {
			Double y = this.equation.get_y(x);
			points_for_line.add(new Point(x, y));
		}
		
		return new PointSet<DriftCorrectionDS>(points_for_line, SystemThemes.HIGHLIGHT2, "", "", "", true);
		
	}
	
	private void set_vals() {
		this.draw_width = graph.get_width();
		this.draw_height = graph.get_height();
		this.x_ratio = graph.get_x_r();
		this.y_ratio = graph.get_y_r();
		this.bottom_buffer_x = graph.get_bbx();
		this.bottom_buffer_y = graph.get_bby();
	}
	
	@Override
	public void refresh() {
		this.info_to_plot = this.datastore.get_plot_info();
		if (this.info_to_plot != null) {
			this.points_to_plot = info_to_plot.get_point_sets();
			this.equation = info_to_plot.get_equation();
			this.graph.set_point_sets(this.points_to_plot);
			this.graph.refresh();
			this.set_vals();
			this.points_to_plot.add(this.get_equation_points());
			this.graph.refresh();
			this.revalidate();
		}
	}

	@Override
	public void set_datastore(DriftCorrectionDS datastore) {
		this.datastore = datastore;
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
		add(graph, constraints);
		graph.on_start();
		setVisible(true);
	}

}
