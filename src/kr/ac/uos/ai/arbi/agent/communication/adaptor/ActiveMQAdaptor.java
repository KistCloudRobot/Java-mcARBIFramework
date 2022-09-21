package kr.ac.uos.ai.arbi.agent.communication.adaptor;

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

import kr.ac.uos.ai.arbi.agent.ArbiAgentMessage;
import kr.ac.uos.ai.arbi.agent.AgentMessageAction;
import kr.ac.uos.ai.arbi.agent.communication.ArbiMessageQueue;
import kr.ac.uos.ai.arbi.framework.ArbiFrameworkServer;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;



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
			System.out.println("my dest : " + arbiAgentURI + "/message");
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
		try {
			JSONObject messageObject = new JSONObject();
			messageObject.put("sender", message.getSender());
			messageObject.put("receiver", message.getReceiver());
			messageObject.put("command", "Arbi-Agent");
			messageObject.put("action", message.getAction().toString());
			messageObject.put("content", message.getContent());
			messageObject.put("conversationID", message.getConversationID());
			messageObject.put("timestamp", message.getTimestamp());
			
			TextMessage textMessage = mqSession.createTextMessage(messageObject.toJSONString());
			mqProducer.send(textMessage);
			
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void onMessage(Message message) {
		try {
			if (message instanceof TextMessage) {
	            TextMessage textMessage = (TextMessage) message;
				String text = textMessage.getText();
				JSONParser jsonParser = new JSONParser();
				JSONObject messageObject = (JSONObject) jsonParser.parse(text);
				
				String command = messageObject.get("command").toString();
				if (command.startsWith("Arbi-Agent")) {
					String sender = messageObject.get("sender").toString();
					String receiver = messageObject.get("receiver").toString();
					String action = messageObject.get("action").toString();
					String content = messageObject.get("content").toString();
					String conversationID = messageObject.get("conversationID").toString();
					long timestamp = Long.parseLong(messageObject.get("timestamp").toString());
					ArbiAgentMessage agentMessage = new ArbiAgentMessage(sender, receiver,
							AgentMessageAction.valueOf(action), content, conversationID, timestamp);
					queue.enqueue(agentMessage);
				}
			}
		} catch (JMSException | ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
}
