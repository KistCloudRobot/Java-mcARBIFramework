package test;

import kr.ac.uos.ai.arbi.Broker;
import kr.ac.uos.ai.arbi.agent.ArbiAgent;
import kr.ac.uos.ai.arbi.agent.ArbiAgentExecutor;
import kr.ac.uos.ai.arbi.ltm.DataSource;

public class SubUnsubTest {

	public static void main(String[] args) {
		ArbiAgent taskManager = new ArbiAgent() {
			@Override
			public String onRequest(String sender, String request) {
				// TODO Auto-generated method stub
				return "(ok)";
			}
		};
		ArbiAgentExecutor.execute("tcp://127.0.0.1:61616", "agent://www.arbi.com/TaskManager", taskManager, Broker.ZEROMQ);

		DataSource ds = new DataSource() {
			@Override
			public void onNotify(String content) {
				
				System.out.println("Notified! : "+content);
			}		
		};
		System.out.println("testing 1");
		taskManager.send("test", "testing");
		ds.connect("tcp://127.0.0.1:61616", "ds://www.arbi.com/TaskManager",Broker.ZEROMQ);
		
		System.out.println("conn ");
		String subscribeID = ds.subscribe("(rule (fact (TestModel $model)) --> (notify (TestModel $model)))");
		System.out.println("testing " + subscribeID);
		
		try {
			ds.assertFact("(TestModel \"TestModel1\")");
			ds.assertFact("(TestModel \"TestModel2\")");
			ds.assertFact("(TestModel \"TestModel3\")");
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		ds.close();
		taskManager.close();
		/*
		Scanner in = new Scanner(System.in);
		
		in.nextLine();
		ds.unsubscribe(subscribeID);
		ds.assertFact("(TestModel \"TestModel2\")");
		
		in.nextLine();
		*/
	}
}
