package kr.ac.uos.ai.arbi.ltm.communication.message;

import kr.ac.uos.ai.arbi.ltm.LTMMessageAction;

public class MatchMessage extends LTMMessage{
	public MatchMessage(String client, String content, String conversationID) {
		super(client, LTMMessageAction.Match, content, conversationID);
	}

}
