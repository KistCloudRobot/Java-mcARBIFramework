package test;

import kr.ac.uos.ai.arbi.BrokerType;
import kr.ac.uos.ai.arbi.agent.ArbiAgentExecutor;
import kr.ac.uos.ai.arbi.framework.ArbiFrameworkServer;
import kr.ac.uos.ai.arbi.framework.broker.ActiveMQBroker;
import kr.ac.uos.ai.arbi.interaction.InteractionManager;

public class AgentServerStart {
	
	public static void main(String[] args) {
		System.out.println("-------------Agent Server Start-------------");
		
		
		ArbiFrameworkServer server = new ArbiFrameworkServer(BrokerType.ACTIVEMQ, "127.0.0.1", 61616);
		server.start();
	
		
//		InteractionManager interactionManager = new InteractionManager();
//		interactionManager.start("tcp://127.0.0.1:61616");
//		ArbiAgentExecutor.execute("tcp://127.0.0.1:61616", InteractionManager.interactionAgentURI, interactionManager);
//		
	}
	
}
