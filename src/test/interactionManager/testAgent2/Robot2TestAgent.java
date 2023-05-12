package test.interactionManager.testAgent2;

import java.util.Scanner;

import kr.ac.uos.ai.arbi.BrokerType;
import kr.ac.uos.ai.arbi.agent.ArbiAgent;
import kr.ac.uos.ai.arbi.agent.ArbiAgentExecutor;
import kr.ac.uos.ai.arbi.agent.logger.LogTiming;
import kr.ac.uos.ai.arbi.agent.logger.LoggerManager;
import kr.ac.uos.ai.arbi.agent.logger.action.ActionBody;
import kr.ac.uos.ai.arbi.agent.logger.action.AgentAction;

public class Robot2TestAgent extends ArbiAgent {
	private static final String AGENT_NAME = "TestRobot2";
	private LoggerManager loggerManager;
	
	public Robot2TestAgent() {
		loggerManager = LoggerManager.getInstance();
		loggerManager.initLoggerManager(AGENT_NAME, this);
		loggerManager.registerAction(new AgentAction("TestAction", new TestAction()), LogTiming.Later);
		loggerManager.registerAction(new AgentAction("AssignRoleAction", new AssignRoleAction()), LogTiming.Later);
	}
	
	private class TestAction implements ActionBody {
		@Override
		public Object execute(Object o) {
			send(AGENT_NAME, "(TestRobotStatus)");
			return "(TestReturnRobot2)";
		}
	}
	
	private class AssignRoleAction implements ActionBody {
		@Override
		public Object execute(Object o) {
			send(AGENT_NAME, "(AssignRole)");
			return "(TestReturnRobot2AssignRole)";
		}
	}
	
	
	@Override
	public void onStart() {
		while(true) {
			Scanner sc = new Scanner(System.in);
			String input = sc.nextLine();
			AgentAction action = null;
			switch(input) {
			case "1":
				action = loggerManager.getAction("AssignRoleAction");
				action.changeAction(true);
				action.execute("(TestArgumentOD)");
				break;
			case "2":
				break;
			case "3":
				break;
			default:
				break;
			}
		}
	}
	
	@Override
	public void onData(String sender, String data) {
		System.out.println("on data : " + data);
	}
	
	public static void main(String[] args) {
		ArbiAgentExecutor.execute("127.0.0.1", 7666, AGENT_NAME, new Robot2TestAgent(), BrokerType.ACTIVEMQ);
	}
}
