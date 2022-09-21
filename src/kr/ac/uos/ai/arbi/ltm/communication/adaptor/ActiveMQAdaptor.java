package kr.ac.uos.ai.arbi.ltm.communication.adaptor;

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

import kr.ac.uos.ai.arbi.framework.ArbiFrameworkServer;
import kr.ac.uos.ai.arbi.ltm.LTMMessageAction;
import kr.ac.uos.ai.arbi.ltm.communication.LTMMessageFactory;
import kr.ac.uos.ai.arbi.ltm.communication.LTMMessageQueue;
import kr.ac.uos.ai.arbi.ltm.communication.message.LTMMessage;

import org.apache.activemq.ActiveMQConnectionFactory;



public class ActiveMQAdaptor implements LTMMessageAdaptor, MessageListener {
	private String					clientURI;
	
	private Connection 				mqConnection; 
	private Session					mqSession;
	
	private MessageProducer			mqProducer;
	private MessageConsumer			mqConsumer;
	
	private LTMMessageQueue			queue;
	
	
	public ActiveMQAdaptor(String brokerURL, String myURI, LTMMessageQueue queue) {
		this.clientURI = myURI;
		this.queue = queue;
		try {
			ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory();
			connectionFactory.setBrokerURL(brokerURL);
			mqConnection 	= connectionFactory.createConnection();
			mqSession 	= mqConnection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			Destination ltmDestination = mqSession.createQueue(ArbiFrameworkServer.URL);
			mqProducer 	= mqSession.createProducer(ltmDestination);

			Destination srcDestination = mqSession.createQueue(clientURI + "/message");
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

	public void send(LTMMessage message)  {
		MapMessage mqMessage;
		try {
			mqMessage = mqSession.createMapMessage();
			mqMessage.setString("client", message.getClient());
			mqMessage.setString("command", "Long-Term-Memory");
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
				if(command.startsWith("Long-Term-Memory")) {
					String client = mapMessage.getString("client");
					String action = mapMessage.getString("action");
					String content = mapMessage.getString("content");
					String conversationID = mapMessage.getJMSCorrelationID();
					LTMMessageFactory f = LTMMessageFactory.getInstance();
					LTMMessage ltmMessage = f.newMessage(client, LTMMessageAction.valueOf(action), content, conversationID);
					queue.enqueue(ltmMessage);
				}
			} catch(JMSException e) {
				e.printStackTrace();
			}
		}
		
	}
	@Override
	public void notify(LTMMessage msg) {
		// TODO Auto-generated method stub
		
	}

}
