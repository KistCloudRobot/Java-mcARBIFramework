package test;

import java.util.Scanner;

import kr.ac.uos.ai.arbi.BrokerType;
import kr.ac.uos.ai.arbi.agent.ArbiAgent;
import kr.ac.uos.ai.arbi.agent.ArbiAgentExecutor;
import kr.ac.uos.ai.arbi.agent.logger.LogTiming;
import kr.ac.uos.ai.arbi.agent.logger.LoggerManager;
import kr.ac.uos.ai.arbi.agent.logger.action.AgentAction;

public class ZeroMQMonitorTest extends ArbiAgent{
	
	public ZeroMQMonitorTest() {
		init();
	}
	
	public void init() {
		String host = "127.0.0.1";
//		String host = "192.168.100.10";
//		String host = "172.16.165.141";
		int port = 61116;
		
		ArbiAgentExecutor.execute(host, port,"agent://www.arbi.com/taskManager", this, BrokerType.ZEROMQ);
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
