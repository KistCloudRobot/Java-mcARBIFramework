package kr.ac.uos.ai.arbi.ltm.communication.message;

import kr.ac.uos.ai.arbi.ltm.LTMMessageAction;

public class GetLastModifiedTimeMessage extends LTMMessage{

	public GetLastModifiedTimeMessage(String client,String content, String conversationID) {
		super(client, LTMMessageAction.GetLastModifiedTime, content, conversationID);
	}

}
