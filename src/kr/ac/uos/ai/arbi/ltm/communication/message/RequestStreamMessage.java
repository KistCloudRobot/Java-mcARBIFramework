package kr.ac.uos.ai.arbi.ltm.communication.message;

import kr.ac.uos.ai.arbi.ltm.LTMMessageAction;

public class RequestStreamMessage extends LTMMessage{
	public RequestStreamMessage(String client, String content, String conversationID) {
		super(client, LTMMessageAction.RequestStream, content, conversationID);
	}

}