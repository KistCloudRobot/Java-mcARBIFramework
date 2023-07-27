package test.interactionManager.organizationDispatcher;

import java.util.Scanner;

import kr.ac.uos.ai.arbi.BrokerType;
import kr.ac.uos.ai.arbi.agent.ArbiAgent;
import kr.ac.uos.ai.arbi.agent.ArbiAgentExecutor;

import kr.ac.uos.ai.arbi.agent.logger.LogTiming;
import kr.ac.uos.ai.arbi.agent.logger.LoggerManager;
import kr.ac.uos.ai.arbi.agent.logger.action.ActionBody;
import kr.ac.uos.ai.arbi.agent.logger.action.AgentAction;

public class ODTestAgent extends ArbiAgent {
	private static final String AGENT_NAME = "TestOD";
	private LoggerManager loggerManager;
	private String ip = "127.0.0.1";
	
	public ODTestAgent() {
		loggerManager = LoggerManager.getInstance();
		loggerManager.initLoggerManager(AGENT_NAME, this);
		loggerManager.registerAction(new AgentAction("RoleSpecification", new RoleSpecificationAction()), LogTiming.Later);
		loggerManager.registerAction(new AgentAction("RoleServiceSpecification", new RoleServiceSpecificationAction()), LogTiming.Later);
		loggerManager.registerAction(new AgentAction("MonitorAgent", new MonitorAgentAction()), LogTiming.Later);
		loggerManager.registerAction(new AgentAction("AssignRole", new AssignRole()), LogTiming.Later);

	}
	
