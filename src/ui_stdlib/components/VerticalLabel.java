package ui_stdlib.components;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;

@SuppressWarnings("serial")
public class VerticalLabel extends javax.swing.JLabel {
	private boolean doing_transform;
	
	public VerticalLabel(String text) {
		super(text);
		
		//when in progress
		doing_transform = false;
	}
	
	@Override
	protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        
        //rotate graphics comonent of jlabel
        g2d.rotate(Math.toRadians(-90));
        g2d.translate(-this.getHeight(), 0);
        doing_transform = true;
        
        super.paintComponent(g2d);
        
        doing_transform = false;
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
        if (doing_transform) {
        	int temp = insets.bottom;
            insets.bottom = insets.left;
            insets.left = insets.top;
            insets.top = insets.right;
            insets.right = temp;
        }
        
        return insets;
    }

	
	@Override
	public int getWidth() {
		if (doing_transform) {
			return super.getHeight();
		}
		return super.getWidth();
    }

	@Override
    public int getHeight() {
		if (doing_transform) {
			return super.getWidth();
		}
		return super.getHeight();
    }
}
