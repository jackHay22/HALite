package ui_framework;

public interface Resizeable {
	int get_width();
	int get_height();
	void set_dimension(java.awt.Dimension dim);
	void set_minimum_dimension(java.awt.Dimension dim);
}
