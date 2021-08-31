package kr.ac.uos.ai.arbi.monitor.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Rectangle;

import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.TableCellRenderer;

public class ColorCellRenderer  implements TableCellRenderer {
	private static final Color NORMAL_COLOR = Color.WHITE;
	private static final Color SELECTED_COLOR = new Color(184, 207, 229);
	private static final Color FOCUSED_COLOR = new Color(99, 130, 191);
	private JButton button;
	private Border normalBorder;
	private Border selectedBorder;
	private Border focusedBorder;

	public ColorCellRenderer() {
		initialize(1);
	}

	public ColorCellRenderer(int borderSize) {
		initialize(borderSize);
	}

	private void initialize(int boderSize) {
		if (boderSize < 0)
			boderSize = 0;

		this.button = new JButton();
		this.normalBorder = new LineBorder(NORMAL_COLOR, boderSize);
		this.selectedBorder = new LineBorder(SELECTED_COLOR, boderSize);

		if (boderSize > 1)
			this.focusedBorder = new CompoundBorder(new LineBorder(FOCUSED_COLOR, 1),
					new LineBorder(SELECTED_COLOR, boderSize - 1));
		else {
			this.focusedBorder = new LineBorder(SELECTED_COLOR, boderSize);
		}

		this.button.setBackground(Color.WHITE);
		this.button.setBorder(this.normalBorder);
	}

	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
			int row, int column) {
		Rectangle rec = table.getCellRect(row, column, false);
		Color color = (Color) value;

		this.button.setBackground(color);
		this.button.setSize(rec.width, rec.height);

		this.button.setBorder(this.normalBorder);
		if (isSelected)
			this.button.setBorder(this.selectedBorder);
		if (hasFocus)
			this.button.setBorder(this.focusedBorder);

		return this.button;
	}
}