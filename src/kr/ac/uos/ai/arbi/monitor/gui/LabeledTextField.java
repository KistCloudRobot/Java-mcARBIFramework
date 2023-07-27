package kr.ac.uos.ai.arbi.monitor.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.LayoutManager;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SpringLayout;

public class LabeledTextField extends JComponent{

	private static final long serialVersionUID = -1847535090007705029L;
	private static final int HEIGHT = 20;
	private JLabel label;
	private JTextField textField;
	
	public LabeledTextField(String str) {
		this.label = new JLabel(str);
		this.textField = new JTextField();
		
		initialize();
	}
	
	public LabeledTextField(String str, String startText) {
		this.label = new JLabel(str);
		this.textField = new JTextField(startText);
		
		initialize();
	}
	
	public LabeledTextField(String str, int labelWidth) {
		this.label = new JLabel(str);
		this.textField = new JTextField();
		
		setLabelWidth(labelWidth);
		initialize();
	}
	
	public LabeledTextField(String str, int labelWidth, String startText) {
		this.label = new JLabel(str);
		this.textField = new JTextField(startText);
		
		setLabelWidth(labelWidth);
		initialize();
	}
	
	private void initialize() {
		SpringLayout layout = new SpringLayout();
		super.setLayout(layout);
		
		this.textField.setPreferredSize(new Dimension(this.textField.getWidth(), 20));
		
		add(this.label);
		add(this.textField);
		
		layout.putConstraint("West", this.label, 0, "West", this);
		layout.putConstraint("VerticalCenter", this.label, 0, "VerticalCenter", this);
		
		layout.putConstraint("West",  this.textField, 0, "East", this.label);
		layout.putConstraint("North", this.textField, 0, "North", this);
		
		layout.putConstraint("East", this, 0, "East", this.textField);
		layout.putConstraint("South", this, 0, "South", this.textField);
	}
	
	public final void setLayout(LayoutManager mgr) {
		super.setLayout(null);
	}
	
	public void setLabelWidth(int width) {
		if(width < 1)
			width = 1;
		this.label.setPreferredSize(new Dimension(width, 20));
	}
	
	public String getText() {
		return this.textField.getText();
	}
	
	public void setText(String str) {
		this.textField.setText(str);
	}
	
	public void setTextFieldColor(Color color) {
		this.textField.setBackground(color);
	}
	
	public void setTextFieldEditable(boolean editable) {
		this.textField.setEditable(editable);
	}
	
	public void setLabelWidth(String str) {
		if(str == null)
			return;
		this.label.setText(str);
	}
}
