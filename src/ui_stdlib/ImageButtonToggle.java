package ui_stdlib;

import java.awt.Image;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.border.Border;

@SuppressWarnings("serial")
public class ImageButtonToggle extends JButton {
	//true: plus, false: minus
	private boolean state;
	private ImageIcon image_pos_icon;
	private ImageIcon image_neg_icon;
	public ImageButtonToggle(String resource_pos, String resource_neg, int size) {
		//, String on_click_resource
		super();
		this.state  = true;
		try {
			Image button_image = ImageIO.read(getClass().getResourceAsStream(resource_pos))
										.getScaledInstance(size, size,  java.awt.Image.SCALE_SMOOTH);
			Image button_image_neg = ImageIO.read(getClass().getResourceAsStream(resource_neg))
											.getScaledInstance(size, size,  java.awt.Image.SCALE_SMOOTH);
			
			this.image_pos_icon = new ImageIcon(button_image);
			this.image_neg_icon = new ImageIcon(button_image_neg);
			
			this.setIcon(image_pos_icon);
			Border emptyBorder = BorderFactory.createEmptyBorder();
			this.setBorder(emptyBorder);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void toggle() {
		this.state = ! this.state;
		if (this.state) {
			this.setIcon(this.image_pos_icon);
		} else {
			this.setIcon(this.image_neg_icon);
		}
	}
 }
