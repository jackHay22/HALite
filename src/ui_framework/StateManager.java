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
	}

	@Override
	public void run() {
		ScheduledState starting_window = create_new_window();
		starting_window.init();
	}
	
	public ScheduledState create_new_window() {
		SystemWindow main_window = ViewBuilder.get_app_view();
		
		ScheduledState main_app_view = main_window;
		
		main_window.setJMenuBar(ViewBuilder.get_menu_items(this, main_app_view));
		
		return main_window;
	}
	
	
	public void release_to(ScheduledState target, StateResult res) {
		target.on_scheduled(this, null, res);
	}
	
}
