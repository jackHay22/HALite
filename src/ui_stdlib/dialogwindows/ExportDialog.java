package ui_stdlib.dialogwindows;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Map;
import java.util.Map.Entry;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JLabel;

import system_utils.CorrelationInfo;
import system_utils.DataStore;
import system_utils.Element;
import system_utils.ElementCorrelationInfo;
import system_utils.FileChooser;
import ui_framework.ScheduledState;
import ui_framework.StateManager;
import ui_framework.StateResult;
import ui_graphlib.CorrelationGraph;
import ui_graphlib.DrawablePanel;
import ui_stdlib.SystemThemes;

@SuppressWarnings("serial")
public class ExportDialog extends SystemDialog implements ScheduledState {
	private JLabel save_current_instructions;
	private String mode;
	
	public ExportDialog(String title, String mode) {
		super(title);
		//this.setLayout(new BorderLayout(4,0));
		
		save_current_instructions = new JLabel("Save");
		save_current_instructions.setFont(new Font("SansSerif", Font.PLAIN, 16));
		save_current_instructions.setHorizontalAlignment(JLabel.CENTER);
		
		this.mode = mode;
	}
	
	@Override
	public void on_scheduled(StateManager callback, ScheduledState previous, StateResult prev_res) {
		DataStore ds = (DataStore) prev_res;
		FileChooser file_chooser = new FileChooser(this);
		
		if (file_chooser.save_file(ds)) {
			String save_path = ds.get_path().toString();
			update_save_label(save_path);
			
			if (mode.equals("model")) {
				save_model_csv(ds, save_path);
			}
			else if (mode.equals("response")) {
				save_response_graphs(ds, save_path);
			}
			// 2 more cases
			
		}
		

	}
	
	private void save_response_graphs(DataStore ds, String save_path) {
		
		CorrelationGraph graph = new CorrelationGraph();
		graph.set_datastore(ds);
		DrawablePanel gpanel = graph.get_points_panel();
		gpanel.refresh();
		//add(gpanel);
		
		graph.on_start();
		graph.refresh();
		add(graph);
		
		int progress = 0;
		
		JButton button2 = new JButton("Progress: " + progress);
		this.add(button2, BorderLayout.PAGE_END);
		
		show_dialog();
		
		try {
			ArrayList<CorrelationInfo> corrs;
			
			
			Map<Element, ElementCorrelationInfo> all_corrs = ds.get_correlations();
			for (Entry<Element, ElementCorrelationInfo> entry : all_corrs.entrySet()) {
				ElementCorrelationInfo elem_corrs = entry.getValue();
				ArrayList<CorrelationInfo> selected_elems = elem_corrs.get_selected();
				
				for (CorrelationInfo corr_info : selected_elems) {
					
				}
				
				// Call graph.refresh() every time we set a new selected element
				
				//BufferedImage img = new BufferedImage(graph.getWidth(), graph.getHeight(), BufferedImage.TYPE_INT_ARGB);
				//graph.paint(img.getGraphics());
				
				
			}
			
			// Output final image to disk
			//File outputfile = new File(save_path);
		    //ImageIO.write(img, "png", outputfile);
			
		} catch (Exception e) {
			ErrorDialog err = new ErrorDialog("Export Error", "Unable to export project to PDF.");
			err.show_dialog();
		}
	}
	
	private void save_model_csv(DataStore ds, String save_path) {
		try {
			PrintWriter pw = new PrintWriter(new File(save_path + ".csv"));
			String output = ds.get_model_string();
			pw.write(output);
			pw.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	private void update_save_label(String new_label) {
		save_current_instructions.setText(new_label);
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub

	}

}
