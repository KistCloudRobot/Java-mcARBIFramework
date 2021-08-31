package kr.ac.uos.ai.arbi.ltm.communication.message;

import kr.ac.uos.ai.arbi.ltm.LTMMessageAction;

public class NotifyMessage extends LTMMessage{
	public NotifyMessage(String client, String content, String conversationID) {
		super(client, LTMMessageAction.Notify, content, conversationID);
	}

}