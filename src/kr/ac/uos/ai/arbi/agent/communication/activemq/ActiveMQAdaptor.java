package kr.ac.uos.ai.arbi.agent.communication.activemq;

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

import kr.ac.uos.ai.arbi.agent.ArbiAgentMessage;
import kr.ac.uos.ai.arbi.agent.AgentMessageAction;
import kr.ac.uos.ai.arbi.agent.communication.ArbiMessageAdaptor;
import kr.ac.uos.ai.arbi.agent.communication.ArbiMessageQueue;
import kr.ac.uos.ai.arbi.framework.ArbiFrameworkServer;

import org.apache.activemq.ActiveMQConnectionFactory;



public class ActiveMQAdaptor implements ArbiMessageAdaptor, MessageListener {
	private String					arbiAgentURI;
	
	private Connection 				mqConnection; 
	private Session					mqSession;
	
	private MessageProducer			mqProducer;
	private MessageConsumer			mqConsumer;
	
	private ArbiMessageQueue			queue;

	
	
	public ActiveMQAdaptor(String broker, String myURI, ArbiMessageQueue queue) {
		this.arbiAgentURI = myURI;
		this.queue = queue;
		try {
			
			ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(broker);
			Properties props = new Properties();
			props.setProperty("brokerURL", broker);
			connectionFactory.setProperties(props);
			mqConnection 	= connectionFactory.createConnection();
			mqSession 	= mqConnection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			Destination blackboardServerDestination = mqSession.createQueue(ArbiFrameworkServer.URL);
			mqProducer 	= mqSession.createProducer(blackboardServerDestination);

			Destination srcDestination = mqSession.createQueue(arbiAgentURI + "/message");
			mqConsumer 	= mqSession.createConsumer(srcDestination);
			mqConnection.start();
			while(mqConsumer.receiveNoWait() != null) {
				
			}
			mqConsumer.setMessageListener(this);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	public void close() {
		if (mqProducer != null) {
			try {
				
				mqProducer.close();
			} catch(JMSException e) {
				e.printStackTrace();
			}
			mqProducer = null;
		}
		if (mqConsumer != null) {
			try {
				mqConsumer.close();
			} catch(JMSException e) {
				e.printStackTrace();
			}
			mqConsumer = null;
		}
		if (mqSession != null) {
			try {
				mqSession.close();
			} catch(JMSException e) {
				e.printStackTrace();
			}
			mqSession = null;
		}
		if (mqConnection != null) {
			try {
				mqConnection.close();
			} catch(JMSException e) {
				e.printStackTrace();
			}
			mqConnection = null;
		}
	}

	public void send(ArbiAgentMessage message)  {
		boolean isRequireResponse = false;
		MapMessage mqMessage;
		try {
			mqMessage = mqSession.createMapMessage();
			mqMessage.setString("sender", message.getSender());
			mqMessage.setString("receiver", message.getReceiver());
			mqMessage.setString("command", "Arbi-Agent");
			mqMessage.setString("action", message.getAction().toString());
			mqMessage.setString("content", message.getContent());
			mqMessage.setJMSCorrelationID(message.getConversationID());
			mqProducer.send(mqMessage);
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void onMessage(Message message) {
		if (message instanceof MapMessage) {
			MapMessage mapMessage = (MapMessage)message;
			try {
				String command = mapMessage.getString("command");
				if (command == null) {
					return;
				}
				if(command.startsWith("Arbi-Agent")) {
					String sender = mapMessage.getString("sender");
					String receiver = mapMessage.getString("receiver");
					String action = mapMessage.getString("action");
					String content = mapMessage.getString("content");
					String conversationID = mapMessage.getJMSCorrelationID();
					ArbiAgentMessage agentMessage = new ArbiAgentMessage(sender, receiver, AgentMessageAction.valueOf(action), content, conversationID);
					queue.enqueue(agentMessage);
				}
			} catch(JMSException e) {
				e.printStackTrace();
			}
		}
		
	}

}
