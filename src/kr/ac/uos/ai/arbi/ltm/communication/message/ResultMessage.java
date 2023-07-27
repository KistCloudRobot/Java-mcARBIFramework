package kr.ac.uos.ai.arbi.ltm.communication.message;

import kr.ac.uos.ai.arbi.ltm.LTMMessageAction;

public class ResultMessage extends LTMMessage{
	public ResultMessage(String client, String content, String conversationID) {
		super(client, LTMMessageAction.Result, content, conversationID);
	}

}
