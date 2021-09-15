package test.demoServer;

import kr.ac.uos.ai.arbi.framework.ArbiFrameworkServer;

public class Server {
	public static void main(String[] args) {
		String centerURL = System.getenv("JMS_CENTER");
		String brokerURL = System.getenv("JMS_BROKER");
		String serverName = System.getenv("SERVER");
		
		System.out.println("server");
		System.out.println("url : " + brokerURL);
		System.out.println("center : " + centerURL);
		
		ArbiFrameworkServer server = new ArbiFrameworkServer(2, serverName);
		server.start(centerURL, brokerURL);
	}
}
