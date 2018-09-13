package ui_graphlib;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;
import system_utils.DataStore;
import ui_framework.Refreshable;
import ui_framework.SystemPanel;

@SuppressWarnings("serial")
public class DrawablePanel extends JPanel implements MouseListener, Refreshable{
	private int height;
	private int width;
	private DrawableManager manager;
	
	public DrawablePanel(DrawableManager manager, int width, int height) {
		super();
		this.width = width;
		this.height = height;
		this.manager = manager;
		setPreferredSize(new Dimension(width, height));
		setFocusable(true);
	}
	
	@Override
	public void paintComponent(Graphics g) { 
		manager.draw_components(g);
        super.paintComponent(g);
    }

	@Override
	public void set_datastore(DataStore datastore) {
	}

	@Override
	public void add_refreshable(Refreshable refreshable_component) { }
	
	@Override
	public void mouseClicked(MouseEvent e) {
		manager.handle_mouse_event(e);
		
	}
	
	@Override
	public void mousePressed(MouseEvent e) {
		manager.handle_mouse_event(e);	
	}
	
	@Override
	public void mouseReleased(MouseEvent e) {}
	
	@Override
	public void mouseEntered(MouseEvent e) {}
	
	@Override
	public void mouseExited(MouseEvent e) {}

	@Override
	public void refresh() {
		this.repaint();
	}
}
