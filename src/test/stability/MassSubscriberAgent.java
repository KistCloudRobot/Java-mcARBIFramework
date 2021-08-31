package test.stability;

import kr.ac.uos.ai.arbi.Broker;
import kr.ac.uos.ai.arbi.agent.ArbiAgent;
import kr.ac.uos.ai.arbi.agent.ArbiAgentExecutor;
import kr.ac.uos.ai.arbi.ltm.DataSource;

public class MassSubscriberAgent extends ArbiAgent {
	private String id = "";
	private DataSource ds = null;
	private int count = 0;
	private long startTime = 0;
	public MassSubscriberAgent(int num, String other) {
		String agentId = "subscribeAction" + num;
		agentId = Integer.toString(num) + other;
		this.id = Integer.toString(num);
		ArbiAgentExecutor.execute("tcp://127.0.0.1:61616", "sub" + agentId, this, 2);
		
		startTime = System.currentTimeMillis();
		ds = new DataSource() {
			@Override
			public void onNotify(String content) {
				count++;
				if(count == 100) {
					long currentTime = System.currentTimeMillis();
					System.out.println("finished " + id + " thread in : " + (currentTime - startTime) + " ms");
				}
			}
		};
		ds.connect("tcp://127.0.0.1:61616", "ds" + agentId, Broker.ZEROMQ);
		String subscribeID = ds.subscribe("(rule (fact (assertAction \""+ id + "\" $i)) --> (notify (assertAction \"" + id +"\" $i)))");
		
		
	}

	public static void main(String[] ar) {
		new MassSubscriberAgent(1,"");
	}

}
