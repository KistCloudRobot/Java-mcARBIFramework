package kr.ac.uos.ai.arbi.agent.communication.adaptor;

import kr.ac.uos.ai.arbi.agent.ArbiAgentMessage;

public interface ArbiMessageAdaptor {
	public void start();
	public void send(ArbiAgentMessage message);
	public void close();
}
