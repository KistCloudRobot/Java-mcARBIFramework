package kr.ac.uos.ai.arbi.ltm.communication;

import kr.ac.uos.ai.arbi.ltm.communication.message.LTMMessage;


public interface LTMMessageAdaptor {
	public void send(LTMMessage msg);
	public void notify(LTMMessage msg);
	public void close();
}
