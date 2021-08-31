package kr.ac.uos.ai.arbi.ltm.communication.message;

import kr.ac.uos.ai.arbi.ltm.LTMMessageAction;

public class UnsubscribeMessage extends LTMMessage{
	public UnsubscribeMessage(String client, String content, String conversationID) {
		super(client, LTMMessageAction.Unsubscribe, content, conversationID);
	}

}