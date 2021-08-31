package test.stability;

import kr.ac.uos.ai.arbi.agent.ArbiAgent;
import kr.ac.uos.ai.arbi.agent.ArbiAgentExecutor;

public class MassReceiveAgent extends ArbiAgent{
	private String id;
	private int rCount = 0;
	public MassReceiveAgent(int id) {
		this.id = Integer.toString(id);
		ArbiAgentExecutor.execute("tcp://127.0.0.1:61616", "ReceiveAgent" + id, this,2);
	}
	
	public String onQuery(String sender, String query) {
		rCount++;
		String text = "(receivedQuery " + "ReceiveAgent" + this.id + " " + rCount + ")";
		
		if(rCount >= 300) {
			System.out.println("i : " + rCount + " " + query);
		}
		
		return text;
	}

	public void onData(String sender, String data) {
		String text = "(receivedData" + "ReceiveAgent" + this.id + " " + rCount + ")";
		rCount++;
		if(rCount >= 300) {
			System.out.println("i : " + rCount + " " + data + " ");
			
			
		}
	}

}
