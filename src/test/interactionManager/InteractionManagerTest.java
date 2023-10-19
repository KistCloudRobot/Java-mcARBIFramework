package test.interactionManager;

import kr.ac.uos.ai.arbi.BrokerType;
import kr.ac.uos.ai.arbi.agent.ArbiAgentExecutor;
import kr.ac.uos.ai.arbi.interaction.InteractionManager;
import kr.ac.uos.ai.arbi.interaction.InteractionManagerBrokerConfiguration;

public class InteractionManagerTest {
	public static void main(String args[]) {
//		InteractionManagerBrokerConfiguration.setActiveMQBrokerHost("127.0.0.1");
//		InteractionManagerBrokerConfiguration.setActiveMQBrokerPort(61615);

		InteractionManagerBrokerConfiguration.setZeroMQBrokerHost("172.16.165.77");
		InteractionManagerBrokerConfiguration.setZeroMQBrokerPort(8888);

//		InteractionManagerBrokerConfiguration.setSocketBrokerHost("172.16.165.77");
//		InteractionManagerBrokerConfiguration.setSocketBrokerPort(8888);
		
		InteractionManager im = new InteractionManager();
		ArbiAgentExecutor.execute("172.16.165.77", 41314, InteractionManager.interactionManagerURI, im, BrokerType.ACTIVEMQ);
		
	}
}
