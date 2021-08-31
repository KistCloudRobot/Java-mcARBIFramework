package kr.ac.uos.ai.arbi.agent.communication;

import kr.ac.uos.ai.arbi.agent.ArbiAgentMessage;

public interface ArbiMessageAdaptor {
	public void send(ArbiAgentMessage message);
	public void close();
}
