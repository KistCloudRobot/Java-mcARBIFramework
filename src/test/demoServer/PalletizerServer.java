package test.demoServer;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Map;

import kr.ac.uos.ai.arbi.BrokerType;
import kr.ac.uos.ai.arbi.framework.ArbiFrameworkServer;

public class PalletizerServer {
	public static void main(String[] args) {
//		String brokerURL = "tcp://127.0.0.1:62112";
		String brokerURL = "tcp://192.168.100.10:62112";
		//String brokerURL = "tcp://172.16.165.141:61112";
		
		ArbiFrameworkServer server = new ArbiFrameworkServer(BrokerType.ZEROMQ);
		server.start(brokerURL);
	}
}
