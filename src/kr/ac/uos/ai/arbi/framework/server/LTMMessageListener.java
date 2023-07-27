package kr.ac.uos.ai.arbi.framework.server;

import kr.ac.uos.ai.arbi.ltm.communication.message.*;

public interface LTMMessageListener {
	public void messageRecieved(LTMMessage msg);
}
