package ui_stdlib;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.Insets;

public class SystemThemes {
	public static Color DARK = new Color(34, 33, 48);
	public static Color MAIN = new Color(70, 77, 87);
	public static Color OFF_WHITE = new Color(212, 232, 211);
	public static Color WHITE = new Color(255, 255, 255);
	public static Color HIGHLIGHT = new Color(237, 137, 23);
	
	private static final int INSET = 5;
	
	public static GridBagConstraints get_grid_constraints() {
		GridBagConstraints constraints =  new GridBagConstraints();
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.weightx = 1.0;
		constraints.insets = new Insets(INSET, INSET, INSET, INSET);
		return constraints;
	}
}
