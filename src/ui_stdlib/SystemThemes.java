package ui_stdlib;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.Insets;

public class SystemThemes {
	public static Color BACKGROUND = new Color(39, 47, 50);
	public static Color MAIN = new Color(157, 189, 198);
	public static Color WHITE = new Color(255, 255, 255);
	public static Color HIGHLIGHT = new Color(255, 61, 46);
	
	private static final int INSET = 5;
	public static final int HEADER_PADDING = 5;
	
	public static GridBagConstraints get_grid_constraints() {
		GridBagConstraints constraints =  new GridBagConstraints();
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.weightx = 1.0;
		constraints.insets = new Insets(INSET, INSET, INSET, INSET);
		return constraints;
	}
}
