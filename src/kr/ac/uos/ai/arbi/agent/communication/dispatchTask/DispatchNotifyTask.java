package kr.ac.uos.ai.arbi.agent.communication.dispatchTask;

import kr.ac.uos.ai.arbi.agent.ArbiAgentMessage;
import kr.ac.uos.ai.arbi.agent.communication.ArbiAgentMessageToolkit;

public class DispatchNotifyTask implements Runnable {

	private final ArbiAgentMessageToolkit				toolkit;
	private final ArbiAgentMessage						message;

	public DispatchNotifyTask(ArbiAgentMessageToolkit toolkit, ArbiAgentMessage message) {
		this.toolkit = toolkit;
		this.message = message;
	}
	
	public void run() {
		String sender = message.getSender();
		String data = message.getContent();
		dispatchNotification(sender, data);
	}
	
	private void dispatchNotification(String sender, String data) {
		toolkit.onNotify(sender, data);
	}
	
}
