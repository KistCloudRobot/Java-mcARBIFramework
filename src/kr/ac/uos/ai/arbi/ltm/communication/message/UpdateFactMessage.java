package kr.ac.uos.ai.arbi.ltm.communication.message;

import kr.ac.uos.ai.arbi.ltm.LTMMessageAction;

public class UpdateFactMessage extends LTMMessage{
	public UpdateFactMessage(String client, String content, String conversationID) {
		super(client, LTMMessageAction.UpdateFact, content, conversationID);
	}

}