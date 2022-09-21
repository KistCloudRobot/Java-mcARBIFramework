package test;
import kr.ac.uos.ai.arbi.BrokerType;
import kr.ac.uos.ai.arbi.agent.ArbiAgent;
import kr.ac.uos.ai.arbi.agent.ArbiAgentExecutor;

public class TestAgent extends ArbiAgent{
	
	
	public static void main(String[] ar) {
		new TestAgent();
	}
	
	public TestAgent() {
		
		ArbiAgentExecutor.execute("tcp://127.0.0.1:61316", "test", this, BrokerType.ACTIVEMQ);
		System.out.println("test start!");
		this.send("test", "(test)");
		System.out.println("here");
		
	}
	
	@Override
	public void onData(String sender, String data) {
		System.out.println("on data : " + data);
	}
}
