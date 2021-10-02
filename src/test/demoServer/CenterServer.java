package test.demoServer;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Map;

import kr.ac.uos.ai.arbi.framework.ArbiFrameworkServer;

public class CenterServer {
	public static void main(String[] args) {
		try {
			String ip = InetAddress.getLocalHost().getHostAddress();
			String centerURL = "tcp://" + System.getenv("JMS_CENTER");
			
			ArbiFrameworkServer server = new ArbiFrameworkServer(4, "Center");
			server.start(centerURL, centerURL);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}