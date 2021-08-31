package kr.ac.uos.ai.arbi.agent.communication;

import kr.ac.uos.ai.arbi.agent.ArbiAgentMessage;

public class DispatchUnsubscribeTask implements Runnable {

	private final ArbiAgentMessageToolkit					toolkit;
	private final ArbiAgentMessage							message;

	DispatchUnsubscribeTask(ArbiAgentMessageToolkit toolkit, ArbiAgentMessage message) {
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
