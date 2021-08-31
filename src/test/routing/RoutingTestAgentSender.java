package test.routing;

import kr.ac.uos.ai.arbi.agent.ArbiAgent;
import kr.ac.uos.ai.arbi.agent.ArbiAgentExecutor;

public class RoutingTestAgentSender extends ArbiAgent {
	public static void main(String[] ar) {
		new RoutingTestAgentSender();
	}
	
	public RoutingTestAgentSender() {
		ArbiAgentExecutor.execute("tcp://127.0.0.1:61316", "agent://www.arbi.com/TestSender/senderAgent", this, 2);
		
		this.send("agent://www.arbi.com/TestReceiver/receiverAgent", "(testData)");
	}
}
