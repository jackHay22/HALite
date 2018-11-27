package ui_framework;

@SuppressWarnings("serial")
public abstract class SystemPanel<Backend extends DataBackend> extends javax.swing.JPanel implements Refreshable<Backend> {
	//wrapper on jpanel that enforces refreshable interface
	public SystemPanel() {
		super();
	}

	public void set_minimum_dimension(int width, int height) {
		super.setMinimumSize(new java.awt.Dimension(width, height));
	}
}
