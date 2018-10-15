package ui_stdlib;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import system_utils.DataStore;
import ui_framework.SystemWindow;
import ui_stdlib.dialogwindows.SaveDialog;

public class SystemManager {
	private DataStore datastore;
	private SaveDialog save_manager;
	
	public SystemManager(DataStore datastore, SystemWindow main_window) {
		this.datastore = datastore;
		save_manager = new SaveDialog("Save", main_window);
	}
	
	public JMenuBar get_menu_bar() {
		JMenuBar bar = new JMenuBar();
		
		//MENUS
		JMenu file = new JMenu("File");
		bar.add(file);
		
		//FUNCTION ITEMS
		JMenuItem save = new JMenuItem("Save");
		save.addActionListener(new ActionListener () {
		    public void actionPerformed(ActionEvent e) {
		    	save_manager.on_scheduled(null, datastore);
		    }
		});
		
		file.add(save);
		
		return bar;
	}
}
