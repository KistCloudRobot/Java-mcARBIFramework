/*** Eclipse Class Decompiler plugin, copyright (c) 2016 Chen Chao (cnfree2000@hotmail.com) ***/
package kr.ac.uos.ai.arbi.monitor.gui.main.sequence;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import javax.swing.JComponent;

import kr.ac.uos.ai.arbi.monitor.model.URLNameBean;
import kr.ac.uos.ai.arbi.monitor.model.URLNameTableModel;


class NameComponent extends JComponent {
	private static final long serialVersionUID = -1578642461646850257L;
	private static final Font DEFAULT_FONT = new Font("", 1, 13);
	private static final int DEFALUT_WIDTH_GAP = 30;
	private static final int DEFALUT_BOX_WIDTH = 50;
	private static final int DEFALUT_BOX_HEIGHT = 30;
	private int horizonGap;
	private int boxWidth;
	private int boxHeight;
	private URLNameTableModel model;

	NameComponent(URLNameTableModel model) {
		this.model = model;
		this.horizonGap = 30;
		this.boxWidth = 50;
		this.boxHeight = 30;
	}

	public void initialize() {
		setDoubleBuffered(true);
	}

	void setHorizonGap(int gap) {
		if (gap > 30)
			this.horizonGap = gap;
		else
			this.horizonGap = 30;
	}

	void setBoxSize(int width, int height) {
		if (width > 50)
			this.boxWidth = width;
		else
			this.boxWidth = 50;

		if (height > 30)
			this.boxHeight = height;
		else
			this.boxHeight = 30;
	}

	int getHorizonGap() {
		return this.horizonGap;
	}

	int getBoxWidth() {
		return this.boxWidth;
	}

	int getBoxHeight() {
		return this.boxHeight;
	}

	public void paint(Graphics g) {
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, getWidth(), getHeight());

		g.setFont(DEFAULT_FONT);

		int y = 0;
		URLNameBean[] nameList = (URLNameBean[]) this.model.toArray(URLNameBean.NULL_ARRAY);
		for (int i = 0; i < nameList.length; ++i) {
			String name = nameList[i].getName();
			Color fontColor = nameList[i].getFontColor();
			Color bgColor = nameList[i].getBgColor();

			if (fontColor == null)
				fontColor = Color.BLACK;
			if (bgColor == null)
				bgColor = Color.getHSBColor(60.0F, 27.0F, 100.0F);

			int x = this.boxWidth * i + this.horizonGap * i;
			g.setColor(Color.RED);
			g.drawRect(x, y, this.boxWidth, this.boxHeight);

			g.setColor(bgColor);
			g.fillRect(x + 1, y + 1, this.boxWidth - 1, this.boxHeight - 1);

			g.setColor(fontColor);
			int t = (this.boxWidth - (name.length() * 9)) / 2;
			if (t < 3)
				t = 3;

			g.drawString(name, x + t, y + 20);
		}
	}
}