package test;

import kr.ac.uos.ai.arbi.BrokerType;
import kr.ac.uos.ai.arbi.agent.ArbiAgent;
import kr.ac.uos.ai.arbi.agent.ArbiAgentExecutor;
import kr.ac.uos.ai.arbi.ltm.DataSource;

public class TaskManagerTest {

	public static void main(String[] args) {
		ArbiAgent taskManager = new ArbiAgent() {
			@Override
			public String onRequest(String sender, String request) {
				// TODO Auto-generated method stub
				return "(ok)";
			}
		};
		ArbiAgentExecutor.execute("127.0.0.1", 61616, "agent://www.arbi.com/TaskManager", taskManager, BrokerType.ZEROMQ);

		DataSource ds = new DataSource();
		System.out.println("started");
		ds.connect("127.0.0.1", 61616, "ds://www.arbi.com/TaskManager",BrokerType.ZEROMQ);
		ds.assertFact("(serviceModel \"ServiceModel.xml\")");
		
		ds.assertFact("(Robot_position (time 467015366.818170342) (position 8.2118526332695385 -9.5512692050022654))");
		System.out.println("fuck");
		System.out.println("ds retrieval" + ds.retrieveFact("(Robot_position $time $position)"));
		//taskManager.request("agent://www.arbi.com/TaskReasoner", "");
	}
}
