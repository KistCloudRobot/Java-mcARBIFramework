/*** Eclipse Class Decompiler plugin, copyright (c) 2016 Chen Chao (cnfree2000@hotmail.com) ***/
package kr.ac.uos.ai.arbi.monitor.gui.main.sequence;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JComponent;

import kr.ac.uos.ai.arbi.monitor.Utility;
import kr.ac.uos.ai.arbi.monitor.model.AbstractListModel;
import kr.ac.uos.ai.arbi.monitor.model.Message;
import kr.ac.uos.ai.arbi.monitor.model.SequenceColorBean;
import kr.ac.uos.ai.arbi.monitor.model.SequenceColorTableModel;
import kr.ac.uos.ai.arbi.monitor.model.URLNameTableModel;


class SequenceComponent extends JComponent implements MouseListener {
	private static final long serialVersionUID = -2636953760334199713L;
	private static final int FONT_WIDTH = 2;
	private static final int DEFALUT_BOX_WIDTH = 50;
	private static final int DEFALUT_H_GAP = 40;
	private static final int DEFALUT_V_GAP = 40;
	private URLNameTableModel nameModel;
	private AbstractListModel<? extends Message> sequenceModel;
	private SequenceColorTableModel lineColorModel;
	private int horizonGap;
	private int verticalGap;
	private int boxWidth;

	SequenceComponent(URLNameTableModel nameModel, AbstractListModel<? extends Message> sequenceModel,
			SequenceColorTableModel lineColorModel) {
		this.nameModel = nameModel;
		this.sequenceModel = sequenceModel;
		this.lineColorModel = lineColorModel;
		this.boxWidth = 50;
		this.horizonGap = 40;
		this.verticalGap = 40;
	}

	public void initialize(int bWidth, int hGap, int vGap) {
		this.boxWidth = bWidth;
		this.horizonGap = hGap;
		this.verticalGap = vGap;

		setDoubleBuffered(true);
		addMouseListener(this);
	}

	int getHorizonGap() {
		return this.horizonGap;
	}

	int getVerticalGap() {
		return this.verticalGap;
	}

	int getBoxWidth() {
		return this.boxWidth;
	}

	private final void drawArrowLine(Graphics g, Color color, int x1, int x2, int y) {
		g.setColor(color);

		if (x1 == x2) {
			int lWidth = getHorizonGap() / 3;
			int lHeight = getVerticalGap() / 3;

			g.drawLine(x1, y, x1 + lWidth, y);
			g.drawLine(x1 + lWidth, y, x1 + lWidth, y + lHeight);
			g.drawLine(x1, y + lHeight, x1 + lWidth, y + lHeight);
			g.drawLine(x1, y + lHeight, x1 + 5, y + lHeight - 5);
			g.drawLine(x1, y + lHeight, x1 + 5, y + lHeight + 5);
		} else {
			g.drawLine(x1, y, x2, y);
			if (x1 < x2) {
				g.drawLine(x2 - 5, y - 5, x2, y);
				g.drawLine(x2 - 5, y + 5, x2, y);
			} else {
				g.drawLine(x2 + 5, y - 5, x2, y);
				g.drawLine(x2 + 5, y + 5, x2, y);
			}
		}
	}

	private final String splitString(String str, int size) {
		if (Utility.isNullString(str))
			return "";
		if (size <= 0)
			return str;
		String result = str;
		if (str.length() > size)
			result = str.substring(0, size) + " ...";
		return result;
	}

	public void paint(Graphics g) {
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, getWidth(), getHeight());

		g.setColor(Color.LIGHT_GRAY);
		int nSize = this.nameModel.getRowCount();
		int ix = getBoxWidth() / 2;
		for (int i = 0; i < nSize; ++i) {
			int x = ix + (getHorizonGap() + getBoxWidth()) * i;
			g.drawLine(x, 0, x, getHeight());
		}

		Message[] messages = (Message[]) this.sequenceModel.toArray(Message.NULL_ARRAY);
		for (int i = 0; i < messages.length; ++i) {
			String type = messages[i].getType().toString();
			String from = messages[i].getFrom();
			String to = messages[i].getTo();
			String[] content = messages[i].getContent();

			int fIndex = this.nameModel.indexOfKey(from);
			int tIndex = this.nameModel.indexOfKey(to);

			if (fIndex < 0)
				continue;
			if (tIndex < 0)
				continue;
			int y = getVerticalGap() * (i + 1);
			int x1 = getBoxWidth() / 2 + (getHorizonGap() + getBoxWidth()) * fIndex;
			int x2 = getBoxWidth() / 2 + (getHorizonGap() + getBoxWidth()) * tIndex;

			SequenceColorBean bean = (SequenceColorBean) this.lineColorModel.getRow(messages[i].getType());
			drawArrowLine(g, bean.getLineColor(), x1, x2, y);

			g.setColor(bean.getFontColor());
			int strSize = (int) Math.ceil(Math.abs(fIndex - tIndex) * getHorizonGap() / 2) - 1;

			int cX = x1;
			if (x1 >= x2)
				cX = x2;

			if (content.length > 0)
				type = type + ":";
			g.drawString(type, cX + 10, y - (5 + 15 * content.length));

			int j = content.length - 1;
			for (String s : content) {
				String str = splitString(s, strSize * 2);
				if (j < content.length - 1)
					str = "    " + str;
				g.drawString(str, cX + 10, y - (5 + 15 * j));
				--j;
			}
		}
	}

	public void mouseClicked(MouseEvent e) {
		if (e.getModifiers() != 16)
			return;

		int index = e.getY() / getVerticalGap();
		Message message = (Message) this.sequenceModel.get(index);
		if (message == null)
			return;

		int y2 = getVerticalGap() + index * getVerticalGap();
		int y1 = y2 - (5 + 15 * (message.getContent().length + 1));

		int fIndex = this.nameModel.indexOfKey(message.getFrom());
		int tIndex = this.nameModel.indexOfKey(message.getTo());

		int x1 = getBoxWidth() / 2 + (getHorizonGap() + getBoxWidth()) * fIndex;
		int x2 = getBoxWidth() / 2 + (getHorizonGap() + getBoxWidth()) * tIndex;

		if (x1 > x2) {
			int t = x1;
			x1 = x2;
			x2 = t;
		}

		if ((x1 <= e.getX()) && (e.getX() <= x2) && (y1 <= e.getY()) && (e.getY() <= y2)) {
			MessageViewDialog d = new MessageViewDialog(message);
			int dx = e.getXOnScreen() - 50;
			int dy = e.getYOnScreen() - 50;
			if (dx < 0)
				dx = 0;
			if (dy < 0)
				dy = 0;
			d.setLocation(dx, dy);
			d.setVisible(true);
			System.out.println(message.toString());
		}
	}

	public void mouseEntered(MouseEvent e) {
	}

	public void mouseExited(MouseEvent e) {
	}

	public void mousePressed(MouseEvent e) {
	}

	public void mouseReleased(MouseEvent e) {
	}
}