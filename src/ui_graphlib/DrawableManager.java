package ui_graphlib;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;

public interface DrawableManager {
	public void draw_components(Graphics2D g);
	public void handle_mouse_event(MouseEvent e);
}
