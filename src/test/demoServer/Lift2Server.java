package test.demoServer;

import kr.ac.uos.ai.arbi.BrokerType;
import kr.ac.uos.ai.arbi.framework.ArbiFrameworkServer;

public class Lift2Server {
	public static void main(String[] args) {
//		String host = "127.0.0.1";
		String host = "172.16.165.164";
//		String host = "172.16.165.141";
//		String host = "172.16.165.158";
		int port = 61115;
		
		ArbiFrameworkServer server = new ArbiFrameworkServer(BrokerType.ACTIVEMQ, host, port);
		server.start();
	}
}
