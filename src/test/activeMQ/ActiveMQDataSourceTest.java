package test.activeMQ;

import java.util.Scanner;

import kr.ac.uos.ai.arbi.BrokerType;
import kr.ac.uos.ai.arbi.agent.ArbiAgentExecutor;
import kr.ac.uos.ai.arbi.ltm.DataSource;

public class ActiveMQDataSourceTest {
	public static void main(String[] ar) {
		DataSource ds = new DataSource() {
			@Override
			public void onNotify(String content) {
				System.out.println("Notified Agent 1 : "+content);
	
			}		
		};
		ds.connect("127.0.0.1", 61616, "ds://www.arbi.com/TestDataSource",BrokerType.ACTIVEMQ);
		String subscribeID = ds.subscribe("(rule (fact (TestSenseResult \"robotID314\" $model)) --> (notify (TestSenseResult $model)))");
		System.out.println(3);
		
		ds.assertFact("(TestSenseResult \"robotID314\" 3)");
		
		System.out.println("?????");
		
		Scanner sc = new Scanner(System.in);
		sc.nextLine();
		
		ds.updateFact("(update (TestSenseResult $robotID $string) (TestSenseResult \"robotID313\" \"testString2\"))");
		
		sc.nextLine();
		ds.updateFact("(update (TestSenseResult $robotID $string) (TestSenseResult \"robotID314\" \"testString3\"))");
	}
}
