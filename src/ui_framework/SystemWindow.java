package ui_framework;

import javax.swing.JFrame;
import javax.swing.JSplitPane;
import system_utils.DataStore;
import system_utils.FileChooser;
import ui_stdlib.SystemThemes;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.FileNotFoundException;
import java.util.ArrayList;

@SuppressWarnings("serial")
public class SystemWindow extends JFrame implements Refreshable, ScheduledState {
	private ArrayList<Refreshable> refreshable_frames;
	private ArrayList<SystemPanel> panel_references;
	private int subframe_width;
	private int subframe_height;
	private int resize_buffer;

	public SystemWindow(String title, int width, int height) {
		super(title);
		
		refreshable_frames = new ArrayList<Refreshable>();
		panel_references = new ArrayList<SystemPanel>();
		resize_buffer = 10;
		
		this.setLayout(new BorderLayout());
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(width, height);
		this.validate();
	}
	
	public void set_minimum_size(int width, int height) {
		this.setMinimumSize(new Dimension(width, height));
		this.subframe_width = (int) width / 2 - resize_buffer;
		this.subframe_height = (int) height / 2 - (3 * resize_buffer);
	}
	
	@Override
	public void refresh() {
		for (int i=0; i < this.refreshable_frames.size(); i++) {
			this.refreshable_frames.get(i).refresh();
		}
	}

	@Override
	public void set_datastore(DataStore datastore) {
		for (int i=0; i < this.refreshable_frames.size(); i++) {
			this.refreshable_frames.get(i).set_datastore(datastore);
		}
	}
	
	public void add_system_panel(SystemPanel new_panel) {
		
		new_panel.set_minimum_dimension(this.subframe_width, this.subframe_height);
		new_panel.setBackground(SystemThemes.BACKGROUND);
		panel_references.add(new_panel);
		
		//add to list of refreshable objects
		add_refreshable(new_panel);
	}
	
	@Override
	public void add_refreshable(Refreshable refreshable_window) {
		refreshable_frames.add(refreshable_window);
	}


	@Override
	public void on_start() {
		ArrayList<JSplitPane> double_panes = new ArrayList<JSplitPane>();
		
		for (int i = 0; i < this.panel_references.size(); i+=2) {
			double_panes.add(new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, 
					this.panel_references.get(i), 
					this.panel_references.get(i + 1)));
		}
		
		if (double_panes.size() == 2) {
			//group sub panes
			this.add(new JSplitPane(JSplitPane.VERTICAL_SPLIT, 
					double_panes.get(0), double_panes.get(1)), BorderLayout.CENTER);
		} else if (double_panes.size() == 1)  {
			this.add(double_panes.get(0), BorderLayout.CENTER);
		}
		for (int i=0; i < this.refreshable_frames.size(); i++) {
			this.refreshable_frames.get(i).on_start();
		}
		this.setVisible(true);
	}

	@Override
	public void on_scheduled(SetupCoordinator callback, StateResult prev_state) {
		FileChooser file_chooser = (FileChooser) prev_state;
		String[] means = file_chooser.get_means();
		String[] xrf = file_chooser.get_xrf();
		String[] standards = file_chooser.get_standards();
		
		DataStore loaded_datastore = new DataStore(this);

		try {
			loaded_datastore.import_data(xrf, standards, means);
		} catch (Exception e) {
			e.printStackTrace();
		}

		set_datastore(loaded_datastore);
		on_start();
	}

	@Override
	public void on_rollback(SetupCoordinator callback) {
	}

	
}
