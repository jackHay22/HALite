package system_utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@SuppressWarnings("serial")
public class HelpWindow extends javax.swing.JTextPane {
	private BufferedReader resource_reader;
	private String content;
	
	public HelpWindow(String resource_help_path) {
		resource_reader = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream(resource_help_path)));
		setContentType("text/html");
		setEditable(false);
	}
	
	public void show() {
	    try {
	        StringBuilder sb = new StringBuilder();
	        String line = resource_reader.readLine();

	        while (line != null) {
	            sb.append(line);
	            sb.append("\n");
	            line = resource_reader.readLine();
	        }
	        content = sb.toString();
	        
	    } catch (IOException e) {
			e.printStackTrace();
		} finally {
	        try {
				resource_reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
	    }
	    
	    setText(content);
	    setVisible(true);
	}
}
