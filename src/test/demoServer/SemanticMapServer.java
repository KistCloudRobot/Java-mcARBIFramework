package test.demoServer;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Map;

import kr.ac.uos.ai.arbi.BrokerType;
import kr.ac.uos.ai.arbi.framework.ArbiFrameworkServer;

public class SemanticMapServer {
	public static void main(String[] args) {
//		String host = "127.0.0.1";
//		String host = "192.168.100.10";
//		String host = "172.16.165.141";
		String host = "172.16.165.143";
		int port = 61315;
		
		ArbiFrameworkServer server = new ArbiFrameworkServer(BrokerType.ACTIVEMQ, host, port);
		server.start();
	}
}
