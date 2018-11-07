package ui_stdlib.dialogwindows;

import java.awt.FileDialog;
import java.awt.GridLayout;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

import system_utils.DataStore;
import system_utils.io_tools.FileChooser;
import ui_framework.ScheduledState;
import ui_framework.StateManager;
import ui_framework.StateResult;
import ui_framework.SystemWindow;

@SuppressWarnings("serial")
public class OpenDialog extends SystemDialog implements ui_framework.ScheduledState {
	DataStore save_loader;
	FileDialog save_dialog;
	FileChooser file_chooser;
	SystemWindow main_window;
	
	public OpenDialog(String title, SystemWindow main_window) {
		super(title);
		save_loader = new DataStore(main_window);
		save_dialog = new FileDialog(this, "Choose saved file");
		this.main_window = main_window;
		this.setLayout(new GridLayout(4,0));
	}

	@Override
	public void on_scheduled(StateManager callback, ScheduledState previous, StateResult prev_res) {
		
		file_chooser = new FileChooser(this);
		boolean file = file_chooser.import_file(this.save_loader);
		String file_path = this.save_loader.get_path();
		
		if (file && is_datastore_file(file_path)) {
			try {
				FileInputStream fileInputStream = new FileInputStream(file_path);
				ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
				DataStore ds = (DataStore) objectInputStream.readObject();
				objectInputStream.close();
				
				ds.set_window(main_window);
				callback.release_to(previous, (StateResult) ds);
				
			} catch (IOException | ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
	}
	
	private boolean is_datastore_file(String file) {
		int pos = file.indexOf('.');
		if (pos < 0) {
			return false;
		}
		
		String extension = file.substring(pos + 1);
		if (extension.equals("ds")) {
			return true;
		}
		return false;
	}

	@Override
	public void init() {

	}
}
