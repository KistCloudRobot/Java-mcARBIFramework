package test.interactionManager.organizationDispatcher;

import kr.ac.uos.ai.arbi.BrokerType;
import kr.ac.uos.ai.arbi.agent.ArbiAgentExecutor;
import kr.ac.uos.ai.arbi.interaction.InteractionManager;
import kr.ac.uos.ai.arbi.interaction.InteractionManagerBrokerConfiguration;

public class ODInteractionManagerTest {
	public static void main(String args[]) {
//		InteractionManagerBrokerConfiguration.setActiveMQBrokerHost("127.0.0.1");
//		InteractionManagerBrokerConfiguration.setActiveMQBrokerPort(61615);

		InteractionManagerBrokerConfiguration.setZeroMQBrokerHost("127.0.0.1");
		InteractionManagerBrokerConfiguration.setZeroMQBrokerPort(5554);

		InteractionManagerBrokerConfiguration.setSocketBrokerHost("127.0.0.1");
		InteractionManagerBrokerConfiguration.setSocketBrokerPort(5553);
		
		InteractionManager im = new InteractionManager();
		ArbiAgentExecutor.execute("127.0.0.1", 5556, InteractionManager.interactionManagerURI, im, BrokerType.ACTIVEMQ);

	}
}
