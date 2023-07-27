package kr.ac.uos.ai.arbi.monitor.gui;

import java.awt.Graphics;
import java.awt.Image;

import javax.swing.JComponent;

public class ImageComponent extends JComponent {

	private static final long serialVersionUID = -3987601073475967553L;
	private String imgName;
	private Image img;

	public ImageComponent(String imgName) {
		this.imgName = imgName;
		this.img = ImageFactory.getInstance().newImage(this.imgName);
	}

	public void paint(Graphics g) {
		if (this.img == null)
			return;

		int sW = this.img.getWidth(this);
		int sH = this.img.getHeight(this);

		g.drawImage(this.img, 0, 0, getWidth(), getHeight(), 0, 0, sW, sH, this);
	}

}
