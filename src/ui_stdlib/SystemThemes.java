package ui_stdlib;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.text.DecimalFormat;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.text.html.StyleSheet;

import system_utils.Element;

public class SystemThemes {
	public static final Color BACKGROUND = new Color(245, 245, 245); //new Color(39, 47, 50);
	public static final Color MAIN = new Color(255, 255, 255);//new Color(157, 189, 198);
	public static final Color WHITE = new Color(0, 255, 0);
	public static final Color HIGHLIGHT = new Color(255, 61, 46);
	public static final Color LOWLIGHT = new Color(100, 100, 119);
	public static final Color HIGHLIGHT2 = new Color(20, 20, 20);//= new Color(255, 176, 59);
	public static final Color HIGHLIGHT3 = new Color(202, 41, 62);
	public static final Color HOVER = new Color(220, 220, 220, 20);
	
	public static final int INSET = 5;
	public static final int HEADER_PADDING = 5;
	
	public static final String COPYRIGHT = "© 2018 Ben Parfitt, Jack Hay, and Oliver Keh";
	
	public static final int DIALOG_WINDOW_WIDTH = 500;
	public static final int DIALOG_WINDOW_HEIGHT = 350;
	
	public static final int MAIN_WINDOW_WIDTH = 1200;
	public static final int MAIN_WINDOW_HEIGHT = 750;
	
	public static final int SCROLL_PANE_SPEED = 8;
	
	public static final int TOTAL_ELEMENTS = Element.values().length;
	
	public static final float LARGE_TEXT_FONT_SIZE = 16.0f;
	
	//FILE EXPORT TYPES
	public static final int PDF_RESPONSE_GRAPHS = 0;
	public static final int PDF_CALIBRATION_GRAPHS = 1;
	public static final int CSV_MODEL_DATA = 2;
	public static final int CSV_FULL_REPORT = 3;
	public static final int CSV_DRIFT_CORRECTION = 4;
	
	public static boolean TRUNCATE_STDS_VALS = false;
	
	private static final String[] HELP_MARKUP_RULES = {
			"body { margin-left: 5px; }",
			"h1 {color: rgb(39,47,50); font-weight: 100; text-align: center; }",
			"h2 {color: rgb(157,189,198); border-bottom: 2px solid rgb(157,189,198); }",
			"h3 { border-bottom: 1px solid rgb(157,189,198); }",
			".section { margin-left: 5px;}",
			"code { font-family: \"SFMono-Regular\",Consolas,\"Liberation Mono\",Menlo,Courier,monospace;\n" + 
			"    background-color: #EBEBEB;\n" + 
			"    border-radius: 3px;\n" + 
			"    font-size: 80%;\n" + 
			"    padding: 0.3em 0.5em;\n" + 
			"    margin: 0;\n" + 
			"}",
			".list { margin-left: 10px; margin-bottom: 10px; line-height: 1.4; }",
			"footer p {text-align: center; color: rgb(157,189,198); }"
	};
	
	// This is the list of all elements to show in the r2 correlation area
	public static Element[] use_for_r2 = new Element[] {Element.Si, 
			Element.Al, Element.Ti, Element.Fe, 
			Element.Mn, Element.Mg, Element.Ca, Element.Na, 
			Element.K, Element.P, Element.Ba,Element.Co, Element.Cr, 
			Element.Cu, Element.Ga, Element.Hf, Element.Nb, Element.Ni, 
			Element.Rb, Element.Sc, Element.Sr, Element.V, Element.W, 
			Element.Y, Element.Zn, Element.Zr, Element.La, Element.Ce, 
			Element.Nd, Element.Sm, Element.Dy};
	
	public static GridBagConstraints get_grid_constraints() {
		GridBagConstraints constraints =  new GridBagConstraints();
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.weightx = 1.0;
		constraints.insets = new Insets(INSET, INSET, INSET, INSET);
		return constraints;
	}
	
	public static void button_hover(JButton button) {
		//this fixes windows button background bug and removes default button look and feel on windows.
		button.setBackground(button.getBackground().darker().brighter());
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
		if (val == null) {
			return "N/A";
		}
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
	
	public static JScrollPane get_scrollable_panel(Component panel) {
		JScrollPane pane = new JScrollPane(panel, 
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		pane.getVerticalScrollBar().setUnitIncrement(SCROLL_PANE_SPEED);
		pane.setMinimumSize(new Dimension(200, 600));
		return pane;
	}
	
	public static void set_help_markup_rules(StyleSheet style_sheet) {
		for (String r : HELP_MARKUP_RULES) {
			style_sheet.addRule(r);
		}
	}
	
	public static JScrollPane get_horiz_scrollable_panel(Component panel) {
		JScrollPane pane = new JScrollPane(panel, 
                JScrollPane.VERTICAL_SCROLLBAR_NEVER,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		pane.getHorizontalScrollBar().setUnitIncrement(SCROLL_PANE_SPEED);
		Dimension panel_size = panel.getMinimumSize();
		pane.setMinimumSize(new Dimension(panel_size.width, panel_size.height + 20));
		pane.setBackground(MAIN);
		pane.setBorder(null);
		pane.setOpaque(false);
		return pane;
	}
	
	public static Dimension get_std_cell_dim() {
		return new Dimension(40,30);
	}
	
	public static boolean valid_csv(String file_path) {
		return file_path.endsWith(".csv");
	}
	
	public static String superscript(String str) {
	    str = str.replaceAll("0", "⁰");
	    str = str.replaceAll("1", "¹");
	    str = str.replaceAll("2", "²");
	    str = str.replaceAll("3", "³");
	    str = str.replaceAll("4", "⁴");
	    str = str.replaceAll("5", "⁵");
	    str = str.replaceAll("6", "⁶");
	    str = str.replaceAll("7", "⁷");
	    str = str.replaceAll("8", "⁸");
	    str = str.replaceAll("9", "⁹");         
	    return str;
	}
}
