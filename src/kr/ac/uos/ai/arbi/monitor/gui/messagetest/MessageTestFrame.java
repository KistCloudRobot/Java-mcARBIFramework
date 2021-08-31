package kr.ac.uos.ai.arbi.monitor.gui.messagetest;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import kr.ac.uos.ai.arbi.monitor.gui.AbstractNullLayoutPanel;
import kr.ac.uos.ai.arbi.monitor.gui.GUI;



public class MessageTestFrame extends JFrame{

	private static final long serialVersionUID = -2187901354012742097L;
	private static final String TITLE = "Message Tester";
	private static final int FRAME_WIDTH = 700;
	private static final int FRAME_HEIGHT = 600;
	private static final ExecutorService threadPool = Executors.newCachedThreadPool();
	private JTabbedPane tabbedPane;
	private AbstractNullLayoutPanel aaMessageTestPanel;
	private AbstractNullLayoutPanel ltmMessageTestPanel;
	private JFrame owner;
	
	public static ExecutorService getThreadPool() {
		return threadPool;
	}

	public MessageTestFrame(String brokerURL) {
		super("Message Tester");
		this.owner = null;
		initialize(brokerURL);
	}
	
	public MessageTestFrame( String brokerURL, JFrame frame) {
		super("Message Tester");
		this.owner = frame;
		initialize(brokerURL);
	}
	

	private void initialize(String brokerURL) {
		this.tabbedPane = new JTabbedPane();
		this.aaMessageTestPanel = new AAMessageTestPanel(brokerURL);
		this.ltmMessageTestPanel = new LTMMessageTestPanel(brokerURL);

		setDefaultCloseOperation(2);
		setSize(700, 600);

		Dimension screenSize = GUI.getScreenSize();
		int x = (screenSize.width - 700) / 2;
		if (x < 0)
			x = 0;
		int y = (screenSize.height - 600) / 2;
		if (y < 0)
			y = 0;
		setLocation(x, y);

		getContentPane().add(this.tabbedPane, "Center");

		if (this.aaMessageTestPanel != null) {
			this.aaMessageTestPanel.initialize();
			this.tabbedPane.add(this.aaMessageTestPanel, "AA Message Test");
		}
		if (this.ltmMessageTestPanel != null) {
			this.ltmMessageTestPanel.initialize();
			this.tabbedPane.add(this.ltmMessageTestPanel, "LTM Message Test");
		}

		initializeButtonPanel();
	}
	

	private void initializeButtonPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new FlowLayout(1));

		JButton okButton = new JButton("OK");
		okButton.setSize(300, 50);

		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				MessageTestFrame.this.setVisible(false);
				MessageTestFrame.this.dispose();
			}
		});
		panel.add(okButton);

		getContentPane().add(panel, "South");
	}
	
	public void dispose() {
		if (this.owner == null)
			System.exit(0);
		super.dispose();
	}

	public void setTitle(String title) {
		super.setTitle("Message Tester");
	}



}
