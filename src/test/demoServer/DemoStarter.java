package test.demoServer;

import kr.ac.uos.ai.arbi.BrokerType;
import kr.ac.uos.ai.arbi.agent.ArbiAgent;
import kr.ac.uos.ai.arbi.agent.ArbiAgentExecutor;
import kr.ac.uos.ai.arbi.ltm.DataSource;

public class DemoStarter extends ArbiAgent {

	private final String MY_ADDRESS = "agent://www.arbi.com/demoStarter";
	DataSource dc;
	private String ip = "tcp://172.16.165.141:61316";
	
	public DemoStarter() {
		
		ArbiAgentExecutor.execute(ip, MY_ADDRESS, this, BrokerType.ZEROMQ);
	}
	
	public DemoStarter(String brokerAddress) {
		
		this.ip = brokerAddress;
		ArbiAgentExecutor.execute(brokerAddress, MY_ADDRESS, this, BrokerType.ZEROMQ);

		//communicator.getBaseChannel().request("agent://www.mcarbi.com/organizationDispatcher", "(DiapatchOrganization)");
	}
	
	
	@Override
	public void onStart() {
		
		dc = new DataSource();
		
		dc.connect(ip, "dc://www.agent.com/demoStarter", BrokerType.ZEROMQ);
		
		//send("agent://www.arbi.com/TaskAllocator", "(hi)");
	}
	
	public static void main(String[] args) {

		String brokerAddress;
		String robotID;
		if(args.length == 0) {
			brokerAddress = "tcp://127.0.0.1:61316";
			robotID = "Local";	
		} else {
			robotID = args[0];
			brokerAddress = args[1];
		}
		
		DemoStarter demo = new DemoStarter(brokerAddress);

		//Scanner in = new Scanner(System.in);
		//in.nextLine();
		
		demo.startDemo();
	}
	
	public void startDemo() {
		dc.assertFact("(context (PersonCall \"call01\" \"http://www.arbi.com/ontologies/arbi.owl#station2\" \"Storing\"))");
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		dc.assertFact("(context (PersonCall \"call02\" \"http://www.arbi.com/ontologies/arbi.owl#station4\" \"Unstoring\"))");

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		dc.assertFact("(context (PersonCall \"call03\" \"http://www.arbi.com/ontologies/arbi.owl#station3\" \"PrepareUnstoring\"))");

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		dc.assertFact("(context (PersonCall \"call04\" \"http://www.arbi.com/ontologies/arbi.owl#station1\" \"PrepareStoring\"))");
	}
}