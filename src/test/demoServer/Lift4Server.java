package test.demoServer;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Map;

import kr.ac.uos.ai.arbi.framework.ArbiFrameworkServer;

public class Lift4Server {
	public static void main(String[] args) {
		String brokerAddress;
		String robotID;
		if(args.length == 0) {
			brokerAddress = "tcp://127.0.0.1:61113";
			robotID = "AMR_LIFT4";	
		} else {
			robotID = args[0];
			brokerAddress = args[1];
		}
		
		ArbiFrameworkServer server = new ArbiFrameworkServer(2, robotID);
		server.start(brokerAddress, brokerAddress);
	}
}
