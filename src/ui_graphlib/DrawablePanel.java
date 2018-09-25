package ui_graphlib;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import system_utils.DataStore;
import ui_framework.Refreshable;
import ui_stdlib.SystemThemes;

@SuppressWarnings("serial")
public class DrawablePanel extends JPanel implements MouseListener, Refreshable{
	private DrawableManager manager;
	
	public DrawablePanel(DrawableManager manager, int width, int height) {
		super();
		//this.setMinimumSize(new Dimension(width, height));
		this.manager = manager;
		this.addMouseListener(this);
		this.setBorder(BorderFactory.createLineBorder(SystemThemes.MAIN, 2, true));
	}
	
	@Override
	public void paintComponent(Graphics g) { 
		super.paintComponent(g);
		Graphics2D g2 =(Graphics2D)g;
		manager.draw_components(g2);   
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
		
	}
	
	public Dimension get_drawable_size() {
		return this.getSize();
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

	@Override
	public void on_start() {
	}
}
