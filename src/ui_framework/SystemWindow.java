package ui_framework;

import javax.swing.JFrame;
import javax.swing.JSplitPane;
import system_utils.DataStore;
import ui_stdlib.SystemThemes;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.ArrayList;

@SuppressWarnings("serial")
public class SystemWindow extends JFrame implements Refreshable, ScheduledState {
	private ArrayList<Refreshable> refreshable_frames;
	private ArrayList<SystemPanel> panel_references;
	private int subframe_width;
	private int subframe_height;
	private int resize_buffer;
	private boolean did_load_datastore = false;

	public SystemWindow(String title, int width, int height) {
		super(title);
		
		refreshable_frames = new ArrayList<Refreshable>();
		panel_references = new ArrayList<SystemPanel>();
		resize_buffer = 20;
		
		this.setLayout(new BorderLayout());
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.validate();
	}
	
	public void set_minimum_size(int width, int height) {
		this.setMinimumSize(new Dimension(width, height));
		this.subframe_width = (int) width / 2 - resize_buffer;
		this.subframe_height = (int) height / 2 - (3 * resize_buffer);
	}
	
	@Override
	public void refresh() {
		if (did_load_datastore) {
			for (int i=0; i < this.refreshable_frames.size(); i++) {
				this.refreshable_frames.get(i).refresh();
			}
		}
	}

	@Override
	public void set_datastore(DataStore datastore) {
		System.out.println("Setting new datastore");
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
	
	public boolean datastore_set() {
		return did_load_datastore;
	}


	@Override
	public void on_start() {
		//setJMenuBar(manager.get_menu_bar());
		
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
		revalidate();
		setVisible(true);
	}

	@Override
	public void on_scheduled(StateManager callback, ScheduledState previous, StateResult prev_state) {
		if (prev_state != null) {
			set_datastore((DataStore) prev_state);
			on_start();
			did_load_datastore = true;
		} 
	}

	@Override
	public void init() {
		setVisible(true);
		add(SystemThemes.get_default_placeholder());
	}
}
