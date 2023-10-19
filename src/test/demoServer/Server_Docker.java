package test.demoServer;

import kr.ac.uos.ai.arbi.BrokerType;
import kr.ac.uos.ai.arbi.framework.ArbiFrameworkServer;

public class Server_Docker {
	public static void main(String[] args) {
		String host = System.getenv("BROKER_ADDRESS");
		String stringPort = System.getenv("BROKER_PORT");
		int port;
		if (stringPort == null) {
			host = "172.16.165.77";
			port = 41314;
		} else {
			port = Integer.parseInt(stringPort);
		}
		
		ArbiFrameworkServer server = new ArbiFrameworkServer(BrokerType.ACTIVEMQ, host, port);
		server.start();
	}
}
