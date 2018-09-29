package ui_stdlib.components;

import java.awt.Dimension;
import java.awt.Image;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JRadioButton;
import javax.swing.border.Border;

@SuppressWarnings("serial")
public class ImageRadioButton extends JRadioButton {
	public ImageRadioButton(String resource) {
		super();
		//TODO
		//create_button(resource, 50, 10);
	}
	private void create_button(String resource_pos, int width, int height) {
		try {
			Image button_image = ImageIO.read(getClass().getResourceAsStream(resource_pos))
										.getScaledInstance(width, height, java.awt.Image.SCALE_SMOOTH);		
			this.setPreferredSize(new Dimension(width, height));
			this.setIcon(new ImageIcon(button_image));
			Border emptyBorder = BorderFactory.createEmptyBorder();
			this.setBorder(emptyBorder);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
