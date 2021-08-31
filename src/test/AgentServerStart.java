package test;

import kr.ac.uos.ai.arbi.agent.ArbiAgentExecutor;
import kr.ac.uos.ai.arbi.framework.ArbiFrameworkServer;
import kr.ac.uos.ai.arbi.framework.broker.ActiveMQBroker;
import kr.ac.uos.ai.arbi.interaction.InteractionManager;

public class AgentServerStart {
	
	public static void main(String[] args) {
		System.out.println("-------------Agent Server Start-------------");
		ActiveMQBroker broker = new ActiveMQBroker();
		broker.setURL("stomp://192.168.1.11:61613");
		broker.start();
		
		
//		ArbiFrameworkServer server = new ArbiFrameworkServer();
//		if(args.length == 0) {
//			server.start("tcp://127.0.0.1:61616");
//		} else if(args.length == 1) {
//			server.start(args[0]);
//		}
		
		
//		InteractionManager interactionManager = new InteractionManager();
//		interactionManager.start("tcp://127.0.0.1:61616");
//		ArbiAgentExecutor.execute("tcp://127.0.0.1:61616", InteractionManager.interactionAgentURI, interactionManager);
//		
	}
	
}
