package kr.ac.uos.ai.arbi.monitor.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.LayoutManager;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SpringLayout;

public class LabeledTextArea extends JComponent {
	private static final long serialVersionUID = 2203101552093565524L;
	private static final int LABEL_HEIGHT = 20;
	private JLabel label;
	private JTextArea textArea;

	public LabeledTextArea(String str) {
		this.label = new JLabel(str);
		this.textArea = new JTextArea();

		initialize();
	}

	public LabeledTextArea(String str, String startText) {
		this.label = new JLabel(str);
		this.textArea = new JTextArea(startText);

		initialize();
	}

	public LabeledTextArea(String str, int labelWidth) {
		this.label = new JLabel(str);
		this.textArea = new JTextArea();

		setLabelWidth(labelWidth);
		initialize();
	}

	public LabeledTextArea(String str, int labelWidth, String startText) {
		this.label = new JLabel(str);
		this.textArea = new JTextArea(startText);

		setLabelWidth(labelWidth);
		initialize();
	}

	private void initialize() {
		SpringLayout layout = new SpringLayout();
		super.setLayout(layout);

		JScrollPane sp = new JScrollPane(this.textArea);

		add(this.label);
		add(sp);

		layout.putConstraint("West", this.label, 0, "West", this);
		layout.putConstraint("VerticalCenter", this.label, 0, "VerticalCenter", this);

		layout.putConstraint("West", sp, 0, "East", this.label);
		layout.putConstraint("North", sp, 0, "North", this);

		layout.putConstraint("East", this, 0, "East", sp);
		layout.putConstraint("South", this, 0, "South", sp);
	}

	public final void setLayout(LayoutManager mgr) {
		super.setLayout(null);
	}

	public void setLabelWidth(int width) {
		if (width < 1)
			width = 1;
		this.label.setPreferredSize(new Dimension(width, 20));
	}

	public String getText() {
		return this.textArea.getText();
	}

	public void setText(String str) {
		this.textArea.setText(str);
	}

	public void setTextAreaColor(Color color) {
		this.textArea.setBackground(color);
	}

	public void setTextAreaEditable(boolean editable) {
		this.textArea.setEditable(editable);
	}

	public void setLabelText(String str) {
		if (str == null)
			return;
		this.label.setText(str);
	}
}