package system_utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.StyleSheet;
import ui_stdlib.SystemThemes;

@SuppressWarnings("serial")
public class HelpWindow extends javax.swing.JTextPane {
	private BufferedReader resource_reader;
	private String content;
	private StyleSheet style_sheet;
	
	public HelpWindow(String resource_help_path) {
		resource_reader = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream(resource_help_path)));
		setContentType("text/html");
		setEditable(false);
		HTMLEditorKit kit = new HTMLEditorKit();
		setEditorKit(kit);
		style_sheet = kit.getStyleSheet();
		
		//add markup rules to this stylesheet
		SystemThemes.set_help_markup_rules(style_sheet);
	}
	
	private String make_img_div(String resource_img_tag) {
		return "<img src=\"" + this.getClass().getClassLoader().getResource(resource_img_tag.split(":")[1].trim()).toString() + "\" />";
	}
	
	public void show() {
	    try {
	        StringBuilder sb = new StringBuilder();
	        String line = resource_reader.readLine();

	        while (line != null) {
	                      
	            //image loading
	            if (line.contains("img")) {
	            	line = make_img_div(line);
	            }
	            
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
