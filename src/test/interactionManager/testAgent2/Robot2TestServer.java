package test.interactionManager.testAgent2;

import kr.ac.uos.ai.arbi.BrokerType;
import kr.ac.uos.ai.arbi.framework.ArbiFrameworkServer;

public class Robot2TestServer {
	public static void main(String[] args) {
		String host = "127.0.0.1";
		int port = 7666;
		
		ArbiFrameworkServer server = new ArbiFrameworkServer(BrokerType.ACTIVEMQ, host, port);
		server.start();
	}
}
