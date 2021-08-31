package kr.ac.uos.ai.arbi.monitor.gui.main.urlname;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.border.TitledBorder;

import kr.ac.uos.ai.arbi.monitor.gui.AbstractNullLayoutPanel;



public class ColorChoosePanel  extends AbstractNullLayoutPanel {
	private static final long serialVersionUID = -3350347372757176626L;
	private Color defaultColor;
	private String title;
	private JButton button;
	private Color currentColor;

	ColorChoosePanel(String title, Color defaultColor) {
		this.title = title;
		this.defaultColor = defaultColor;
		this.button = new JButton();
		this.currentColor = defaultColor;
	}

	public void initialize() {
		setBorder(new TitledBorder(this.title));
		this.button.setBackground(this.defaultColor);
		this.button.setBounds(10, 20, 20, 20);
		add(this.button);

		this.button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ColorChoosePanel.this.chooseColor();
			}
		});
	}

	protected void setSizeComponent(int width, int height) {
	}

	public Color getColor() {
		return this.currentColor;
	}

	public String getColorString() {
		return String.format("#%H%H%H", new Object[] { Integer.valueOf(getColor().getRed()),
				Integer.valueOf(getColor().getGreen()), Integer.valueOf(getColor().getBlue()) });
	}

	public void reset() {
		this.currentColor = this.defaultColor;
		this.button.setBackground(this.currentColor);
	}

	private void chooseColor() {
		Color color = JColorChooser.showDialog(this, this.title + " Color", this.defaultColor);
		if (color != null) {
			this.currentColor = color;
			this.button.setBackground(this.currentColor);
		}
	}
}