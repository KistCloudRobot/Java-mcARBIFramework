package test.demoServer;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Map;

import kr.ac.uos.ai.arbi.framework.ArbiFrameworkServer;

public class Tow1Server {
	public static void main(String[] args) {
		String centerURL = "tcp://172.16.165.204:61616";
		String brokerURL = "tcp://172.16.165.204:61114";
		String brokerName = "Tow1";
		
		ArbiFrameworkServer server = new ArbiFrameworkServer(2, brokerName);
		server.start(centerURL, brokerURL);
	}
}
