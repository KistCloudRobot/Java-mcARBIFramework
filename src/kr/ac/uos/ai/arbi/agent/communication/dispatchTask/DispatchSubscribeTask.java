package kr.ac.uos.ai.arbi.agent.communication.dispatchTask;

import kr.ac.uos.ai.arbi.agent.ArbiAgentMessage;
import kr.ac.uos.ai.arbi.agent.communication.ArbiAgentMessageToolkit;

public class DispatchSubscribeTask implements Runnable {

	
	private final ArbiAgentMessageToolkit					toolkit;
	private final ArbiAgentMessage							message;

	public DispatchSubscribeTask(ArbiAgentMessageToolkit toolkit, ArbiAgentMessage message) {
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
