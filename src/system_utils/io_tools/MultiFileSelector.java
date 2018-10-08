package system_utils.io_tools;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JFrame;

import system_utils.FileChooser;
import ui_stdlib.components.StatefulFileSelector;

@SuppressWarnings("serial")
public class MultiFileSelector extends JFrame implements Runnable {
	private ArrayList<StatefulFileSelector> selector_list;
	private int width = 600;
	private int height = 400;
	private JButton continue_button;
	private FileChooser file_chooser;
	private boolean xrf_chosen = false;
	private boolean means_chosen = false;
	private boolean standards_chosen = false;

	
	public MultiFileSelector(String title, SetupCoordinator callback) {
		super(title);	
		
		selector_list = new ArrayList<StatefulFileSelector>();
		this.setLayout(new GridLayout(5,0));
		continue_button = new JButton("Continue");
		continue_button.setEnabled(false);
		
		continue_button.addActionListener(new ActionListener () {
			public void actionPerformed(ActionEvent e) {
				callback.do_system_setup(file_chooser);
				setVisible(false);
				dispose();
			}
        });
		file_chooser = new FileChooser(this);
		add_selection_target(new ActionListener () {
		    public void actionPerformed(ActionEvent e) {
		    	file_chooser.import_files(file_chooser.xrf, "XRF_DATA_RUN_229");
		    	xrf_chosen = true;
		    	can_continue();
		    }
		}, "XRF");
		
		add_selection_target(new ActionListener () {
		    public void actionPerformed(ActionEvent e) {
		    	file_chooser.import_files(file_chooser.means, "means");
		    	means_chosen = true;
		    	can_continue();
		    }
		}, "Means");
		
		add_selection_target(new ActionListener () {
		    public void actionPerformed(ActionEvent e) {
		    	file_chooser.import_files(file_chooser.standards, "standards");
		    	standards_chosen = true;
		    	can_continue();
		    }
		}, "Standards");
	}
	
	private void can_continue() {
		continue_button.setEnabled(xrf_chosen && standards_chosen && means_chosen);
	}
	
	public void add_selection_target(ActionListener task, String label) {
		StatefulFileSelector new_selector = new StatefulFileSelector(label);
		new_selector.get_button().addActionListener(task);
		selector_list.add(new_selector);
		this.add(new_selector);
	}

	@Override
	public void run() {

		this.setMinimumSize(new Dimension(width, height));
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);
		for (int i=0; i < selector_list.size(); i++) {
			selector_list.get(i).on_start();
		}
		this.setResizable(false);
		this.add(continue_button);
		continue_button.setVisible(true);
		this.setVisible(true);
	}

	
}
