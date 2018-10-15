package ui_framework;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import system_main.ViewBuilder;
import system_utils.DataStore;
import ui_stdlib.dialogwindows.NewDialog;
import ui_stdlib.dialogwindows.SaveDialog;
import ui_stdlib.dialogwindows.OpenDialog;

public class StateManager implements Runnable {
	private DataStore datastore;
	private boolean on_starting_view;
	
	//private ScheduledState starting_dialog;
	//private ArrayList<SystemWindow> windows;
	
	public StateManager() {
		//save dialog is stateful and shouldn't be recreated
		//save_dialog = new SaveDialog("Save");

	}

	@Override
	public void run() {
		ScheduledState starting_window = create_new_window();
		starting_window.init();
	}
	
	private ScheduledState create_new_window() {
		SystemWindow main_window = ViewBuilder.get_app_view();
		
		ScheduledState main_app_view = main_window;
		
		main_window.setJMenuBar(get_menu_items(this, main_app_view));
		
		return main_window;
	}
	
	private JMenuBar get_menu_items(StateManager manager, ScheduledState main_app_view) {
		JMenuBar bar = new JMenuBar();
		
		//MENUS
		JMenu file = new JMenu("File");
		bar.add(file);
		
		//FUNCTION ITEMS
		JMenuItem save = new JMenuItem("Save");
		save.addActionListener(new ActionListener () {
		    public void actionPerformed(ActionEvent e) {
		    	//open dialog, set return state to main
		    	//save_dialog.on_scheduled(manager, main_app_view, datastore);
		    }
		});
		
		JMenuItem open_new = new JMenuItem("Open New");
		open_new.addActionListener(new ActionListener () {
		    public void actionPerformed(ActionEvent e) {
		    	//open dialog, set return state to main
		    	ScheduledState current_state = main_app_view;
		    	SystemWindow current_window = (SystemWindow) current_state;
		    	
		    	if (current_window.datastore_set()) {
		    		current_state = create_new_window();
		    		current_window = (SystemWindow) current_state;
		    	}
		    	
		    	NewDialog file_selector = new NewDialog("Select Files", current_window);
			    file_selector.init();
			    file_selector.on_scheduled(manager, current_state, null);
		    	
		    }
		});
		
		JMenuItem open_saved = new JMenuItem("Open");
		open_saved.addActionListener(new ActionListener () {
		    public void actionPerformed(ActionEvent e) {
		    	//open dialog, set return state to main
		    	OpenDialog open_dialog = new OpenDialog("Open Files", (SystemWindow) main_app_view);
		    	open_dialog.init();
		    	open_dialog.on_scheduled(manager, main_app_view, null);
		    }
		});
		
		file.add(open_new);
		file.add(open_saved);
		file.add(save);
		
		return bar;
	}
	
	public void release_to(ScheduledState target, StateResult res) {
		target.on_scheduled(this, null, res);
	}
	
}
