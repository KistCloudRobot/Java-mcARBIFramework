package test.interactionManager.testAgent;

import kr.ac.uos.ai.arbi.BrokerType;
import kr.ac.uos.ai.arbi.framework.ArbiFrameworkServer;

public class Robot1TestServer {
	public static void main(String[] args) {
		String host = "127.0.0.1";
		int port = 6666;
		
		ArbiFrameworkServer server = new ArbiFrameworkServer(BrokerType.ACTIVEMQ, host, port);
		server.start();
	}
}
