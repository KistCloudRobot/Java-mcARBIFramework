package test;

import java.util.Scanner;

import kr.ac.uos.ai.arbi.BrokerType;
import kr.ac.uos.ai.arbi.agent.ArbiAgentExecutor;
import kr.ac.uos.ai.arbi.ltm.DataSource;

public class AgentSubscriptionTest {
	public static void main(String[] ar) {
		new AgentSubscriptionTest();
	}
	
	public AgentSubscriptionTest() {
		super();
		DataSource ds = new DataSource() {
			@Override
			public void onNotify(String content) {
				System.out.println("Notified Agent 1 : "+content);

			}		
		};
		ds.connect("tcp://192.168.100.10:61116", "ds://www.arbi.com/TestAgent",BrokerType.ACTIVEMQ);
		String subscribeID = ds.subscribe("(rule (fact (TestSenseResult \"robotID314\" $model)) --> (notify (TestSenseResult $model)))");
		System.out.println(3);
		
		ds.assertFact("(TestSenseResult \"robotID314\" 3)");
		
		System.out.println("?????");
		
		Scanner sc = new Scanner(System.in);
		sc.nextLine();
		
		ds.assertFact("(TestSenseResult \"robotID313\" \"testString2\")");
		
		sc.nextLine();
		ds.assertFact("(TestSenseResult \"robotID314\" \"testString3\")");
	}
}
