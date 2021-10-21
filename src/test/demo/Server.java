package test.demo;

import java.net.InetAddress;
import java.net.UnknownHostException;

import kr.ac.uos.ai.arbi.framework.ArbiFrameworkServer;

public class Server {
	public static void main(String[] args) {
		String centerURL = "tcp://" + System.getenv("JMS_CENTER");
		String brokerURL = "tcp://" + System.getenv("JMS_BROKER");
		String brokerName = System.getenv("AGENT");
		
		ArbiFrameworkServer server = new ArbiFrameworkServer(2, brokerName);
		server.start(centerURL, brokerURL);
	}
}
