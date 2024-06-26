package kr.ac.uos.ai.arbi.agent.communication.dispatchTask;

import kr.ac.uos.ai.arbi.agent.ArbiAgentMessage;
import kr.ac.uos.ai.arbi.agent.communication.ArbiAgentMessageToolkit;

public class DispatchUnsubscribeTask implements Runnable {

	private final ArbiAgentMessageToolkit					toolkit;
	private final ArbiAgentMessage							message;

	public DispatchUnsubscribeTask(ArbiAgentMessageToolkit toolkit, ArbiAgentMessage message) {
		this.toolkit = toolkit;
		this.message = message;
	}

	public void run() {
		dispatchUnsubscribe();
	}
	
	private void dispatchUnsubscribe() {
		String sender = message.getSender();
		String request = message.getContent();
		toolkit.onUnsubscribe(sender, request);
	}
}
