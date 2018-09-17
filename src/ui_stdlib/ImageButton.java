package ui_stdlib;

import java.awt.Image;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.border.Border;

@SuppressWarnings("serial")
public class ImageButton extends JButton {
	public ImageButton(String resource_pos, int size) {
		super();
		try {
			Image button_image = ImageIO.read(getClass().getResourceAsStream(resource_pos))
										.getScaledInstance(size, size, java.awt.Image.SCALE_SMOOTH);			
			this.setIcon(new ImageIcon(button_image));
			Border emptyBorder = BorderFactory.createEmptyBorder();
			this.setBorder(emptyBorder);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
 }
