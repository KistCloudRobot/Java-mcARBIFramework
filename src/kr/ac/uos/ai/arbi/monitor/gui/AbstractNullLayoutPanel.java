package kr.ac.uos.ai.arbi.monitor.gui;

import java.awt.Dimension;
import java.awt.LayoutManager;
import java.awt.Rectangle;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

public abstract class AbstractNullLayoutPanel extends JPanel {

	private static final long serialVersionUID = -7178944498849571849L;
	
	public AbstractNullLayoutPanel() {
		super.setLayout(null);
	}
	
	public abstract void initialize();
	
	public void close() {}
	
	protected abstract void setSizeComponent(int paraInt1, int paramInt2);
	
	public final void setLayout(LayoutManager mgr) {
		super.setLayout(null);
	}
	
	public final void setSize(Dimension d) {
		super.setSize(d);
		setSizeComponent(d.width, d.height);
	}
	
	public final void setSize(int width, int height) {
		super.setSize(width, height);
		setSizeComponent(width, height);
	}
	
	public final void setBounds(Rectangle r) {
		super.setBounds(r);
		setSizeComponent(r.width, r.height);
	}
	
	public final void setBounds(int x, int y, int width, int height) {
		super.setBounds(x, y, width, height);
		setSizeComponent(width, height);
	}
	
	public final void setPreferredSize(Dimension preferredSize) {
		super.setPreferredSize(preferredSize);
		setSizeComponent(preferredSize.width, preferredSize.height);
	}
	
	protected final void showWarning(String text) {
		JOptionPane.showMessageDialog(this, text,"Warning", 2);
	}

}
