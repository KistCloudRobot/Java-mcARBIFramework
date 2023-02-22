package test.interactionManager;

import kr.ac.uos.ai.arbi.BrokerType;
import kr.ac.uos.ai.arbi.agent.ArbiAgent;
import kr.ac.uos.ai.arbi.agent.ArbiAgentExecutor;
import kr.ac.uos.ai.arbi.agent.logger.LogTiming;
import kr.ac.uos.ai.arbi.agent.logger.LoggerManager;
import kr.ac.uos.ai.arbi.agent.logger.action.ActionBody;
import kr.ac.uos.ai.arbi.agent.logger.action.AgentAction;

public class TestAgent extends ArbiAgent {
	private static final String AGENT_NAME = "testAgent";
	private LoggerManager loggerManager;
	
	public TestAgent() {
		loggerManager = LoggerManager.getInstance();
		loggerManager.initLoggerManager(AGENT_NAME, this);
		loggerManager.registerAction(new AgentAction("testAction", new TestAction()), LogTiming.Later);
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
		AgentAction action = loggerManager.getAction("testAction");
		action.changeAction(true);
		action.execute("(testArgument)");
	}
	
	@Override
	public void onData(String sender, String data) {
		System.out.println("on data : " + data);
	}
	
	public static void main(String[] args) {
		ArbiAgentExecutor.execute("127.0.0.1", 61616, "testAgent", new TestAgent(), BrokerType.ACTIVEMQ);
	}
}
