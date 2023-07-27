package kr.ac.uos.ai.arbi.monitor.gui;

import java.awt.Dimension;
import java.awt.LayoutManager;
import java.awt.event.ActionListener;

import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.SpringLayout;

public class LabeledComboBox extends JComponent{

	private static final long serialVersionUID = -81520398368185390L;
	private static final int HEIGHT = 20;
	private JLabel label;
	private JComboBox comboBox;
	
	public LabeledComboBox(String str) {
		this.label = new JLabel(str);
		this.comboBox = new JComboBox();
		
		initialize();
	}

	public LabeledComboBox(String str, Object[] items) {
		this.label = new JLabel(str);
		this.comboBox = new JComboBox(items);
		
		initialize();
	}
	
	public LabeledComboBox(String str, int labelWidth) {
		this.label = new JLabel(str);
		this.comboBox = new JComboBox();
		
		setLabelWidth(labelWidth);
		initialize();
	}
	
	public LabeledComboBox(String str, int labelWidth, Object[] items) {
		this.label = new JLabel(str);
		this.comboBox = new JComboBox(items);
		
		setLabelWidth(labelWidth);
		initialize();
	}
	
	private void initialize() {
		SpringLayout layout = new SpringLayout();
		super.setLayout(layout);
		
		this.comboBox.setPreferredSize(new Dimension(this.comboBox.getWidth(), 20));
		
		add(this.label);
		add(this.comboBox);
		
		layout.putConstraint("West", this.label, 0, "West", this);
		layout.putConstraint("VerticalCenter", this.label, 0, "VerticalCenter", this);

		layout.putConstraint("West", this.comboBox, 0, "East", this.label);
		layout.putConstraint("North", this.comboBox, 0, "North", this);

		layout.putConstraint("East", this, 0, "East", this.comboBox);
		layout.putConstraint("South", this, 0, "South", this.comboBox);
	}
	
	public final void setLayout(LayoutManager mgr) {
		super.setLayout(null);
	}
	
	public void setLabelWidth(int width) {
		if(width < 1)
			width = 1;
		
		this.label.setPreferredSize(new Dimension(width, 20));
	}
	
	public void setComboBoxItems(Object[] items) {
		this.comboBox.removeAllItems();
		for (Object item : items)
			this.comboBox.addItem(item);
	}

	public void removeComboBoxItem(Object item) {
		if (item == null)
			return;
		this.comboBox.removeItem(item);
	}

	public Object getSelectedItem() {
		return this.comboBox.getSelectedItem();
	}

	public void addComboBoxActionListener(ActionListener listener) {
		if (listener == null)
			return;
		this.comboBox.addActionListener(listener);
	}

	public void setLabelText(String str) {
		if (str == null)
			return;
		this.label.setText(str);
	}

	public void setComboBoxEnabled(boolean b) {
		this.comboBox.setEnabled(b);
	}
}
