package test;

import java.util.Scanner;

import kr.ac.uos.ai.arbi.BrokerType;
import kr.ac.uos.ai.arbi.ltm.DataSource;

public class AgentSubscriptionTest {
	public static void main(String[] ar) {
		new AgentSubscriptionTest();
	}
	
	public AgentSubscriptionTest() {
		DataSource ds = new DataSource() {
			@Override
			public void onNotify(String content) {
				System.out.println("Notified Agent 1 : "+content);

			}		
		};
		
		ds.connect("tcp://127.0.0.1:61616", "ds://www.arbi.com/TestAgent",BrokerType.ZEROMQ);
		String subscribeID = ds.subscribe("(rule (fact (TestSenseResult \"robotID314\" $model)) --> (notify (TestSenseResult $model)))");
		
		ds.assertFact("(TestSenseResult \"robotID314\" 3)");
		
		System.out.println("?????");
		
		Scanner sc = new Scanner(System.in);
		sc.nextLine();
		
		ds.assertFact("(TestSenseResult \"robotID313\" \"testString2\")");
		
		sc.nextLine();
		ds.assertFact("(TestSenseResult \"robotID314\" \"testString3\")");
	}
}
