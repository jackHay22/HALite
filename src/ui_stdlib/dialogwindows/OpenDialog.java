package ui_stdlib.dialogwindows;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import system_utils.DataStore;
import system_utils.io_tools.SystemFileDialog;
import ui_framework.SystemWindow;
import ui_framework.ScheduledState;

public class OpenDialog implements ScheduledState<DataStore> {
	private SystemWindow<DataStore> main_window;
	private SystemFileDialog<DataStore> file_dialog;
	
	public OpenDialog(String title, SystemWindow<DataStore> main_window) {
		this.main_window = main_window;
		file_dialog = new SystemFileDialog<DataStore>(main_window, "Open...", "ds");
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
	public void on_scheduled(DataStore backend) {
		
		boolean status = file_dialog.init_backend_on_path(backend);
		
		String file_path = backend.get_path();
		
		if (status && is_datastore_file(file_path)) {
			try {
				FileInputStream fileInputStream = new FileInputStream(file_path);
				ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
				
				DataStore ds = (DataStore) objectInputStream.readObject();
				objectInputStream.close();
				
				ds.set_window(main_window);
				
				main_window.on_scheduled(ds);
				
			} catch (IOException | ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
	}

}
