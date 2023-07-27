package kr.ac.uos.ai.arbi.monitor.gui.main;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;

public class MenuActionListener implements ActionListener {
	private JFrame frame;
	private AboutDialog dialog;

	MenuActionListener(JFrame frame) {
		this.frame = frame;
		this.dialog = new AboutDialog(this.frame);
		this.dialog.init();
	}

	public void actionPerformed(ActionEvent e) {
		String str = e.getActionCommand();
		MenuType type = MenuType.valueOf(str);

		if (type == MenuType.EXIT)
			System.exit(0);
		else if (type == MenuType.ABOUT)
			this.dialog.setVisible(true);
	}

	public static enum MenuType {
		EXIT, ABOUT;
	}
}