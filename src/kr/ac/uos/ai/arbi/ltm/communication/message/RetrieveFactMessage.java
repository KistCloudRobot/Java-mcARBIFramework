package kr.ac.uos.ai.arbi.ltm.communication.message;

import kr.ac.uos.ai.arbi.ltm.LTMMessageAction;

public class RetrieveFactMessage extends LTMMessage{
	public RetrieveFactMessage(String client, String content, String conversationID) {
		super(client, LTMMessageAction.RetrieveFact, content, conversationID);
	}

}