	@Override
	public void onStart() {
		AgentAction action = null;
		
		int monitorAgentActionDelayInMilliseconds = 1000;
		
		action = loggerManager.getAction("MonitorAgent");
		action.changeAction(true);
		
		try {
			action.execute("TestRobot1");
			Thread.sleep(monitorAgentActionDelayInMilliseconds);
			
			action.execute("local");
			Thread.sleep(monitorAgentActionDelayInMilliseconds);
			action.execute("amr_lift1");
			Thread.sleep(monitorAgentActionDelayInMilliseconds);
			action.execute("amr_lift2");
			Thread.sleep(monitorAgentActionDelayInMilliseconds);
			action.execute("amr_lift3");
			Thread.sleep(monitorAgentActionDelayInMilliseconds);
			action.execute("amr_lift4");
			Thread.sleep(monitorAgentActionDelayInMilliseconds);
			action.execute("palletizer1");
			Thread.sleep(monitorAgentActionDelayInMilliseconds);
			action.execute("semantic_map");
			
			action = loggerManager.getAction("RoleSpecification");
			action.changeAction(true);
			Thread.sleep(monitorAgentActionDelayInMilliseconds);
			action.execute("");
			
			action = loggerManager.getAction("RoleServiceSpecification");
			action.changeAction(true);
			Thread.sleep(monitorAgentActionDelayInMilliseconds);
			action.execute("");

			action = loggerManager.getAction("AssignRole");
			action.changeAction(true);
			Thread.sleep(monitorAgentActionDelayInMilliseconds);
			action.execute("");
			
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	private class RoleSpecificationAction implements ActionBody {
		@Override
		public Object execute(Object o) {
//			send(AGENT_NAME, "(hi " + o.toString() + ")");
			return "(RoleSpecification \"{&quot;prefixes&quot;:{&quot;PREFIXES&quot;:[{&quot;fac&quot;:&quot;&lt;http://https://www.arbi.com/factory#&gt;&quot;}]},&quot;policyList&quot;:[&quot;Efficiency($efficiency)&quot;,&quot;Safety($safety)&quot;],&quot;interactionMap&quot;:{&quot;RequestPalletTransportation&quot;:{&quot;interactionVariable&quot;:{&quot;task&quot;:&quot;PalletTransported($palletID,$startLocation,$endLocation)&quot;},&quot;initiator&quot;:&quot;LogisticManager&quot;,&quot;interactionName&quot;:&quot;RequestPalletTransportation&quot;,&quot;interactionTemplateName&quot;:&quot;TaskAssign&quot;,&quot;participants&quot;:{&quot;LogisticManager&quot;:&quot;TaskAssigner&quot;,&quot;Carrier&quot;:&quot;taskExecutor&quot;}},&quot;ReportPalletTransportation&quot;:{&quot;interactionVariable&quot;:{&quot;task&quot;:&quot;PalletTransported($palletID,$startLocation,$endLocation)&quot;},&quot;initiator&quot;:&quot;Carrier&quot;,&quot;interactionName&quot;:&quot;ReportPalletTransportation&quot;,&quot;interactionTemplateName&quot;:&quot;TaskReport&quot;,&quot;participants&quot;:{&quot;LogisticManager&quot;:&quot;TaskAssigner&quot;,&quot;Carrier&quot;:&quot;taskExecutor&quot;}}},&quot;normList&quot;:[],&quot;roleList&quot;:[{&quot;permissionRead&quot;:[&quot;BatteryRemain($robotID,$remain)&quot;,&quot;RobotAt($robotID,$vertex1,$vertex2)&quot;],&quot;safety&quot;:[],&quot;skill&quot;:[&quot;OnStation($objectID,$station)&quot;],&quot;roleName&quot;:&quot;LogisticManager&quot;,&quot;liveness&quot;:[&quot;RequestPalletTransportation&quot;,&quot;ReportPalletTransportation&quot;],&quot;permissionCreate&quot;:[&quot;OnStation($objectID,$station)&quot;],&quot;permissionChange&quot;:[],&quot;interactionsAndActivites&quot;:[&quot;RequestPalletTransportation&quot;,&quot;ReportPalletTransportation&quot;]},{&quot;permissionRead&quot;:[&quot;OnStation($objectID,$station)&quot;],&quot;safety&quot;:[&quot;BatteryRemain($robotID,$remain)&quot;],&quot;skill&quot;:[&quot;ObjectGrabbed($objectLocation,$objectID)&quot;,&quot;MovedToLocation($objectID)&quot;,&quot;ObjectPlaced($objectLocation,$objectID)&quot;,&quot;BatteryRemain($robotID,$remain)&quot;,&quot;OnRobotTaskStatus($robotID,$taskID,$taskStatus)&quot;,&quot;OnRobotBatteryStatus($robotID,$chargeStatus)&quot;,&quot;BatteryRemain($robotID,$remain)&quot;,&quot;RobotAt($robotID,$vertex1,$vertex2)&quot;],&quot;roleName&quot;:&quot;Carrier&quot;,&quot;liveness&quot;:[&quot;RequestPalletStoringTransportation&quot;,&quot;ObjectGrabbed&quot;,&quot;MovedToLocation&quot;,&quot;ObjectPlaced&quot;,&quot;ReportPalletStoringTransportation&quot;],&quot;permissionCreate&quot;:[&quot;OnRobotTaskStatus($robotID,$taskID,$taskStatus)&quot;,&quot;OnRobotBatteryStatus($robotID,$chargeStatus)&quot;,&quot;BatteryRemain($robotID,$remain)&quot;,&quot;RobotAt($robotID,$vertex1,$vertex2)&quot;],&quot;permissionChange&quot;:[],&quot;interactionsAndActivites&quot;:[&quot;ObjectGrabbed($objectLocation,$objectID)&quot;,&quot;MovedToLocation($objectID)&quot;,&quot;ObjectPlaced($objectLocation,$objectID)&quot;,&quot;RequestPalletTransportation&quot;,&quot;ReportPalletTransportation&quot;]}]}\")";
		}
	}
	
	private class RoleServiceSpecificationAction implements ActionBody {
		@Override
		public Object execute(Object o) {
//			send(AGENT_NAME, "(hi " + o.toString() + ")");
			return "(RoleServiceSpecification \"{&quot;multiRobotTaskName&quot;:&quot;logistics&quot;,&quot;roleServiceList&quot;:[{&quot;contextList&quot;:[{&quot;condition&quot;:[{&quot;property&quot;:&quot;variableType&quot;,&quot;range&quot;:&quot;fac:robot&quot;,&quot;object&quot;:&quot;$robotID&quot;},{&quot;property&quot;:&quot;variableType&quot;,&quot;range&quot;:&quot;fac:battery&quot;,&quot;object&quot;:&quot;$remain&quot;}],&quot;context&quot;:&quot;BatteryRemain($robotID,$remain)&quot;,&quot;permission&quot;:&quot;read&quot;},{&quot;condition&quot;:[{&quot;property&quot;:&quot;variableType&quot;,&quot;range&quot;:&quot;fac:robot&quot;,&quot;object&quot;:&quot;$robotID&quot;},{&quot;property&quot;:&quot;variableType&quot;,&quot;range&quot;:&quot;fac:vertex&quot;,&quot;object&quot;:&quot;$vertex1&quot;},{&quot;property&quot;:&quot;variableType&quot;,&quot;range&quot;:&quot;fac:vertex&quot;,&quot;object&quot;:&quot;$vertex2&quot;}],&quot;context&quot;:&quot;RobotAt($robotID,$vertex1,$vertex2)&quot;,&quot;permission&quot;:&quot;read&quot;},{&quot;condition&quot;:[{&quot;property&quot;:&quot;variableType&quot;,&quot;range&quot;:&quot;fac:object&quot;,&quot;object&quot;:&quot;$objectID&quot;},{&quot;property&quot;:&quot;variableType&quot;,&quot;range&quot;:&quot;fac:station&quot;,&quot;object&quot;:&quot;$station&quot;}],&quot;context&quot;:&quot;OnStation($objectID,$station)&quot;,&quot;permission&quot;:&quot;create&quot;}],&quot;policyList&quot;:[&quot;Efficiency($efficiency)&quot;,&quot;Safety($safety)&quot;],&quot;roleServiceName&quot;:&quot;LogisticManager&quot;,&quot;taskList&quot;:[{&quot;condition&quot;:[{&quot;property&quot;:&quot;interactionTemplate&quot;,&quot;range&quot;:&quot;TaskAssign&quot;,&quot;object&quot;:&quot;?model&quot;},{&quot;property&quot;:&quot;participantAs&quot;,&quot;range&quot;:&quot;TaskAssigner&quot;,&quot;object&quot;:&quot;LogisticManager&quot;},{&quot;property&quot;:&quot;interactionVariable&quot;,&quot;range&quot;:&quot;PalletTransported($palletID,$startLocation,$endLocation)&quot;,&quot;object&quot;:&quot;task&quot;}],&quot;task&quot;:&quot;RequestPalletTransportation&quot;},{&quot;condition&quot;:[{&quot;property&quot;:&quot;interactionTemplate&quot;,&quot;range&quot;:&quot;TaskReport&quot;,&quot;object&quot;:&quot;?model&quot;},{&quot;property&quot;:&quot;participantAs&quot;,&quot;range&quot;:&quot;TaskAssigner&quot;,&quot;object&quot;:&quot;LogisticManager&quot;},{&quot;property&quot;:&quot;interactionVariable&quot;,&quot;range&quot;:&quot;PalletTransported($palletID,$startLocation,$endLocation)&quot;,&quot;object&quot;:&quot;task&quot;}],&quot;task&quot;:&quot;ReportPalletTransportation&quot;}],&quot;knowledgeList&quot;:[{&quot;knowledge&quot;:&quot;&lt;http:\\/\\/https:\\/\\/www.arbi.com\\/factory#&gt;&quot;}]},{&quot;contextList&quot;:[{&quot;condition&quot;:[{&quot;property&quot;:&quot;variableType&quot;,&quot;range&quot;:&quot;fac:object&quot;,&quot;object&quot;:&quot;$objectID&quot;},{&quot;property&quot;:&quot;variableType&quot;,&quot;range&quot;:&quot;fac:station&quot;,&quot;object&quot;:&quot;$station&quot;}],&quot;context&quot;:&quot;OnStation($objectID,$station)&quot;,&quot;permission&quot;:&quot;read&quot;},{&quot;condition&quot;:[{&quot;property&quot;:&quot;variableType&quot;,&quot;range&quot;:&quot;fac:robot&quot;,&quot;object&quot;:&quot;$robotID&quot;},{&quot;property&quot;:&quot;variableType&quot;,&quot;range&quot;:&quot;fac:task&quot;,&quot;object&quot;:&quot;$taskID&quot;},{&quot;property&quot;:&quot;variableType&quot;,&quot;range&quot;:&quot;fac:taskStatus&quot;,&quot;object&quot;:&quot;$taskStatus&quot;}],&quot;context&quot;:&quot;OnRobotTaskStatus($robotID,$taskID,$taskStatus)&quot;,&quot;permission&quot;:&quot;create&quot;},{&quot;condition&quot;:[{&quot;property&quot;:&quot;variableType&quot;,&quot;range&quot;:&quot;fac:robot&quot;,&quot;object&quot;:&quot;$robotID&quot;},{&quot;property&quot;:&quot;variableType&quot;,&quot;range&quot;:&quot;fac:chargeStatus&quot;,&quot;object&quot;:&quot;$chargeStatus&quot;}],&quot;context&quot;:&quot;OnRobotBatteryStatus($robotID,$chargeStatus)&quot;,&quot;permission&quot;:&quot;create&quot;},{&quot;condition&quot;:[{&quot;property&quot;:&quot;variableType&quot;,&quot;range&quot;:&quot;fac:robot&quot;,&quot;object&quot;:&quot;$robotID&quot;},{&quot;property&quot;:&quot;variableType&quot;,&quot;range&quot;:&quot;fac:battery&quot;,&quot;object&quot;:&quot;$remain&quot;}],&quot;context&quot;:&quot;BatteryRemain($robotID,$remain)&quot;,&quot;permission&quot;:&quot;create&quot;},{&quot;condition&quot;:[{&quot;property&quot;:&quot;variableType&quot;,&quot;range&quot;:&quot;fac:robot&quot;,&quot;object&quot;:&quot;$robotID&quot;},{&quot;property&quot;:&quot;variableType&quot;,&quot;range&quot;:&quot;fac:vertex&quot;,&quot;object&quot;:&quot;$vertex1&quot;},{&quot;property&quot;:&quot;variableType&quot;,&quot;range&quot;:&quot;fac:vertex&quot;,&quot;object&quot;:&quot;$vertex2&quot;}],&quot;context&quot;:&quot;RobotAt($robotID,$vertex1,$vertex2)&quot;,&quot;permission&quot;:&quot;create&quot;}],&quot;policyList&quot;:[&quot;Efficiency($efficiency)&quot;,&quot;Safety($safety)&quot;],&quot;roleServiceName&quot;:&quot;Carrier&quot;,&quot;taskList&quot;:[{&quot;condition&quot;:[{&quot;property&quot;:&quot;variableType&quot;,&quot;range&quot;:&quot;fac:station&quot;,&quot;object&quot;:&quot;$objectLocation&quot;},{&quot;property&quot;:&quot;variableType&quot;,&quot;range&quot;:&quot;fac:object&quot;,&quot;object&quot;:&quot;$objectID&quot;}],&quot;task&quot;:&quot;ObjectGrabbed($objectLocation,$objectID)&quot;},{&quot;condition&quot;:[{&quot;property&quot;:&quot;variableType&quot;,&quot;range&quot;:&quot;fac:object&quot;,&quot;object&quot;:&quot;$objectID&quot;}],&quot;task&quot;:&quot;MovedToLocation($objectID)&quot;},{&quot;condition&quot;:[{&quot;property&quot;:&quot;variableType&quot;,&quot;range&quot;:&quot;fac:station&quot;,&quot;object&quot;:&quot;$objectLocation&quot;},{&quot;property&quot;:&quot;variableType&quot;,&quot;range&quot;:&quot;fac:object&quot;,&quot;object&quot;:&quot;$objectID&quot;}],&quot;task&quot;:&quot;ObjectPlaced($objectLocation,$objectID)&quot;},{&quot;condition&quot;:[{&quot;property&quot;:&quot;interactionTemplate&quot;,&quot;range&quot;:&quot;TaskAssign&quot;,&quot;object&quot;:&quot;?model&quot;},{&quot;property&quot;:&quot;participantAs&quot;,&quot;range&quot;:&quot;taskExecutor&quot;,&quot;object&quot;:&quot;Carrier&quot;},{&quot;property&quot;:&quot;interactionVariable&quot;,&quot;range&quot;:&quot;PalletTransported($palletID,$startLocation,$endLocation)&quot;,&quot;object&quot;:&quot;task&quot;}],&quot;task&quot;:&quot;RequestPalletTransportation&quot;},{&quot;condition&quot;:[{&quot;property&quot;:&quot;interactionTemplate&quot;,&quot;range&quot;:&quot;TaskReport&quot;,&quot;object&quot;:&quot;?model&quot;},{&quot;property&quot;:&quot;participantAs&quot;,&quot;range&quot;:&quot;taskExecutor&quot;,&quot;object&quot;:&quot;Carrier&quot;},{&quot;property&quot;:&quot;interactionVariable&quot;,&quot;range&quot;:&quot;PalletTransported($palletID,$startLocation,$endLocation)&quot;,&quot;object&quot;:&quot;task&quot;}],&quot;task&quot;:&quot;ReportPalletTransportation&quot;},{&quot;condition&quot;:[{&quot;property&quot;:&quot;variableType&quot;,&quot;range&quot;:&quot;fac:robot&quot;,&quot;object&quot;:&quot;$robotID&quot;},{&quot;property&quot;:&quot;variableType&quot;,&quot;range&quot;:&quot;fac:battery&quot;,&quot;object&quot;:&quot;$remain&quot;}],&quot;task&quot;:&quot;BatteryRemain($robotID,$remain)&quot;}],&quot;knowledgeList&quot;:[{&quot;knowledge&quot;:&quot;&lt;http:\\/\\/https:\\/\\/www.arbi.com\\/factory#&gt;&quot;}]}]}\")";
		}
	}
	
	private class MonitorAgentAction implements ActionBody {
		@Override
		public Object execute(Object o) {
			if(o.toString().equals("TestRobot1")) {
				return "(MonitorAgent \"TestRobot1\" \"tcp://127.0.0.1:6664\")";
			}else if(o.toString().equals("TestRobot2")) {
				return "(MonitorAgent \"TestRobot2\" \"tcp://127.0.0.1:7664\")";
			}else if(o.toString().equals("local")) {
				return "(MonitorAgent \"local\" \"tcp://127.0.0.1:51316\")";
			}else if(o.toString().equals("amr_lift1")) {
				return "(MonitorAgent \"amr_lift1\" \"tcp://127.0.0.1:51116\")";
			}else if(o.toString().equals("amr_lift2")) {
				return "(MonitorAgent \"amr_lift2\" \"tcp://127.0.0.1:51115\")";
			}else if(o.toString().equals("amr_lift3")) {
				return "(MonitorAgent \"amr_lift3\" \"tcp://127.0.0.1:51114\")";
			}else if(o.toString().equals("amr_lift4")) {
				return "(MonitorAgent \"amr_lift4\" \"tcp://127.0.0.1:51113\")";
			}else if(o.toString().equals("palletizer1")) {
				return "(MonitorAgent \"palletizer1\" \"tcp://127.0.0.1:51112\")";
			}else if(o.toString().equals("semantic_map")) {
				return "(MonitorMap \"semantic_map\" \"tcp://127.0.0.1:51315\")";
			}
			else {
				return "";
			}
		}
	}
	
	private class AssignRole implements ActionBody {
		@Override
		public Object execute(Object o) {
//			send(AGENT_NAME, "(AssignRole \"testRoleRobot1\")");
			return "(AssignRole \"Carrier\" \"TestRobot1\")";
		}
	}
	
	
	@Override
	public void onData(String sender, String data) {
		System.out.println("on data : " + data);
	}
	
	public static void main(String[] args) {
		ArbiAgentExecutor.execute("127.0.0.1", 5556, AGENT_NAME, new ODTestAgent(), BrokerType.ACTIVEMQ);
	}
}
