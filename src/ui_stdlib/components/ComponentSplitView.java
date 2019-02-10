package ui_stdlib.components;

import java.awt.Component;
import java.awt.GridLayout;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class ComponentSplitView extends JPanel {	
	public ComponentSplitView(Component top, Component bottom) {	
		//set layout to vertical two panels
		this.setLayout(new GridLayout(2,0));
		add(top);
		add(bottom);
	}
}
