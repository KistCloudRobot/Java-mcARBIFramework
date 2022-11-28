package test.activeMQ;

import java.io.IOException;
import java.net.UnknownHostException;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.transport.stomp.StompConnection;
import org.apache.activemq.transport.stomp.StompFrame;

import kr.ac.uos.ai.arbi.framework.ArbiFrameworkServer;
import kr.ac.uos.ai.arbi.framework.broker.ActiveMQBroker;

public class ActiveMQClientTest {
	public static void main(String[] args) {
		try {
			StompConnection conn = new StompConnection();
			conn.open("127.0.0.1", 61616);
			conn.connect(null);
			conn.subscribe("java");
			conn.send(ArbiFrameworkServer.URL, "hello server");
			StompFrame msg = conn.receive();
			System.out.println(msg.getBody());
			conn.send("queue", "hello python");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
