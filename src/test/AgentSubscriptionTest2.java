package test;

import kr.ac.uos.ai.arbi.BrokerType;
import kr.ac.uos.ai.arbi.ltm.DataSource;

public class AgentSubscriptionTest2 {
	public static void main(String[] ar) {
		new AgentSubscriptionTest2();
	
	}
	
	public AgentSubscriptionTest2() {
		DataSource ds = new DataSource() {
			@Override
			public void onNotify(String content) {
				System.out.println("Notified Agent 2 : "+content);
			}		
		};
		
		ds.connect("tcp://127.0.0.1:61616", "ds://www.arbi.com/TestAgent2",BrokerType.ZEROMQ);
		String subscribeID = ds.subscribe("(rule (fact (TestSenseResult \"robotID314\" $model)) --> (notify (TestSenseResult $model)))");
		
	}
}
