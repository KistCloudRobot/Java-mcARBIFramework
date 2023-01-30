package test;
import kr.ac.uos.ai.arbi.BrokerType;
import kr.ac.uos.ai.arbi.agent.ArbiAgent;
import kr.ac.uos.ai.arbi.agent.ArbiAgentExecutor;
import kr.ac.uos.ai.arbi.framework.ArbiFrameworkServer;

public class TestAgent extends ArbiAgent{
	
	
	public static void main(String[] ar) {
		new TestAgent();
	}
	
	public TestAgent() {
		String host = "127.0.0.1";
//		String host = "192.168.100.10";
//		String host = "172.16.165.141";
		int port = 61616;
		
		ArbiAgentExecutor.execute(host, port, "test81", this, BrokerType.ACTIVEMQ);
		System.out.println("test start!");
		this.send("test81", "(Move \"AMR_LIFT2+Move_120\" (Path 120))");
		this.send("test81", "(Move \"AMR_LIFT2+Move_120\" (Path 120))");
		this.send("test81", "(Move \"AMR_LIFT2+Move_120\" (Path 120))");
//		this.request("agent://www.arbi.com/MultiAgentPathFinder", "(MultiRobotPath (RobotPath \"AMR_LIFT1\" 146 103)(RobotPath \"AMR_LIFT2\" 114 145) (RobotPath \"AMR_LIFT3\" 152 124) (RobotPath \"AMR_LIFT4\" 135 102))");
	}
	
	@Override
	public void onData(String sender, String data) {
		System.out.println("on data : " + data);
	}
}
