package ui_stdlib;

import java.awt.Image;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;

@SuppressWarnings("serial")
public class ImageButton extends JButton {
	public ImageButton(String resource, int size) {
		//, String on_click_resource
		super();
		try {
			BufferedImage button_image = ImageIO.read(getClass().getResourceAsStream(resource));
			//button_image_on_click = ImageIO.read(getClass().getResourceAsStream(on_click_resource));
			Image scaled = button_image.getScaledInstance(size, size,  java.awt.Image.SCALE_SMOOTH);
			this.setIcon(new ImageIcon(scaled));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
