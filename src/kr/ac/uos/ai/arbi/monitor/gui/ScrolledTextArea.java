package kr.ac.uos.ai.arbi.monitor.gui;

import java.awt.Color;

import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class ScrolledTextArea  extends JScrollPane {
	private static final long serialVersionUID = -8236775609224001920L;
	private JTextArea textArea;

	public ScrolledTextArea() {
		super(20, 30);

		this.textArea = new JTextArea();
		setViewportView(this.textArea);
	}

	public String getText() {
		return this.textArea.getText();
	}

	public void setText(String text) {
		this.textArea.setText(text);
	}

	public void setEditable(boolean editable) {
		this.textArea.setEditable(editable);
	}

	public void setBackground(Color bg) {
		if (this.textArea != null)
			this.textArea.setBackground(bg);
		super.setBackground(bg);
	}
}