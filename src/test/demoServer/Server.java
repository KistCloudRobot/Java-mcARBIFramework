package test.demoServer;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Map;

import kr.ac.uos.ai.arbi.framework.ArbiFrameworkServer;

public class Server {
	public static void main(String[] args) {
		try {
			String ip = InetAddress.getLocalHost().getHostAddress();
			String centerURL = "tcp://" + System.getenv("JMS_CENTER");
			String brokerURL = "tcp://" + System.getenv("JMS_BROKER");
			String brokerName = System.getenv("AGENT");
			
			ArbiFrameworkServer server = new ArbiFrameworkServer(2, brokerName);
			server.start(centerURL, brokerURL);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
