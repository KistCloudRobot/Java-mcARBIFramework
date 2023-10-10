package kr.ac.uos.ai.arbi.interaction.adaptor;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.Properties;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.transport.stomp.StompConnection;
import org.apache.activemq.transport.stomp.StompFrame;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import kr.ac.uos.ai.arbi.agent.AgentMessageAction;
import kr.ac.uos.ai.arbi.agent.ArbiAgentMessage;
import kr.ac.uos.ai.arbi.framework.ArbiFrameworkServer;
import kr.ac.uos.ai.arbi.framework.broker.ActiveMQBroker;
import kr.ac.uos.ai.arbi.interaction.InteractionManager;
import kr.ac.uos.ai.arbi.interaction.InteractionManagerBrokerConfiguration;
import kr.ac.uos.ai.arbi.interaction.MonitorMessageQueue;

public class ActiveMQAdaptor implements InteractionMessageAdaptor {
	private MonitorMessageQueue queue;
	
	private StompConnection	connection;
	private MessageRecvTask messageRecvTask;
	
	public ActiveMQAdaptor(MonitorMessageQueue queue) {
		try {
			connection = new StompConnection();
			connection.open(InteractionManagerBrokerConfiguration.getActiveMQBrokerHost(), InteractionManagerBrokerConfiguration.getActiveMQBrokerPort());
			connection.connect("system", "manager");
			connection.subscribe(InteractionManager.interactionManagerURI + "/message");
			
			this.queue = queue;
			messageRecvTask = new MessageRecvTask();
			messageRecvTask.start();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	private class MessageRecvTask extends Thread {
		@Override
		public void run() {
			while (true) {
				String message = "";
				try {
					StompFrame messageFrame;
					messageFrame = connection.receive();
					if(messageFrame != null) message = messageFrame.getBody();
					else continue;
				}
				catch(SocketTimeoutException e) {
					continue;
				}
				catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					continue;
				}
				if(message.isEmpty()) continue;
				onMessage(message);
			} 
		}
	}
	
	public void close() {
		try {
			connection.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void onMessage(String message) {
//		System.out.println(message);
		queue.enqueue(message);
	}

	@Override
	public void send(String monitorID, String message) {
		try {
			System.out.println("before send : " + monitorID);
			System.out.println("before send : " + message);
			connection.send(monitorID, message);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void sendStatus(String status) {
		try {
			JSONObject messageObject = new JSONObject();
			messageObject.put("command", "InteractionManager-Status");
			messageObject.put("status", status);
			connection.send(ArbiFrameworkServer.URL, messageObject.toJSONString());
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
}
