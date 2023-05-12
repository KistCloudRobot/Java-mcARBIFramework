package test.interactionManager.testAgent;

import java.util.Random;
import java.util.Scanner;

import kr.ac.uos.ai.arbi.BrokerType;
import kr.ac.uos.ai.arbi.agent.ArbiAgent;
import kr.ac.uos.ai.arbi.agent.ArbiAgentExecutor;
import kr.ac.uos.ai.arbi.agent.logger.LogTiming;
import kr.ac.uos.ai.arbi.agent.logger.LoggerManager;
import kr.ac.uos.ai.arbi.agent.logger.action.ActionBody;
import kr.ac.uos.ai.arbi.agent.logger.action.AgentAction;

public class Robot1TestAgent extends ArbiAgent {
	private static final String AGENT_NAME = "TestRobot1";
	private LoggerManager loggerManager;
	
	public Robot1TestAgent() {
		loggerManager = LoggerManager.getInstance();
		loggerManager.initLoggerManager(AGENT_NAME, this);
		loggerManager.registerAction(new AgentAction("TestAction", new TestAction()), LogTiming.Later);
		loggerManager.registerAction(new AgentAction("AchieveGoalAction", new AchiveGoalAction()), LogTiming.Later);
		loggerManager.registerAction(new AgentAction("UnpostGoal", new UnpostAction()), LogTiming.Later);
		loggerManager.registerAction(new AgentAction("IntendGoal", new IntendAction()), LogTiming.Later);
	}
	
	private class IntendAction implements ActionBody {
		@Override
		public Object execute(Object o) {
			send(AGENT_NAME, "(IntendGoal \"testRoleRobot1\")");
	        Random random = new Random();
			int randomInt = random.nextInt(1000);
			return "(IntendGoal \"testIntendGoalName"+randomInt+"\")";
		}
	}
	
	private class UnpostAction implements ActionBody {
		@Override
		public Object execute(Object o) {
			send(AGENT_NAME, "(UnpostGoal \"testRoleRobot1\")");
	        Random random = new Random();
			int randomInt = random.nextInt(1000);
			return "(UnpostGoal \"testUnpostActionGoalName"+randomInt+"\")"; 
			
		}
	}
	

	
	private class TestAction implements ActionBody {
		@Override
		public Object execute(Object o) {
			send(AGENT_NAME, "(TestRobotStatus)");
			return "(TestReturnRobot1)";
		}
	}
	
	private class AchiveGoalAction implements ActionBody {
		@Override
		public Object execute(Object o) {
//			send(AGENT_NAME, "(UnpostGoal \"testGoalName\")");
			return "(testGoalName)";
		}
	}
	
	@Override
	public void onStart() {
		while(true) {
			AgentAction action = null;
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			action = loggerManager.getAction("UnpostGoal");
			action.changeAction(true);
			action.execute("");
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			action = loggerManager.getAction("IntendGoal");
			action.changeAction(true);
			action.execute("");
			
		}
//		while(true) {
//			Scanner sc = new Scanner(System.in);
//			String input = sc.nextLine();
//			AgentAction action = null;
//			switch(input) {
//			case "4":
//				action = loggerManager.getAction("TestAction");
//				action.changeAction(true);
//				action.execute("(TestArgumentOD)");				
//				break;
//			case "3":
//				action = loggerManager.getAction("AchieveGoalAction");
//				action.changeAction(true);
//				action.execute("(TestArgumentOD)");			
//				break;
//			case "2":
//				action = loggerManager.getAction("UnpostGoal");
//				action.changeAction(true);
//				action.execute("(TestArgumentOD)");			
//				break;
//			case "1":
//				action = loggerManager.getAction("IntendGoal");
//				action.changeAction(true);
//				action.execute("(TestArgumentOD)");			
//				break;
//			default:
//				break;
//			}
//		}
	}
	
	@Override
	public void onData(String sender, String data) {
		System.out.println("on data : " + data);
	}
	
	public static void main(String[] args) {
		ArbiAgentExecutor.execute("127.0.0.1", 6666, AGENT_NAME, new Robot1TestAgent(), BrokerType.ACTIVEMQ);
	}
}
