package ui_framework;

import javax.swing.JFrame;
import javax.swing.JSplitPane;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.ArrayList;

public class SystemWindow extends JFrame implements Refreshable {
	private ArrayList<SystemSubFrame> frames;
	private int subframe_width;
	private int subframe_height;

	public SystemWindow(String title, int width, int height) {
		super();
		frames = new ArrayList<SystemSubFrame>();
		subframe_width = (int) width / 2;
		subframe_height = (int) height / 2;
		this.setLayout(new BorderLayout());
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JPanel top_left = new JPanel();
		//top_left.setMinimumSize(new Dimension(200, 200));
        JPanel top_right = new JPanel();
        //top_right.setMinimumSize(new Dimension(200, 200));
        JPanel bot_left = new JPanel();
        //bot_left.setMinimumSize(new Dimension(200, 200));
        JPanel bot_right = new JPanel();
       // bot_right.setMinimumSize(new Dimension(200, 200));
        

        JSplitPane top = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, top_left, top_right);
        JSplitPane bot = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, bot_left, bot_right);
        
        JSplitPane split_container = new JSplitPane(JSplitPane.VERTICAL_SPLIT, top, bot);

        this.add(split_container, BorderLayout.CENTER);
        
        this.setSize(width, height);
        this.setVisible(true);
	}
	
	public void add_subwindow(SystemSubFrame new_frame) {
		new_frame.set_minimum_dimension(new Dimension(subframe_width, subframe_height));
		frames.add(new_frame);
	}
	
	@Override
	public void refresh() {
		// TODO Auto-generated method stub

	}
	
	public void start_window() {
		for (int i = 0; i < this.frames.size(); i+=2){
			  ...
		}
		this.setVisible(true);
	}

}
