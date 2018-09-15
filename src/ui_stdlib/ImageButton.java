package ui_stdlib;

import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;
import javax.swing.JButton;

public class ImageButton {
	private BufferedImage button_image;
	private BufferedImage button_image_on_click;
	private JButton button;
	
	public ImageButton(String resource, String on_click_resource) {
		
		button = new JButton();
		try {
			button_image = ImageIO.read(getClass().getResourceAsStream(resource));
			button_image_on_click = ImageIO.read(getClass().getResourceAsStream(on_click_resource));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
