package test.interactionManager.organizationDispatcher;

import kr.ac.uos.ai.arbi.BrokerType;
import kr.ac.uos.ai.arbi.framework.ArbiFrameworkServer;

public class ODTestServer {
	public static void main(String[] args) {
		String host = "127.0.0.1";
		int port = 5556;
		
		ArbiFrameworkServer server = new ArbiFrameworkServer(BrokerType.ACTIVEMQ, host, port);
		server.start();
	}
}
