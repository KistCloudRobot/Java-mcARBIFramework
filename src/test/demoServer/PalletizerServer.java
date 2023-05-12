package test.demoServer;

import kr.ac.uos.ai.arbi.BrokerType;
import kr.ac.uos.ai.arbi.framework.ArbiFrameworkServer;

public class PalletizerServer {
	public static void main(String[] args) {
//		String host = "127.0.0.1";
//		String host = "192.168.100.10";
		String host = "172.16.165.164";
//		String host = "172.16.165.141";
//		String host = "172.16.165.158";
		int port = 61112;
		
		ArbiFrameworkServer server = new ArbiFrameworkServer(BrokerType.ACTIVEMQ, host, port);
		server.start();
	}
}
