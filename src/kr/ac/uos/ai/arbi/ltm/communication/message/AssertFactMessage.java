package kr.ac.uos.ai.arbi.ltm.communication.message;

import kr.ac.uos.ai.arbi.ltm.LTMMessageAction;

public class AssertFactMessage extends LTMMessage{

	public AssertFactMessage(String client, String content, String conversationID) {
		super(client, LTMMessageAction.AssertFact, content, conversationID);
	}

}
