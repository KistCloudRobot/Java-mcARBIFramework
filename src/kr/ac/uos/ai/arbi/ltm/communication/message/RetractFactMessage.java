package kr.ac.uos.ai.arbi.ltm.communication.message;

import kr.ac.uos.ai.arbi.ltm.LTMMessageAction;

public class RetractFactMessage extends LTMMessage{
	public RetractFactMessage(String client, String content, String conversationID) {
		super(client, LTMMessageAction.RetractFact, content, conversationID);
	}

}
