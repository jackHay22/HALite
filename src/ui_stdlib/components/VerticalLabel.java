package ui_stdlib.components;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import javax.swing.SwingConstants;

@SuppressWarnings("serial")
public class VerticalLabel extends javax.swing.JLabel {
	
	public VerticalLabel(String text) {
		super(text, SwingConstants.LEFT);
	}
	
	@Override
	protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        
        g2d.rotate(Math.toRadians(-90));
        g2d.translate(-this.getHeight(), 0);
        super.paintComponent(g2d);
        
        //inverse
        g2d.rotate(-Math.toRadians(-90));
        g2d.translate(-this.getWidth(), 0);
    }
	
	@Override
	public Dimension getPreferredSize() {
		Dimension d = super.getPreferredSize();
		int width = d.width;
		d.width = d.height;
		d.height = width;
		return d;
	}

	@Override
	public Dimension getMinimumSize() {
		Dimension d = super.getMinimumSize();
		int width = d.width;
		d.width = d.height;
		d.height = width;
		return d;
	}

	@Override
	public Dimension getMaximumSize() {
		Dimension d = super.getMaximumSize();
		int width = d.width;
		d.width = d.height + 10;
		d.height = width + 10;
		return d;
	}
	
	@Override
	public Insets getInsets() {
        Insets insets = super.getInsets();
        int temp = insets.bottom;
        insets.bottom = insets.left;
        insets.left = insets.top;
        insets.top = insets.right;
        insets.right = temp;

        return insets;
    }

	
	@Override
	public int getWidth() {
		return super.getHeight();
    }

	@Override
    public int getHeight() {
		return super.getWidth();
    }
}
