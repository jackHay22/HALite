package system_drift_correction;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import ui_framework.Refreshable;
import ui_graphlib.GraphPanel;
import ui_stdlib.SystemThemes;

@SuppressWarnings("serial")
public class DriftCorrectionGraph extends ui_framework.SystemPanel<DriftCorrectionDS> {
	private GraphPanel<DriftCorrectionDS> graph;
	private GridBagConstraints constraints;
	
	public DriftCorrectionGraph() {
		super();
		setLayout(new GridBagLayout());
		constraints = SystemThemes.get_grid_constraints();
		graph = new GraphPanel<DriftCorrectionDS>(450, 250);
		graph.setBackground(SystemThemes.BACKGROUND);
		graph.set_title("Drift Correction Graph");
	}

	@Override
	public void refresh() {
		
	}

	@Override
	public void set_datastore(DriftCorrectionDS datastore) {

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
