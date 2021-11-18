package test.demoServer;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Map;

import kr.ac.uos.ai.arbi.framework.ArbiFrameworkServer;

public class CenterServer {
	public static void main(String[] args) {
		String centerURL = "tcp://192.168.0.14:61616";
		
		ArbiFrameworkServer server = new ArbiFrameworkServer(4, "Center");
		server.start(centerURL, centerURL);
	}
}