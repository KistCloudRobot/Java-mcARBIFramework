package test.interactionManager;

import kr.ac.uos.ai.arbi.BrokerType;
import kr.ac.uos.ai.arbi.agent.ArbiAgent;
import kr.ac.uos.ai.arbi.agent.ArbiAgentExecutor;
import kr.ac.uos.ai.arbi.agent.logger.LogTiming;
import kr.ac.uos.ai.arbi.agent.logger.LoggerManager;
import kr.ac.uos.ai.arbi.agent.logger.action.ActionBody;
import kr.ac.uos.ai.arbi.agent.logger.action.AgentAction;
import kr.ac.uos.ai.arbi.ltm.DataSource;

public class TestAgent extends ArbiAgent {
	private static final String AGENT_NAME = "testAgent";
	private LoggerManager loggerManager;
	private DataSource ds;
	
	public TestAgent() {
		loggerManager = LoggerManager.getInstance();
		loggerManager.initLoggerManager(AGENT_NAME, this);
		loggerManager.registerAction(new AgentAction("testAction", new TestAction()), LogTiming.Later);
		ds = new DataSource();
		ds.connect("172.16.165.158", 61316, "ds://testAgent", BrokerType.ACTIVEMQ);
	}
	
	private class TestAction implements ActionBody {
		@Override
		public Object execute(Object o) {
			send(AGENT_NAME, "(hi " + o.toString() + ")");
			return "(testReturn)";
		}
	}
	
	@Override
	public void onStart() {
		try {
			AgentAction action = loggerManager.getAction("testAction");
			action.changeAction(true);
			action.execute("(testArgument)");
			ds.assertFact("(test \"assert\")");
			Thread.sleep(1000);
			ds.updateFact("(update (test $x) (test \"update\"))");
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	@Override
	public void onData(String sender, String data) {
		System.out.println("on data : " + data);
	}
	
	public static void main(String[] args) {
		ArbiAgentExecutor.execute("172.16.165.158", 61116, "testAgent", new TestAgent(), BrokerType.ACTIVEMQ);
	}
}
