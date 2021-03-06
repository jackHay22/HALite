package ui_framework;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JSplitPane;
import ui_stdlib.SystemThemes;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.ArrayList;

@SuppressWarnings("serial")
public class SystemWindow<Backend extends DataBackend> extends JFrame implements Refreshable<Backend>, ScheduledState<Backend> {
	private ArrayList<Refreshable<Backend>> refreshable_frames;
	private ArrayList<SystemPanel<Backend>> panel_references;
	private int subframe_width;
	private int subframe_height;
	private int resize_buffer;
	private boolean did_load_datastore = false;
	private JLabel placeholder;
	private ArrayList<JFrame> split_panels;
	private boolean windows_split = false;
	private ArrayList<JSplitPane> double_panes;
	private JSplitPane main_split;
	private Backend datastore_ref;

	public SystemWindow(String title, int width, int height) {
		//by default, don't kill application
		//alternative parameter: JFrame.EXIT_ON_CLOSE
		this(title, width, height, JFrame.DISPOSE_ON_CLOSE);
	}
	
	public SystemWindow(String title, int width, int height, int close_behavior) {
		super(title);
		
		//create list of panels to be refreshed when datastore changes backend content
		refreshable_frames = new ArrayList<Refreshable<Backend>>();
		panel_references = new ArrayList<SystemPanel<Backend>>();
		resize_buffer = 20;
		
		split_panels = new ArrayList<JFrame>();
		
		this.setLayout(new BorderLayout());
		
		//set close to EXIT_ON_CLOSE or DISPOSE_ON_CLOSE
		this.setDefaultCloseOperation(close_behavior);
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
			//if backend loaded, refresh all refreshable frames in panel
			for (int i=0; i < this.refreshable_frames.size(); i++) {
				this.refreshable_frames.get(i).refresh();
			}
		}
	}
	
	public void add_system_panel(SystemPanel<Backend> new_panel) {
		
		//add a new system panel to jframe
		new_panel.set_minimum_dimension(this.subframe_width, this.subframe_height);
		new_panel.setBackground(SystemThemes.BACKGROUND);
		panel_references.add(new_panel);
		
		//add to list of refreshable objects
		add_refreshable(new_panel);
	}
	
	@Override
	public void add_refreshable(Refreshable<Backend> refreshable_window) {
		refreshable_frames.add(refreshable_window);
	}
	
	public boolean datastore_set() {
		return did_load_datastore;
	}
	
	public Backend get_datastore() {
		return datastore_ref;
	}
	
	public void split_panels() {
		//prevent panels from being split if ds not loaded, windows already split, or drift correction loaded
		if (did_load_datastore && !windows_split && this.panel_references.size() > 2) {
			remove(main_split);
			revalidate();
			repaint();
			getContentPane().setBackground(SystemThemes.BACKGROUND);
			SystemPanel<Backend> temp;
			JFrame temp_frame;
			Dimension screen_dim = Toolkit.getDefaultToolkit().getScreenSize();
			
			//calculate small window sizes
			int half_width = screen_dim.width/2;
			int half_height = subframe_height + 100;
			
			int[] x_vals = {0, half_width, 0, half_width};
			int[] y_vals = {0, 0, half_height, half_height};
			
			for (int i=0; i < this.panel_references.size(); i++) {
				//make new frames and transfer components
				temp = panel_references.get(i);
				temp_frame = new JFrame();
				temp_frame.add(temp);
				temp_frame.setVisible(true);
				temp_frame.setMinimumSize(new Dimension(subframe_width + 30, subframe_height + 30));
				temp_frame.setLocation(x_vals[i], y_vals[i]);
				
				//retain ref to new frame
				split_panels.add(temp_frame);
			}
			
			//set flag
			windows_split = true;
		}
	}
	
	public boolean windows_split() {
		return windows_split;
	}
	
	public void regroup_panels() {
		if (windows_split) {
			
			//add panels back to main frame
			add_panels_to_panes();
			this.revalidate();
			JFrame temp;
			for (int i=0; i< split_panels.size(); i++) {
				temp = split_panels.get(i);
				//remove frame
				temp.setVisible(false);
				temp.dispose();
			}
			split_panels.clear();
			windows_split = false;
		}
	}
	
	private void add_panels_to_panes() {
		
		double_panes = new ArrayList<JSplitPane>();
		
		//add panels to the split panes
		for (int i = 0; i < this.panel_references.size(); i+=2) {
			double_panes.add(new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, 
					this.panel_references.get(i), 
					this.panel_references.get(i + 1)));
		}
		
		//only two panels to add
		if (double_panes.size() == 2) {
			//group sub panes
			main_split = new JSplitPane(JSplitPane.VERTICAL_SPLIT, 
					double_panes.get(0), double_panes.get(1));
			
			this.add(main_split, BorderLayout.CENTER);
		} else if (double_panes.size() == 1)  {
			this.add(double_panes.get(0), BorderLayout.CENTER);
		}
	}

	@Override
	public void on_start() {
		//add placeholder text (no backend loaded yet)
		placeholder = SystemThemes.get_default_placeholder();
		add(placeholder);
		
		//reformat
		revalidate();
		setVisible(true);
	}

	@Override
	public void on_scheduled(Backend backend) {
		//remove default placeholder
		getContentPane().removeAll();
		repaint();
		
		//assign datastore to subcomponents
		set_datastore(backend);
		
		//add system panels
		add_panels_to_panes();
		
		//start system panels
		for (int i=0; i < this.refreshable_frames.size(); i++) {
			this.refreshable_frames.get(i).on_start();
		}
		
		setVisible(true);
	}

	@Override
	public void set_datastore(Backend backend) {
		//set backend ref for child panels
		datastore_ref = backend;
		for (int i=0; i < this.refreshable_frames.size(); i++) {
			this.refreshable_frames.get(i).set_datastore(backend);
		}
		did_load_datastore = true;
	}
}
