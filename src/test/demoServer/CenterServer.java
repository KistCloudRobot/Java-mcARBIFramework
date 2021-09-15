package test.demoServer;

import kr.ac.uos.ai.arbi.framework.ArbiFrameworkServer;

public class CenterServer {
	public static void main(String[] args) {
		String centerURL = System.getenv("JMS_CENTER");
		String serverName = System.getenv("SERVER");
		
		System.out.println("center server");
		System.out.println("url : " + centerURL);
		
		ArbiFrameworkServer server = new ArbiFrameworkServer(4, serverName);
		server.start(centerURL, centerURL);
	}
}