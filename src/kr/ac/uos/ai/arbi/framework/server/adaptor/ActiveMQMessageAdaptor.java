package kr.ac.uos.ai.arbi.framework.server.adaptor;

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
import org.json.simple.JSONObject;

import kr.ac.uos.ai.arbi.agent.ArbiAgentMessage;
import kr.ac.uos.ai.arbi.agent.communication.ArbiMessageQueue;
import kr.ac.uos.ai.arbi.framework.ArbiFrameworkServer;
import kr.ac.uos.ai.arbi.framework.server.MessageService;
import kr.ac.uos.ai.arbi.ltm.communication.LTMMessageQueue;
import kr.ac.uos.ai.arbi.ltm.communication.message.LTMMessage;

public class ActiveMQMessageAdaptor extends MessageAdaptor implements MessageListener {
	private Connection 					mqConnection;
	private Session						mqSession;
	private MessageConsumer				mqConsumer;
	private MessageProducer				mqProducer;
	private MessageService				service;

	public ActiveMQMessageAdaptor(MessageService service,ArbiMessageQueue arbiQueue,LTMMessageQueue ltmQueue) {
		super(service, arbiQueue, ltmQueue);
	}
	
	
	@Override
	public void deliver(ArbiAgentMessage message) {
		try {
			String receiverURL = message.getReceiver();
			String receiverDestination = receiverURL + "/message";
			Destination destination = mqSession.createQueue(receiverDestination);
			
			MapMessage mqMessage = mqSession.createMapMessage();
			mqMessage.setString("sender", message.getSender());
			mqMessage.setString("receiver", message.getReceiver());
			mqMessage.setString("command", "Arbi-Agent");
			mqMessage.setString("action", message.getAction().toString());
			mqMessage.setString("content", message.getContent());
			mqMessage.setJMSCorrelationID(message.getConversationID());
			mqProducer.send(destination, mqMessage);
	
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	
	@Override
	public void initialize(String brokerURL) {
		try {
			ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory();
			connectionFactory.setBrokerURL(brokerURL);
			mqConnection 	= connectionFactory.createConnection();
			mqSession 	= mqConnection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			Destination serverDestination = mqSession.createQueue(ArbiFrameworkServer.URL);
			mqConsumer 	= mqSession.createConsumer(serverDestination);
			mqProducer 	= mqSession.createProducer(null);

			mqConnection.start();
			mqConsumer.setMessageListener(this);
		} catch(Exception e) {
			e.printStackTrace();
		}
		
	}

	@Override
	public void onMessage(Message message) {
		try {
			if (message instanceof TextMessage) {
	            TextMessage textMessage = (TextMessage) message;
				String text = textMessage.getText();
	            this.handleMessage(text);
			}
		} catch (JMSException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	@Override
	protected void send(String receiver, JSONObject msg) {
		try {
			Destination destination = mqSession.createQueue(receiver + "/message");
	
	        TextMessage message = mqSession.createTextMessage(msg.toJSONString());
			mqProducer.send(destination, message);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}
