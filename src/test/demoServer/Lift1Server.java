package test.demoServer;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Map;

import kr.ac.uos.ai.arbi.framework.ArbiFrameworkServer;

public class Lift1Server {
	public static void main(String[] args) {
		String centerURL = "tcp://127.0.0.1:61616";
		String brokerURL = "tcp://127.0.0.1:61116";
		String brokerName = "Lift1";
		
		ArbiFrameworkServer server = new ArbiFrameworkServer(2, brokerName);
		server.start(centerURL, brokerURL);
	}
}
