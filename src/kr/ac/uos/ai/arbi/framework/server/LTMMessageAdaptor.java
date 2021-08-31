package kr.ac.uos.ai.arbi.framework.server;

import kr.ac.uos.ai.arbi.ltm.communication.message.LTMMessage;

public interface LTMMessageAdaptor extends MessageAdaptor{
	public void send(LTMMessage msg);

	public void notify(LTMMessage msg);

}
