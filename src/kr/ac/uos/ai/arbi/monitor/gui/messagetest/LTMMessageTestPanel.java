package kr.ac.uos.ai.arbi.monitor.gui.messagetest;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.ExecutorService;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.TitledBorder;

import kr.ac.uos.ai.arbi.ServerLauncher;
import kr.ac.uos.ai.arbi.ltm.DataSource;
import kr.ac.uos.ai.arbi.model.GLFactory;
import kr.ac.uos.ai.arbi.model.GeneralizedList;
import kr.ac.uos.ai.arbi.model.parser.ParseException;
import kr.ac.uos.ai.arbi.monitor.Utility;
import kr.ac.uos.ai.arbi.monitor.gui.AbstractNullLayoutPanel;
import kr.ac.uos.ai.arbi.monitor.gui.LabeledComboBox;
import kr.ac.uos.ai.arbi.monitor.gui.LabeledTextArea;
import kr.ac.uos.ai.arbi.monitor.gui.LabeledTextField;
import kr.ac.uos.ai.arbi.monitor.gui.ScrolledTextArea;
import kr.ac.uos.ai.arbi.monitor.model.LTMMessage.LTMMessageAction;

public class LTMMessageTestPanel extends AbstractNullLayoutPanel {

	private static final long serialVersionUID = 1806998279854038155L;
	private static final int LABEL_WIDTH = 90;
	private static final int BUTTON_WIDTH = 100;
	private static final int COMBOBOX_WIDTH = 110;
	private static final String DEFAULT_JMS_URL = "tcp://localhost:61616";
	private static final String DEFAULT_SENDER_URL = "dc:testdc1";
	private static final String DEFAULT_AGENT_URL = "tcp://localhost:61616";

	private JPanel senderPanel;
	private LabeledTextField jmsURLTF;
	private LabeledTextField senderURLTF;
	private LabeledTextField arbiURLTF;
	private JButton connectBtn;
	private JPanel receiverPanel;
	private LabeledComboBox typeCB;
	private LabeledTextArea messageTA;
	private JButton sendBtn;

	// private LabeledTextArea newGLTA;
	// private LabeledComboBox performativeCB;
	// private LabeledTextField receiverTF;

	private JPanel sendPanel;
	private ScrolledTextArea sendTA;
	private JPanel receivePanel;
	private ScrolledTextArea receiveTA;
	private DataSource ds;
	private ExecutorService threadPool;

	LTMMessageTestPanel(String brokerURL) {

		this.senderPanel = new JPanel();
		this.senderPanel = new JPanel();
		this.jmsURLTF = new LabeledTextField("JMS URL: ", 90, "tcp://localhost:61616");
		this.senderURLTF = new LabeledTextField("Sender URL: ", 90, "dc:testdc1");
		this.arbiURLTF = new LabeledTextField("ARBI URL: ", 90, "tcp://localhost:61616");

		this.connectBtn = new JButton("Connect");

		this.receiverPanel = new JPanel();
		this.typeCB = new LabeledComboBox("Type: ", 90, LTMMessageAction.values());
		this.messageTA = new LabeledTextArea("Message: ", 90);

		this.sendBtn = new JButton("Send");

		// this.newGLTA = new LabeledTextArea("New GL: ", 90);
		// this.performativeCB = new LabeledComboBox("PerformativeL ", 90,
		// LTMMessageAction.values());
		// this.receiverTF = new LabeledTextField("Receiver URL: ", 90);

		this.sendPanel = new JPanel();
		this.sendTA = new ScrolledTextArea();
		this.receivePanel = new JPanel();
		this.receiveTA = new ScrolledTextArea();

		this.threadPool = MessageTestFrame.getThreadPool();

		if (!(Utility.isNullString(brokerURL))) {
			this.jmsURLTF.setText(brokerURL);
			this.jmsURLTF.setTextFieldEditable(true);
		}
	}

	public void initialize() {
		initializeSenderPart();
		initializeReceiverPart();
		initializeSendMessagePart();
		initializeReceiveMessagePart();
		registerActionListener();
	}

	public void close() {

	}

	private void initializeSenderPart() {
		this.senderPanel.setLayout(null);
		this.senderPanel.setBorder(new TitledBorder(""));
		this.jmsURLTF.setTextFieldColor(Color.WHITE);
		this.senderURLTF.setTextFieldColor(Color.WHITE);
		this.arbiURLTF.setTextFieldColor(Color.WHITE);
		this.connectBtn.setActionCommand("CONNECT");

		this.senderPanel.add(this.arbiURLTF);
		this.senderPanel.add(this.jmsURLTF);
		this.senderPanel.add(this.senderURLTF);
		this.senderPanel.add(this.connectBtn);

		add(this.senderPanel);
	}

