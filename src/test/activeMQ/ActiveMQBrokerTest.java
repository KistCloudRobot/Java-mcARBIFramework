package test.activeMQ;

import org.apache.activemq.transport.stomp.StompConnection;
import org.apache.activemq.transport.stomp.StompFrame;

import kr.ac.uos.ai.arbi.framework.broker.ActiveMQBroker;

public class ActiveMQBrokerTest {
	public static void main(String[] args) {
		ActiveMQBroker broker = new ActiveMQBroker("127.0.0.1", 61616);
		broker.start();
		while(true);
	}
}
