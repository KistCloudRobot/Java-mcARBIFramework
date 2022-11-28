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
import kr.ac.uos.ai.arbi.agent.ArbiAgent;
import kr.ac.uos.ai.arbi.agent.ArbiAgentExecutor;
import kr.ac.uos.ai.arbi.monitor.Utility;
import kr.ac.uos.ai.arbi.monitor.gui.AbstractNullLayoutPanel;
import kr.ac.uos.ai.arbi.monitor.gui.LabeledComboBox;
import kr.ac.uos.ai.arbi.monitor.gui.LabeledTextArea;
import kr.ac.uos.ai.arbi.monitor.gui.LabeledTextField;
import kr.ac.uos.ai.arbi.monitor.gui.ScrolledTextArea;
import kr.ac.uos.ai.arbi.monitor.model.ArbiAgentMessage.AgentMessageAction;
import test.AgentExecuteTest;

public class AAMessageTestPanel extends AbstractNullLayoutPanel {

	private static final long serialVersionUID = 8303616064348648370L;
	private static final int LABEL_WIDTH = 90;
	private static final int BUTTON_WIDTH = 100;
	private static final int BUTTON_HEIGHT = 45;
	private static final int COMBOBOX_WIDTH = 90;
	private static final String DEFAULT_JMS_URL = "tcp://localhost:61616";
	private static final String DEFAULT_SENDER_URL = "agent://ai.uos.ac.kr/tester01";
	private static final String DEFAULT_RECEIVER_URL = "agent://ai.uos.ac.kr/tester02";
	private static final String DEFAULT_DS_URL = "ds://ai.uos.ac.kr/tester01";
	private static final String DEFAULT_FRAMEWORK_URL = "tcp://localhost:61616";
	private static final String RESPONSE_MESSAGE = "(ok)";

	private JPanel senderPanel;
	private LabeledTextField jmsHostTF;
	private LabeledTextField jmsPortTF;
	private LabeledTextField senderURLTF;
	private JButton connectBtn;
	private JPanel receiverPanel;
	private LabeledTextField receiverURLTF;
	private LabeledComboBox agentActionCB;
	private LabeledTextArea messageTA;
	private JButton sendBtn;
	private JPanel sendPanel;
	private ScrolledTextArea sendTA;
	private JPanel receivePanel;
	private ScrolledTextArea receiveTA;
	private ArbiAgent aa;
	private ExecutorService threadPool;

	public AAMessageTestPanel(String brokerURL) {
		// TODO Auto-generated constructor stub
		this.senderPanel = new JPanel();
		this.jmsHostTF = new LabeledTextField("JMS URL: ", 60, "localhost");
		this.jmsPortTF = new LabeledTextField("JMS URL: ", 30, "61616");
		this.senderURLTF = new LabeledTextField("Sender URL: ", 90, "agent://ai.uos.ac.kr/tester01");
		this.connectBtn = new JButton("Connect");

		this.receiverPanel = new JPanel();
		this.receiverURLTF = new LabeledTextField("Receiver URL: ", 90, "agent://ai.uos.ac.kr/tester02");
		this.agentActionCB = new LabeledComboBox("Action: ", 90, AgentMessageAction.values());
		this.messageTA = new LabeledTextArea("Message: ", 90);
		this.sendBtn = new JButton("Send");

		this.sendPanel = new JPanel();
		this.sendTA = new ScrolledTextArea();
		this.receivePanel = new JPanel();
		this.receiveTA = new ScrolledTextArea();

		this.threadPool = MessageTestFrame.getThreadPool();

		if (!(Utility.isNullString(brokerURL))) {
			this.jmsHostTF.setText(brokerURL);
			this.jmsHostTF.setTextFieldEditable(true);
			this.jmsPortTF.setText(brokerURL);
			this.jmsPortTF.setTextFieldEditable(true);
		}
	}

	@Override
	public void initialize() {
		// TODO Auto-generated method stub
		initializeSenderPart();
		initializeReceiverPart();
		initializeSendMessagePart();
		initializeReceiveMessagePart();
		registerActionListener();
	}

	private void initializeSenderPart() {
		this.senderPanel.setLayout(null);
		this.senderPanel.setBorder(new TitledBorder(""));
		this.jmsHostTF.setTextFieldColor(Color.WHITE);
		this.jmsPortTF.setTextFieldColor(Color.WHITE);
		this.senderURLTF.setTextFieldColor(Color.WHITE);
		this.connectBtn.setActionCommand("CONNECT");

		this.senderPanel.add(this.jmsHostTF);
		this.senderPanel.add(this.jmsPortTF);
		this.senderPanel.add(this.senderURLTF);
		this.senderPanel.add(this.connectBtn);

		add(this.senderPanel);
	}

