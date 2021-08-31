package kr.ac.uos.ai.arbi.framework.server;

import kr.ac.uos.ai.arbi.agent.ArbiAgentMessage;
import kr.ac.uos.ai.arbi.ltm.communication.message.LTMMessage;

public interface MessageDeliverAdaptor extends MessageAdaptor{
	public void deliver(ArbiAgentMessage message);
	public void deliverToMonitor(LTMMessage cdcMessage);
	public void deliverToMonitor(ArbiAgentMessage agentMessage);
	
}
