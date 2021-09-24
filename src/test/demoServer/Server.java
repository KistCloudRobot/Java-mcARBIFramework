package test.demoServer;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Map;

import kr.ac.uos.ai.arbi.framework.ArbiFrameworkServer;

public class Server {
	public static void main(String[] args) {
		try {
			String ip = InetAddress.getLocalHost().getHostAddress();
			String centerURL = "tcp://192.168.0.2:61616";
			String brokerURL = "tcp://" + ip + ":61316";
			String brokerName = System.getenv("AGENT");
			
			ArbiFrameworkServer server = new ArbiFrameworkServer(2, brokerName);
			server.start(centerURL, brokerURL);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
