package kr.ac.uos.ai.arbi.agent.communication.dispatchTask;

import kr.ac.uos.ai.arbi.agent.ArbiAgentMessage;
import kr.ac.uos.ai.arbi.agent.communication.ArbiAgentMessageToolkit;

public class DispatchQueryTask implements Runnable{
	
	private final ArbiAgentMessageToolkit					toolkit;
	private final ArbiAgentMessage							message;

	public DispatchQueryTask(ArbiAgentMessageToolkit toolkit, ArbiAgentMessage message) {
		this.toolkit = toolkit;
		this.message = message;
	}

	public void run() {
		dispatchQuery();
	}
	
	private void dispatchQuery() {
		String requestID = message.getConversationID();
		String sender = message.getSender();
		String request = message.getContent();
		String response = toolkit.onQuery(sender, request);
		if (response == null) {
			response = "(response \"ok\")";
		}
		toolkit.sendResponseMessage(requestID, sender, response);
	}
}
