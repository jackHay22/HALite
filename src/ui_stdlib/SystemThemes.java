package ui_stdlib;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.KeyEvent;
import java.text.DecimalFormat;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.KeyStroke;

public class SystemThemes {
	public static final Color BACKGROUND = new Color(39, 47, 50);
	public static final Color MAIN = new Color(157, 189, 198);
	public static final Color WHITE = new Color(255, 255, 255);
	public static final Color HIGHLIGHT = new Color(255, 61, 46);
	public static final Color HIGHLIGHT2 = new Color(255, 176, 59);
	public static final Color HIGHLIGHT3 = new Color(202, 41, 62);
	public static final Color HOVER = new Color(220, 220, 220, 20);
	
	private static final int INSET = 5;
	public static final int HEADER_PADDING = 5;
	
	public static final String COPYRIGHT = "© 2018 Ben Parfitt, Jack Hay, and Oliver Keh";
	
	public static final int DIALOG_WINDOW_WIDTH = 500;
	public static final int DIALOG_WINDOW_HEIGHT = 350;
	
	public static final int MAIN_WINDOW_WIDTH = 1200;
	public static final int MAIN_WINDOW_HEIGHT = 750;
	public static KeyStroke SAVE_BINDING = KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0);
	
	public static GridBagConstraints get_grid_constraints() {
		GridBagConstraints constraints =  new GridBagConstraints();
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.weightx = 1.0;
		constraints.insets = new Insets(INSET, INSET, INSET, INSET);
		return constraints;
	}
	
	public static void button_hover(JButton button) {
		button.addMouseListener(new java.awt.event.MouseAdapter() {
		    public void mouseEntered(java.awt.event.MouseEvent evt) {
		    	button.setBackground(button.getBackground().darker());
		    }

		    public void mouseExited(java.awt.event.MouseEvent evt) {
		    	button.setBackground(button.getBackground().brighter());
		    }
		});
	}
	
	public static JLabel get_copyright() {
		JLabel copyright = new JLabel(SystemThemes.COPYRIGHT);
		copyright.setHorizontalAlignment(JLabel.CENTER);
		return copyright;
	}
	
	public static String get_display_number(Double val, String mask) {
		DecimalFormat df = new DecimalFormat(mask);
		return df.format(val);
	}
	
	public static String get_display_number(Double val) {
		return get_display_number(val, "#.000");
	}
	
	public static JLabel get_default_placeholder() {
		JLabel placeholder = new JLabel("Open a new or saved project");
		placeholder.setHorizontalAlignment(JLabel.CENTER);
		placeholder.setVerticalAlignment(JLabel.CENTER);
		placeholder.setFont(new Font("SansSerif", Font.PLAIN, 20));
		return placeholder;
	}
}