	private void initializeReceiverPart() {

		this.receiverPanel.setLayout(null);
		this.receiverPanel.setBorder(new TitledBorder(""));
		this.sendBtn.setActionCommand("SEND");

		this.receiverPanel.add(this.typeCB);
		this.receiverPanel.add(this.messageTA);
		this.receiverPanel.add(this.sendBtn);

		// this.receiverTF.setText("dc://testdc1");
		//
		// this.receiverPanel.add(this.receiverTF);
		// this.receiverPanel.add(this.performativeCB);
		// this.receiverPanel.add(this.receiverTF);

		add(this.receiverPanel);
	}

	private void initializeSendMessagePart() {
		this.sendTA.setEditable(false);
		this.sendTA.setBackground(Color.WHITE);
		this.sendPanel.setLayout(new BorderLayout());
		this.sendPanel.setBorder(new TitledBorder("Send Message"));
		this.sendPanel.add(this.sendTA, "Center");
		add(this.sendPanel);
	}

	private void initializeReceiveMessagePart() {
		this.typeCB.removeComboBoxItem(LTMMessageAction.Result);
		
		this.receiveTA.setEditable(false);
		this.receiveTA.setBackground(Color.WHITE);
		this.receivePanel.setLayout(new BorderLayout());
		this.receivePanel.setBorder(new TitledBorder("Receive Message"));
		this.receivePanel.add(this.receiveTA, "Center");
		add(this.receivePanel);
	}

