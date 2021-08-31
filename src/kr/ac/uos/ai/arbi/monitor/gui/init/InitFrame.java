package kr.ac.uos.ai.arbi.monitor.gui.init;

import java.awt.Dimension;

import javax.swing.JFrame;

import kr.ac.uos.ai.arbi.monitor.RMC;
import kr.ac.uos.ai.arbi.monitor.gui.GUI;


public class InitFrame extends JFrame{

	private static final long serialVersionUID = 8783105731993666010L;
	private InitFramePanel panel;
	
	public InitFrame(InitFramePanel panel) {
		this.panel = panel;
	}
	
	public void init() {
		Dimension screenSize = GUI.getScreenSize();
		int dx = (screenSize.width - 500) /2;
		if (dx < 0)
			dx = 0;
		int dy = (screenSize.height - 400) / 2;
		if (dy < 0)
			dy = 0;
		
		setTitle(RMC.getTitle());
		setDefaultCloseOperation(3);
		setUndecorated(true);
		setBounds(dx, dy, 500, 200);
		
		getContentPane().add(this.panel);
		this.panel.initialize();
	}
	
}
