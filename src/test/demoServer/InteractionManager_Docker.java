package test.demoServer;

import kr.ac.uos.ai.arbi.BrokerType;
import kr.ac.uos.ai.arbi.agent.ArbiAgentExecutor;
import kr.ac.uos.ai.arbi.interaction.InteractionManager;
import kr.ac.uos.ai.arbi.interaction.InteractionManagerBrokerConfiguration;

public class InteractionManager_Docker extends InteractionManager {
	public static void main(String[] args) {

		String host = System.getenv("BROKER_ADDRESS");
		String stringPort = System.getenv("BROKER_PORT");
		String zeroMQBrokerPort = System.getenv("INTERACTION_MANAGER_ZEROMQ_PORT");
		int port = Integer.parseInt(stringPort);
		int zmqPort = Integer.parseInt(zeroMQBrokerPort);
		InteractionManagerBrokerConfiguration.setZeroMQBrokerHost(host);
		InteractionManagerBrokerConfiguration.setZeroMQBrokerPort(zmqPort);

		
		InteractionManager im = new InteractionManager();
		ArbiAgentExecutor.execute(host, port, InteractionManager.interactionManagerURI, im, BrokerType.ACTIVEMQ);
	}
}
