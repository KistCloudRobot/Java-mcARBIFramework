package test.routing;

import kr.ac.uos.ai.arbi.agent.ArbiAgent;
import kr.ac.uos.ai.arbi.agent.ArbiAgentExecutor;

public class RoutingTestAgentReceiver extends ArbiAgent  {
	public static void main(String[] ar) {
		new RoutingTestAgentReceiver();
	}
	
	public RoutingTestAgentReceiver() {
		ArbiAgentExecutor.execute("tcp://127.0.0.1:61116", "agent://www.arbi.com/TestReceiver/receiverAgent", this, 2);
		
	}
	
	@Override
	public void onData(String sender, String data) {
		System.out.println("received! " + data + " " + sender);
	}
}