	private void registerActionListener() {
		ActionListener listener = new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				String command = event.getActionCommand();
				if ("SEND".equals(command))
					LTMMessageTestPanel.this.send();
				else
					LTMMessageTestPanel.this.connet();
			}
		};
		this.connectBtn.addActionListener(listener);
		this.sendBtn.addActionListener(listener);

		this.typeCB.addComboBoxActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				LTMMessageTestPanel.this.changedMessageType();
			}
		});
	}

	private void connet() {
		if (this.ds != null)
			return;

		String jmsURL = this.jmsURLTF.getText();
		if ((jmsURL == null) || ("".equals(jmsURL))) {
			showWarning("Please input JMS Broker URL!");
			return;
		}

		String dsURL = this.senderURLTF.getText();
		if ((dsURL == null) || ("".equals(dsURL))) {
			showWarning("Please input sender LTM URL!");
			return;
		}

		String agentURL = this.arbiURLTF.getText();
		if ((agentURL == null) || ("".equals(agentURL))) {
			showWarning("Please input ARBI URL!");
			return;
		}

		this.ds = new DataSource() {
			@Override
			public void onNotify(String content) {
				receiveNotification(content);
			}
		};
		System.err.println("ds monitor code not finished");
//		this.ds.connect(jmsURL, dsURL, ServerLauncher.brokerType);

		this.connectBtn.setEnabled(false);
		this.jmsURLTF.setTextFieldEditable(false);
		this.senderURLTF.setTextFieldEditable(false);
		this.arbiURLTF.setTextFieldEditable(false);

	}
	
	private void receiveNotification(String content) {
		StringBuilder builder = new StringBuilder();
		String m = this.receiveTA.getText();
		builder.append(m);
		if (!("".equals(m)))
			builder.append("\n\n");
		builder.append("type:\tNotification\n");
		builder.append("content:\t").append(content).append("\n");

		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				receiveTA.setText(builder.toString());
				receiveTA.repaint();
			}
		});
	}
	
	private void send() {
		if (this.ds == null) {
			showWarning("Please ds connect...");
			return;
		}

		String arbiURL = this.arbiURLTF.getText();

		LTMMessageAction type = (LTMMessageAction) this.typeCB.getSelectedItem();
		LTMMessageSender ms = new LTMMessageSender(this.ds, type, arbiURL, this.sendTA);

		if (type == LTMMessageAction.UpdateFact) {
			ms.setContentGLString(this.messageTA.getText().toString());
		} else if (type == LTMMessageAction.Match) {
			ms.setContentGLString(this.messageTA.getText());
		} else if (type == LTMMessageAction.Subscribe) {
			ms.setContentGLString(this.messageTA.getText());
		} else if (type == LTMMessageAction.Unsubscribe) {
			String sID = this.messageTA.getText();
			if (!checkString(sID, "subscription id"))
				return;
			ms.setContentString(sID);
		} 
//		else if (type == LTMMessageAction.Push) {
//			 GeneralizedList gl = getGLFromString(this.messageTA.getText(),
//			 "message content");
//			 if (gl == null)
//			 return;
//			 String receiverURL = this.receiverTF.getText();
//			 if (!(checkString(receiverURL, "receiver url")))
//			 return;
//			 LTMMessageAction performative = (LTMMessageAction)
//			 this.performativeCB.getSelectedItem();
//			 ms.setSendParameters(performative, receiverURL);
//			 ms.setContentGL(gl);
//		} 
		else {
			ms.setContentGLString(this.messageTA.getText());
		}

		this.threadPool.execute(ms);
	}

	private GeneralizedList getGLFromString(String str, String errorMsg) {
		if ((str == null) || ("".equals(str))) {
			showWarning("Please input " + errorMsg + "!");
			return null;
		}
		try {
			return GLFactory.newGLFromGLString(str);
		} catch (ParseException e) {
			showWarning("Please input a correct gl string of " + errorMsg + ".");
		}
		return null;
	}

	private boolean checkString(String str, String errorMsg) {
		if ((str == null) || ("".equals(str))) {
			showWarning("Please input " + errorMsg + "!");
			return false;
		}
		return true;
	}

	private void changedMessageType() {
		LTMMessageAction type = (LTMMessageAction) this.typeCB.getSelectedItem();

		// this.newGLTA.setVisible(false);
		// this.receiverTF.setVisible(false);
		// this.performativeCB.setVisible(false);

		switch (type) {
//		case RequestStream:
//		case ReleaseStream:
		case AssertFact:
		case RetrieveFact:
		case RetractFact:
//		case Result:
		case Notify:
			this.messageTA.setLabelText("Message: ");
			break;
//		case Push:
//			this.messageTA.setLabelText("Message: ");
//			this.performativeCB.setVisible(true);
//			this.receiverTF.setVisible(true);
//			break;
		case UpdateFact:
			this.messageTA.setLabelText("Old GL: ");
			// this.newGLTA.setVisible(true);
			break;
		case Match:
			this.messageTA.setLabelText("Pattern: ");
			break;
		case Subscribe:
			this.messageTA.setLabelText("Rule: ");
			break;
		case Unsubscribe:
			this.messageTA.setLabelText("S-ID: ");
			break;
		}
		repaint();
	}

	protected void setSizeComponent(int width, int height) {
		int tfWidth = width - 100 - 50;
		int bX = tfWidth + 20;
		int cbWidth = 200;

		this.senderPanel.setBounds(10, 10, width - 20, 80);
		this.jmsURLTF.setBounds(10, 5, tfWidth, 20);
		this.senderURLTF.setBounds(10, 30, tfWidth, 20);
		this.arbiURLTF.setBounds(10, 55, tfWidth, 20);
		this.connectBtn.setBounds(bX, 5, 100, 70);

		this.typeCB.setBounds(10, 5, cbWidth, 20);

		LTMMessageAction type = (LTMMessageAction) this.typeCB.getSelectedItem();
		// if (type == LTMMessageAction.UpdateFact) {
		// this.receiverPanel.setBounds(10, 20 + this.senderPanel.getHeight(),
		// width - 20, 200);
		// this.messageTA.setBounds(10, 30, tfWidth, 80);
		// this.newGLTA.setBounds(10, this.messageTA.getY() +
		// this.messageTA.getHeight() + 5, tfWidth, 80);
		// this.sendBtn.setBounds(bX, 5, 100, 190);
		// } else if (type == LTMMessageAction.Push) {
		// int cX = (width - 100 - 30) / 2;
		// this.performativeCB.setBounds(cX, 5, cbWidth, 20);
		// this.receiverPanel.setBounds(10, 20 + this.senderPanel.getHeight(),
		// width - 20, 160);
		// this.messageTA.setBounds(10, 30, tfWidth, 100);
		// this.receiverTF.setBounds(10, this.messageTA.getY() +
		// this.messageTA.getHeight() + 5, tfWidth, 20);
		// this.sendBtn.setBounds(bX, 5, 100, 150);
		// } else {
		// this.receiverPanel.setBounds(10, 20 + this.senderPanel.getHeight(),
		// width - 20, 135);
		// this.messageTA.setBounds(10, 30, tfWidth, 100);
		// this.sendBtn.setBounds(bX, 5, 100, 125);
		// }

		this.receiverPanel.setBounds(10, 20 + this.senderPanel.getHeight(), width - 20, 135);
		this.messageTA.setBounds(10, 30, tfWidth, 100);
		this.sendBtn.setBounds(bX, 5, 100, 125);

		int spY = this.senderPanel.getHeight() + this.receiverPanel.getHeight() + 35;
		int spWidth = (width - 25) / 2;
		int spHeight = height - spY - 10;

		this.sendPanel.setBounds(10, spY, spWidth, spHeight);
		this.receivePanel.setBounds(15 + spWidth, spY, spWidth, spHeight);
	}

}
