package test.demoServer;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Map;

import kr.ac.uos.ai.arbi.framework.ArbiFrameworkServer;

public class LocalServer {
	public static void main(String[] args) {
		String centerURL = "tcp://127.0.0.1:61616";
<<<<<<< HEAD
		String brokerURL = "tcp://127.0.0.1:61313";
=======
		String brokerURL = "tcp://127.0.0.1:61316";
>>>>>>> 4280e80611076017f82676b5da487599f5eced18
		String brokerName = "Local";
		
		ArbiFrameworkServer server = new ArbiFrameworkServer(2, brokerName);
		server.start(centerURL, brokerURL);
	}
}