	private void initializeReceiverPart() {
		this.sendBtn.setActionCommand("SEND");

		this.receiverPanel.setLayout(null);
		this.receiverPanel.setBorder(new TitledBorder(""));

		this.receiverPanel.add(this.receiverURLTF);
		this.receiverPanel.add(this.agentActionCB);
		this.receiverPanel.add(this.messageTA);
		this.receiverPanel.add(this.sendBtn);

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
					AAMessageTestPanel.this.send();
				else
					AAMessageTestPanel.this.connet();
			}
		};
		this.connectBtn.addActionListener(listener);
		this.sendBtn.addActionListener(listener);
	}

	private void connet() {
		if (this.aa != null)
			return;

		String host = this.jmsHostTF.getText();
		if ((host == null) || host.isEmpty()) {
			showWarning("Please input JMS Broker URL!");
			return;
		}

		String portString = this.jmsPortTF.getText();
		if ((portString == null) || portString.isEmpty()) {
			showWarning("Please input JMS Broker URL!");
			return;
		}
		int port = Integer.parseInt(portString);
		
		String saURL = this.senderURLTF.getText();
		if ((saURL == null) || ("".equals(saURL))) {
			showWarning("Please input sender AA URL!");
			return;
		}
		
		aa = new ArbiAgent(){
			
			@Override
			public void onData(String sender, String data) {
				// TODO Auto-generated method stub
				receiveMessage("Data", sender, data, "");
			}
			
			@Override
			public String onQuery(String sender, String query) {
				// TODO Auto-generated method stub
				receiveMessage("Query", sender, query, "(ok)");
				return "(ok)";
			}
			
			@Override
			public String onRequest(String sender, String request) {
				// TODO Auto-generated method stub
				receiveMessage("Request", sender, request, "(ok)");
				return "(ok)";
			}
			
			@Override
			public String onSubscribe(String sender, String subscribe) {
				// TODO Auto-generated method stub
				receiveMessage("Subscribe", sender, subscribe, "(ok)");
				return "(ok)";
			}
		};
		ArbiAgentExecutor.execute(host, port, senderURLTF.getText() , aa, ServerLauncher.brokerType);

		this.connectBtn.setEnabled(false);
		this.jmsHostTF.setTextFieldEditable(false);
		this.jmsPortTF.setTextFieldEditable(false);
		this.senderURLTF.setTextFieldEditable(false);

	}
	
	private void receiveMessage(String type,String sender, String content, String response) {
		StringBuilder builder = new StringBuilder();
		String m = this.receiveTA.getText();
		builder.append(m);
		if (!("".equals(m)))
			builder.append("\n\n");
		builder.append("sender:\t").append(sender).append("\n");
		builder.append("type:\t"+ type +"\n");
		builder.append("content:\t").append(content).append("\n");
		builder.append("response:\t").append(response).append("\n");

		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				receiveTA.setText(builder.toString());
				receiveTA.repaint();
			}
		});
	}

	private void send() {
		if (this.aa == null) {
			showWarning("Please aa connect...");
			return;
		}

		String receiverURL = this.receiverURLTF.getText();
		if ((receiverURL == null) || ("".equals(receiverURL))) {
			showWarning("Please input receiver AA URL!");
			return;
		}

		String content = this.messageTA.getText();
		if ((content == null) || ("".equals(content))) {
			showWarning("Please input message content!");
			return;
		}

		AgentMessageAction type = (AgentMessageAction) this.agentActionCB.getSelectedItem();
		
		this.threadPool.execute(new AAMessageSender(this.aa, type, receiverURL, content, this.sendTA));
	}

	protected void setSizeComponent(int width, int height) {
		int tfWidth = width - 100 - 50;
		int bX = tfWidth + 20;

		this.senderPanel.setBounds(10, 10, width - 20, 55);
		this.connectBtn.setBounds(bX, 5, 100, 45);

		this.jmsHostTF.setBounds(10, 5, tfWidth * 3 / 4, 20);
		this.jmsPortTF.setBounds(10 + tfWidth * 3 / 4 + 5, 5, tfWidth / 4 - 5, 20);
		this.senderURLTF.setBounds(10, 30, tfWidth, 20);

		this.receiverPanel.setBounds(10, 20 + this.senderPanel.getHeight(), width - 20, 160);
		this.receiverURLTF.setBounds(10, 5, tfWidth, 20);
		this.agentActionCB.setBounds(10, 30, 180, 20);
		this.messageTA.setBounds(10, 55, tfWidth, 100);
		this.sendBtn.setBounds(bX, 5, 100, 150);

		int spY = this.senderPanel.getHeight() + this.receiverPanel.getHeight() + 35;
		int spWidth = (width - 25) / 2;
		int spHeight = height - spY - 10;

		this.sendPanel.setBounds(10, spY, spWidth, spHeight);
		this.receivePanel.setBounds(15 + spWidth, spY, spWidth, spHeight);
	}

}
