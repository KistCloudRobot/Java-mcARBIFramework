package test;

import java.util.Scanner;

import kr.ac.uos.ai.arbi.BrokerType;
import kr.ac.uos.ai.arbi.agent.ArbiAgent;
import kr.ac.uos.ai.arbi.agent.ArbiAgentExecutor;
import kr.ac.uos.ai.arbi.agent.logger.AgentAction;
import kr.ac.uos.ai.arbi.agent.logger.LogTiming;
import kr.ac.uos.ai.arbi.agent.logger.LoggerManager;

public class ZeroMQMonitorTest extends ArbiAgent{
	
	public ZeroMQMonitorTest() {
		init();
	}
	
	public void init() {
		ArbiAgentExecutor.execute("tcp://192.168.0.2:61616","agent://www.arbi.com/taskManager", this, BrokerType.ZEROMQ);
		String test = "(testArg)";
		AgentAction owlLoadAction = new AgentAction("WorldModel", new TestAction());
		LoggerManager.getInstance().registerAction(owlLoadAction, LogTiming.NonAction);
		
		
		Scanner in = new Scanner(System.in);
		in.nextLine();
		
		owlLoadAction.execute(test);
	}
	
	public static void main(String[] ar) {
		new ZeroMQMonitorTest();
	}
}
