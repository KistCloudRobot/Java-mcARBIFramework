package kr.ac.uos.ai.arbi.ltm.communication.message;

import kr.ac.uos.ai.arbi.ltm.LTMMessageAction;

public class SubscribeMessage extends LTMMessage{
	public SubscribeMessage(String client, String content, String conversationID) {
		super(client, LTMMessageAction.Subscribe, content, conversationID);
	}

}