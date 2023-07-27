package kr.ac.uos.ai.arbi.monitor.gui;

import java.awt.Dimension;
import java.awt.LayoutManager;
import java.awt.Rectangle;

import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

public class TitledTextField  extends JPanel {
	private static final long serialVersionUID = 2370093417525127190L;
	private JTextField textField;

	public TitledTextField(String title) {
		TitledBorder border = new TitledBorder(title);
		setBorder(border);

		this.textField = new JTextField();
		this.textField.setLocation(10, 20);
		add(this.textField);
	}

	public void setLayout(LayoutManager mgr) {
		super.setLayout(null);
	}

	public void setSize(Dimension d) {
		super.setSize(d);
		setSizeTextField(d.width, d.height);
	}

	public void setSize(int width, int height) {
		super.setSize(width, height);
		setSizeTextField(width, height);
	}

	public void setBounds(Rectangle r) {
		super.setBounds(r);
		setSizeTextField(r.width, r.height);
	}

	public void setBounds(int x, int y, int width, int height) {
		super.setBounds(x, y, width, height);
		setSizeTextField(width, height);
	}

	public void setPreferredSize(Dimension preferredSize) {
		super.setPreferredSize(preferredSize);
		setSizeTextField(preferredSize.width, preferredSize.height);
	}

	private void setSizeTextField(int width, int height) {
		this.textField.setSize(width - 20, height - 30);
	}

	public String getText() {
		return this.textField.getText();
	}

	public void setText(String text) {
		this.textField.setText(text);
	}

	public void clear() {
		this.textField.setText("");
	}
}