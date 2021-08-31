package kr.ac.uos.ai.arbi.monitor.gui.messagetest;

import java.net.ConnectException;

import javax.swing.SwingUtilities;

import kr.ac.uos.ai.arbi.agent.ArbiAgent;
import kr.ac.uos.ai.arbi.monitor.gui.ScrolledTextArea;
import kr.ac.uos.ai.arbi.monitor.model.ArbiAgentMessage.AgentMessageAction;

public class AAMessageSender implements Runnable {
	private ArbiAgent aa;
	private AgentMessageAction type;
	private String receiver;
	private String content;
	private ScrolledTextArea textArea;

	AAMessageSender(ArbiAgent aa, AgentMessageAction type, String receiver, String content, ScrolledTextArea textArea) {
		this.aa = aa;
		this.type = type;
		this.receiver = receiver;
		this.content = content;
		this.textArea = textArea;
	}

	public void run() {
		String response = null;
		switch (type) {
		case Request:
			response = this.aa.request(this.receiver, this.content);
			break;
		case Query:
			response = this.aa.query(this.receiver, this.content);
			break;
		case Subscribe:
			response = this.aa.subscribe(this.receiver, this.content);
			break;
		case Unsubscribe:
			this.aa.unsubscribe(this.receiver, this.content);
			break;
		case Notify:
			this.aa.notify(this.receiver, this.content);
			break;
		case Inform:
			this.aa.send(this.receiver, this.content);
			break;
		case System:
			this.aa.system(this.receiver, this.content);
			break;
		}

		if (response == null)
			response = "";

		StringBuilder builder = new StringBuilder();

		String m = this.textArea.getText();
		builder.append(m);
		if (!("".equals(m)))
			builder.append("\n\n");
		builder.append("reciver:\t").append(this.receiver).append("\n");
		builder.append("type:\t").append(this.type).append("\n");
		builder.append("content:\t").append(this.content).append("\n");
		builder.append("response:\t").append(response).append("\n");

		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				AAMessageSender.this.textArea.setText(builder.toString());
				AAMessageSender.this.textArea.repaint();
			}
		});
	}
}