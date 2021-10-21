package test.demo;

import java.net.InetAddress;
import java.net.UnknownHostException;

import kr.ac.uos.ai.arbi.framework.ArbiFrameworkServer;

public class CenterServer {
	public static void main(String[] args) {
		String centerURL = "tcp://" + System.getenv("JMS_CENTER");
		
		ArbiFrameworkServer server = new ArbiFrameworkServer(4, "Center");
		server.start(centerURL, centerURL);
	}
}