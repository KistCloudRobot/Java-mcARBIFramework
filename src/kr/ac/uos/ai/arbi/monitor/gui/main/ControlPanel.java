package kr.ac.uos.ai.arbi.monitor.gui.main;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;



public class ControlPanel  extends JPanel {
	private static final long serialVersionUID = -3984527503116080336L;
	private static final int PANEL_HEIGHT = 50;
	private JButton newTesterBtn;
	private JButton exitBtn;

	ControlPanel() {
		this.newTesterBtn = new JButton("New Message Tester");
		this.exitBtn = new JButton("EXIT");
	}

	public void initialize() {
		setPreferredSize(new Dimension(200, 50));
		GridLayout gl = new GridLayout(1, 2);
		gl.setHgap(10);
		gl.setVgap(10);
		setLayout(gl);

		this.newTesterBtn.setActionCommand("NewMessageTester");

		add(this.newTesterBtn);
		add(this.exitBtn);

		this.exitBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
	}

	public void addNewMessageTesterButtonActionListener(ActionListener listener) {
		if (listener == null)
			return;
		this.newTesterBtn.addActionListener(listener);
	}
}