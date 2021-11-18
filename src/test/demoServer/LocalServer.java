package test.demoServer;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Map;

import kr.ac.uos.ai.arbi.framework.ArbiFrameworkServer;

public class LocalServer {
	public static void main(String[] args) {
		String centerURL = "tcp://192.168.0.14:61616";
		String brokerURL = "tcp://192.168.0.14:61313";
		String brokerName = "Local";
		
		ArbiFrameworkServer server = new ArbiFrameworkServer(2, brokerName);
		server.start(centerURL, brokerURL);
	}
}
