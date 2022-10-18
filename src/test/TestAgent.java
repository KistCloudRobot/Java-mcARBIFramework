package test;
import kr.ac.uos.ai.arbi.BrokerType;
import kr.ac.uos.ai.arbi.agent.ArbiAgent;
import kr.ac.uos.ai.arbi.agent.ArbiAgentExecutor;

public class TestAgent extends ArbiAgent{
	
	
	public static void main(String[] ar) {
		new TestAgent();
	}
	
	public TestAgent() {
		
		ArbiAgentExecutor.execute("tcp://172.16.165.141:61316", "test8", this, BrokerType.ZEROMQ);
		
		this.request("agent://www.arbi.com/MultiAgentPathFinder", "(MultiRobotPath (RobotPath \"AMR_LIFT1\" 146 103)(RobotPath \"AMR_LIFT2\" 114 145) (RobotPath \"AMR_LIFT3\" 152 124) (RobotPath \"AMR_LIFT4\" 135 102))");
	}
	
	@Override
	public void onData(String sender, String data) {
		System.out.println("on data : " + data);
	}
}
