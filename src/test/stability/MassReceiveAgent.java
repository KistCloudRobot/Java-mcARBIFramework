package test.stability;

import kr.ac.uos.ai.arbi.Broker;
import kr.ac.uos.ai.arbi.agent.ArbiAgent;
import kr.ac.uos.ai.arbi.agent.ArbiAgentExecutor;
import kr.ac.uos.ai.arbi.ltm.DataSource;

public class MassReceiveAgent extends ArbiAgent{
	private String id;
	private int rCount = 0;
	
	private DataSource ds = null;
	public MassReceiveAgent(int id) {
		this.id = Integer.toString(id);
		ArbiAgentExecutor.execute("tcp://127.0.0.1:61616", "ReceiveAgent" + id, this,2);
		
		ds = new DataSource() {
			@Override
			public void onNotify(String content) {
				System.out.println("notified" + content);
			}
		};
		ds.connect("tcp://127.0.0.1:61616", "ds" + id, Broker.ZEROMQ);
		String subscribeID = ds.subscribe("(rule (fact (SendTest \"sendAgent\" "+ id + " $i)) --> (notify (SendTest " + id +" $i)))");
		
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
