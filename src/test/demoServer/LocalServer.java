package test.demoServer;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Map;

import kr.ac.uos.ai.arbi.framework.ArbiFrameworkServer;

public class LocalServer {
	public static void main(String[] args) {
		String brokerURL = "tcp://172.16.165.141:61316";
		String brokerName = "Local";
		
		ArbiFrameworkServer server = new ArbiFrameworkServer(2, brokerName);
		server.start(brokerURL, brokerURL);
	}
}
