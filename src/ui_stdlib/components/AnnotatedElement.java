package ui_stdlib.components;

import system_utils.Element;

public class AnnotatedElement {
	private Element element;
	private String annotation;
	private String sep = "";
	
	public AnnotatedElement(Element element, String annotation, String sep) {
		this.element = element;
		this.annotation = annotation;
		this.sep = sep;
	}
	
	public AnnotatedElement(Element element, String annotation) {
		this.element = element;
		this.annotation = annotation;
	}
	
	public AnnotatedElement(Element element) {
		this.element = element;
		this.annotation = "";
	}
	
	
	public void set_annotation(String annotation) {
		this.annotation = annotation;
	}
	
	@Override
	public String toString() {
		return element + sep + annotation;
	}
	
	public Element get_element() {
		return element;
	}
}
