package kr.ac.uos.ai.arbi.ltm.communication.message;

import kr.ac.uos.ai.arbi.ltm.LTMMessageAction;

public class ReleaseStreamMessage extends LTMMessage{
	public ReleaseStreamMessage(String client, String content, String conversationID) {
		super(client, LTMMessageAction.ReleaseStream, content, conversationID);
	}

}