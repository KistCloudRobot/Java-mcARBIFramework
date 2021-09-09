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
		System.out.println("Agent start");

		ArbiAgentExecutor.execute("tcp://127.0.0.1:61616", "agent://www.arbi.com/Lift2/TaskManager", taskManager,
				Broker.ZEROMQ);

		DataSource ds = new DataSource() {
			@Override
			public void onNotify(String content) {

				System.out.println("Notified! : " + content);
			}
		};
		ds.connect("tcp://127.0.0.1:61616", "ds://www.arbi.com/Lift2/TaskManager", Broker.ZEROMQ);
		System.out.println("connected");

		String subscribeID1 = ds.subscribe("(rule (fact (context $context)) --> (notify (context $context)))");
		String subscribeID2 = ds.subscribe("(rule (fact (RobotInfo $robot_id $x $y $loading $timestamp)) --> (notify (RobotInfo $robot_id $x $y $loading $timestamp)))");
		String subscribeID3 = ds.subscribe("(rule (fact (DoorStatus $status)) --> (notify (DoorStatus $status)))");
		String subscribeID4 = ds.subscribe("(rule (fact (MosPersonCall $locationID $callID)) --> (notify (MosPersonCall $locationID $callID)))");

		System.out.println("test start");

		try {
			ds.assertFact("(context 1)");
			ds.updateFact("(update (context 1) (context 2))");
			ds.assertFact("(context \"TestModel3\")");
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println("end test");
		/*
		 * Scanner in = new Scanner(System.in);
		 * 
		 * in.nextLine(); ds.unsubscribe(subscribeID);
		 * ds.assertFact("(TestModel \"TestModel2\")");
		 * 
		 * in.nextLine();
		 */
	}
}
