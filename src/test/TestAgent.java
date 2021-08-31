package test;
import kr.ac.uos.ai.arbi.agent.ArbiAgent;
import kr.ac.uos.ai.arbi.agent.ArbiAgentExecutor;

public class TestAgent extends ArbiAgent{
	
	
	public static void main(String[] ar) {
		new TestAgent();
	}
	
	public TestAgent() {
		
		ArbiAgentExecutor.execute("testAgent", this,2);
		this.send("test", "(test)");
		
	}
}
