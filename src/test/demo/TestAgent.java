package test.demo;
import kr.ac.uos.ai.arbi.agent.ArbiAgent;
import kr.ac.uos.ai.arbi.agent.ArbiAgentExecutor;
import kr.ac.uos.ai.arbi.ltm.DataSource;

public class TestAgent extends ArbiAgent{
	DataSource ds;
	public TestAgent() {
		ArbiAgentExecutor.execute("tcp://0.0.0.0:61115", "agent://www.arbi.com/Lift2/ContextManager", this, 2);
		ds = new DataSource();
		ds.connect("tcp://0.0.0.0:61115", "ds://www.arbi.com/Lift2/ContextManager", 2);
	}
	
	@Override
	public void onStart() {
	}

	@Override
	public void onNotify(String sender, String notification) {
		ds.assertFact(notification);
		// TODO Auto-generated method stub
	}
	public static void main(String[] ar) {
		new TestAgent();
	}
}