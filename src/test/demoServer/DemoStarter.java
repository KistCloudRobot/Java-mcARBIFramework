package test.demoServer;

import java.util.Scanner;

import kr.ac.uos.ai.arbi.BrokerType;
import kr.ac.uos.ai.arbi.agent.ArbiAgent;
import kr.ac.uos.ai.arbi.agent.ArbiAgentExecutor;
import kr.ac.uos.ai.arbi.ltm.DataSource;

public class DemoStarter extends ArbiAgent {
	static String agentURI = "agent://www.arbi.com/demoStarter";
//	static String host = "127.0.0.1";
	static String host = "172.16.165.141";
	static int port = 61316;
	
	public static void main(String[] args) {
		DemoStarter demo = new DemoStarter(BrokerType.ACTIVEMQ);

		Scanner in = new Scanner(System.in);
		in.nextLine();
		System.out.println("demo start!");
		
		demo.startDemo();

		in.nextLine();
		System.out.println("second call start!");
		demo.secondCall();
	}

	DataSource dc;
	
	public DemoStarter(BrokerType brokerType) {

		dc = new DataSource();
		dc.connect(host, port, "dc://www.agent.com/demoStarter", brokerType);
		ArbiAgentExecutor.execute(host, port, agentURI, this, brokerType);

		//communicator.getBaseChannel().request("agent://www.mcarbi.com/organizationDispatcher", "(DiapatchOrganization)");
	}
	
	
	@Override
	public void onStart() {
		
		
		//send("agent://www.arbi.com/TaskAllocator", "(hi)");
	}
	public void secondCall() {
		dc.assertFact("(context (PersonCall \"call05\" \"http://www.arbi.com/ontologies/arbi.owl#station4\" \"Unstoring\"))");
		try {
			Thread.sleep(10);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		dc.assertFact("(context (PersonCall \"call06\" \"http://www.arbi.com/ontologies/arbi.owl#station3\" \"PrepareUnstoring\"))");
		
	}
	public void startDemo() {
		dc.assertFact("(context (PersonCall \"call01\" \"http://www.arbi.com/ontologies/arbi.owl#station1\" \"PrepareStoring\"))");
		try {
			Thread.sleep(10);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		dc.assertFact("(context (PersonCall \"call02\" \"http://www.arbi.com/ontologies/arbi.owl#station4\" \"Unstoring\"))");
		try {
			Thread.sleep(10);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		dc.assertFact("(context (PersonCall \"call03\" \"http://www.arbi.com/ontologies/arbi.owl#station3\" \"PrepareUnstoring\"))");

		try {
			Thread.sleep(10);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		dc.assertFact("(context (PersonCall \"call04\" \"http://www.arbi.com/ontologies/arbi.owl#station2\" \"Storing\"))");
		System.out.println("??");
	}
}