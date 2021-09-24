package test.demoServer;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Map;

import kr.ac.uos.ai.arbi.framework.ArbiFrameworkServer;

public class TestServer {
	public static void main(String[] args) {
		try {
			String ip = InetAddress.getLocalHost().getHostAddress();
			String centerURL = "tcp://" + ip + ":61616";
			String brokerURL = "tcp://172.16.165.204:61316";
			String brokerName = "agent://www.arbi.com/Test/TestAgent";
			
			ArbiFrameworkServer server = new ArbiFrameworkServer(2, brokerName);
			server.start(centerURL, brokerURL);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
