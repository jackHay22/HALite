package ui_stdlib.dialogwindows;

import java.awt.GridLayout;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import system_utils.DataStore;
import system_utils.io_tools.SystemFileDialog;
import ui_framework.DataBackend;
import ui_framework.SystemWindow;

@SuppressWarnings("serial")
public class OpenDialog<T extends DataBackend> extends SystemDialog implements ui_framework.ScheduledState<T> {
	private SystemWindow<T> main_window;
	private SystemFileDialog<T> file_dialog;
	
	public OpenDialog(String title, SystemWindow<T> main_window) {
		super(title);
		this.setLayout(new GridLayout(4,0));
		
		file_dialog = new SystemFileDialog<T>(this, "Open...");
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
	public void on_scheduled(T backend) {
		
		boolean status = file_dialog.init_backend_on_path(backend);
		
		String file_path = backend.get_path();
		
		if (status && is_datastore_file(file_path)) {
			try {
				FileInputStream fileInputStream = new FileInputStream(file_path);
				ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
				
				T ds = (T) objectInputStream.readObject();
				objectInputStream.close();
				
				ds.set_window(main_window);

				main_window.on_scheduled(ds);
				
			} catch (IOException | ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
	}

}
