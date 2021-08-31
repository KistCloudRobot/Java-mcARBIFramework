package kr.ac.uos.ai.arbi.agent.communication;

import kr.ac.uos.ai.arbi.agent.ArbiAgentMessage;

public class DispatchSubscribeTask implements Runnable {

	
	private final ArbiAgentMessageToolkit					toolkit;
	private final ArbiAgentMessage							message;

	DispatchSubscribeTask(ArbiAgentMessageToolkit toolkit, ArbiAgentMessage message) {
		this.toolkit = toolkit;
		this.message = message;
	}

	public void run() {
		dispatchRequest();
	}
	
	private void dispatchRequest() {
		String requestID = message.getConversationID();
		String sender = message.getSender();
		String request = message.getContent();
		String response = toolkit.onSubscribe(sender, request);
		if (response == null) {
			response = "(response \"ok\")";
		}
		toolkit.sendResponseMessage(requestID, sender, response);
	}
}
