package test.speed;

import kr.ac.uos.ai.arbi.agent.ArbiAgent;
import kr.ac.uos.ai.arbi.agent.ArbiAgentExecutor;

public class ReceiveAgent extends ArbiAgent{

	public ReceiveAgent() {

		ArbiAgentExecutor.execute("tcp://127.0.0.1:61616", "receiver", this,2);
	}
	
	public String onQuery(String sender, String query) {
		System.out.println("received " + query);
		return "(return " + query + ")";
	}

	public void onData(String sender, String data) {
	
	}

}
