package test;
import kr.ac.uos.ai.arbi.BrokerType;
import kr.ac.uos.ai.arbi.agent.ArbiAgent;
import kr.ac.uos.ai.arbi.agent.ArbiAgentExecutor;

public class TestAgent extends ArbiAgent{
	
	
	public static void main(String[] ar) {
		new TestAgent();
	}
	
	public TestAgent() {
		
		ArbiAgentExecutor.execute("tcp://127.0.0.1:61316", "test8", this, BrokerType.ZEROMQ);
		System.out.println("test start!");
		while(true) {
			this.send("test7", "(test8)");
		}
		
	}
	
	@Override
	public void onData(String sender, String data) {
		System.out.println("on data : " + data);
	}
}
