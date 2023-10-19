package test.interactionManager;

import kr.ac.uos.ai.arbi.BrokerType;
import kr.ac.uos.ai.arbi.agent.ArbiAgentExecutor;
import kr.ac.uos.ai.arbi.interaction.InteractionManager;
import kr.ac.uos.ai.arbi.interaction.InteractionManagerBrokerConfiguration;

public class InteractionManager6_SemanticMap {
	public static void main(String args[]) {
//		InteractionManagerBrokerConfiguration.setActiveMQBrokerHost("127.0.0.1");
//		InteractionManagerBrokerConfiguration.setActiveMQBrokerPort(51315);

//		InteractionManagerBrokerConfiguration.setZeroMQBrokerHost("127.0.0.1");
//		InteractionManagerBrokerConfiguration.setZeroMQBrokerPort(51315);

		InteractionManagerBrokerConfiguration.setSocketBrokerHost("127.0.0.1");
		InteractionManagerBrokerConfiguration.setSocketBrokerPort(51315);
		
		// semantic map
		InteractionManager im = new InteractionManager();
		ArbiAgentExecutor.execute("127.0.0.1", 61315, InteractionManager.interactionManagerURI, im, BrokerType.ACTIVEMQ);
		// 61112 ~ 61116  61316 
	}
}
