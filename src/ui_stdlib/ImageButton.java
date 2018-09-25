package ui_stdlib;

import java.awt.Dimension;
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
		create_button(resource_pos, size, size);
	}
	public ImageButton(String resource_pos, int width, int height) {
		super();
		create_button(resource_pos, width, height);
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
	public void set_text(String text) {
		this.setText(text);
	}
 }
