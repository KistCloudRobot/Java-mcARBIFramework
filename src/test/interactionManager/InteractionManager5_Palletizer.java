package test.interactionManager;

import kr.ac.uos.ai.arbi.BrokerType;
import kr.ac.uos.ai.arbi.agent.ArbiAgentExecutor;
import kr.ac.uos.ai.arbi.interaction.InteractionManager;
import kr.ac.uos.ai.arbi.interaction.InteractionManagerBrokerConfiguration;

public class InteractionManager5_Palletizer {
	public static void main(String args[]) {
//		InteractionManagerBrokerConfiguration.setActiveMQBrokerHost("127.0.0.1");
//		InteractionManagerBrokerConfiguration.setActiveMQBrokerPort(61615);

		InteractionManagerBrokerConfiguration.setZeroMQBrokerHost("127.0.0.1");
		InteractionManagerBrokerConfiguration.setZeroMQBrokerPort(51112);

//		InteractionManagerBrokerConfiguration.setSocketBrokerHost("127.0.0.1");
//		InteractionManagerBrokerConfiguration.setSocketBrokerPort(61613);
		
		InteractionManager im = new InteractionManager();
		ArbiAgentExecutor.execute("172.16.165.158", 61112, InteractionManager.interactionManagerURI, im, BrokerType.ACTIVEMQ);
		// 61112 ~ 61116  61316 
	}
}
