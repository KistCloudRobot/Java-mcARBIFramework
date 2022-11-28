package test.activeMQ;

import kr.ac.uos.ai.arbi.BrokerType;
import kr.ac.uos.ai.arbi.agent.ArbiAgent;
import kr.ac.uos.ai.arbi.agent.ArbiAgentExecutor;

public class ActiveMQAgentTest extends ArbiAgent {
	
	public ActiveMQAgentTest() {
		String host = "127.0.0.1";
//		String host = "192.168.100.10";
//		String host = "172.16.165.141";
		int port = 61616;
		
		ArbiAgentExecutor.execute(host, port, "activeMQTestAgent", this, BrokerType.ACTIVEMQ);
		System.out.println("test start!");
		this.send("activeMQTestAgent", "(Test 1)");
		this.send("activeMQTestAgent", "(Test 2)");
		this.send("activeMQTestAgent", "(Test 3)");
//		this.request("agent://www.arbi.com/MultiAgentPathFinder", "(MultiRobotPath (RobotPath \"AMR_LIFT1\" 146 103)(RobotPath \"AMR_LIFT2\" 114 145) (RobotPath \"AMR_LIFT3\" 152 124) (RobotPath \"AMR_LIFT4\" 135 102))");
	}
	
	@Override
	public void onData(String sender, String data) {
		System.out.println("on data : " + data);
	}

	public static void main(String[] ar) {
		new ActiveMQAgentTest();
	}
}
