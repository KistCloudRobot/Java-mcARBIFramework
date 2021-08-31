package kr.ac.uos.ai.arbi.agent.communication;

import kr.ac.uos.ai.arbi.agent.ArbiAgentMessage;

public class DispatchRequestStreamTask implements Runnable{

	private final ArbiAgentMessageToolkit		toolkit;
	private final ArbiAgentMessage				message;
	
	public DispatchRequestStreamTask(ArbiAgentMessageToolkit toolkit, ArbiAgentMessage message) {
		this.toolkit = toolkit;
		this.message = message;
	}
	
	@Override
	public void run() {
		dispatchRequestStream();
	}

	private void dispatchRequestStream() {
		String requestID = message.getConversationID();
		String sender = message.getSender();
		String rule = message.getContent();
		String response = toolkit.onRequestStream(sender, rule);
		if(response == null) {
			response = "(response \"ok\")";
		}
		toolkit.sendResponseMessage(requestID, sender, response);
	}
	
	
}
