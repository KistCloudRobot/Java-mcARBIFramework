package kr.ac.uos.ai.arbi.ltm.communication.adaptor;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import kr.ac.uos.ai.arbi.framework.ArbiFrameworkServer;
import kr.ac.uos.ai.arbi.ltm.LTMMessageAction;
import kr.ac.uos.ai.arbi.ltm.communication.LTMMessageFactory;
import kr.ac.uos.ai.arbi.ltm.communication.LTMMessageQueue;
import kr.ac.uos.ai.arbi.ltm.communication.message.LTMMessage;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

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
		try {
			JSONObject messageObject = new JSONObject();
			messageObject.put("client", message.getClient());
			messageObject.put("command", "Long-Term-Memory");
			messageObject.put("action", message.getAction().toString());
			messageObject.put("content", message.getContent());
			messageObject.put("conversationID", message.getConversationID());
			
			TextMessage textMessage = mqSession.createTextMessage(messageObject.toJSONString());
			mqProducer.send(textMessage);
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void onMessage(Message message) {
		if (message instanceof TextMessage) {
			try {
	            TextMessage textMessage = (TextMessage) message;
				String text = textMessage.getText();			
				JSONParser jsonParser = new JSONParser();
				JSONObject messageObject = (JSONObject) jsonParser.parse(text);
				
				String command = messageObject.get("command").toString();
				if (command.startsWith("Long-Term-Memory")) {
					String client = messageObject.get("client").toString();
					String action = messageObject.get("action").toString();
					String content = messageObject.get("content").toString();
					String conversationID = messageObject.get("conversationID").toString();
					LTMMessageFactory f = LTMMessageFactory.getInstance();
					LTMMessage ltmMessage = f.newMessage(client, LTMMessageAction.valueOf(action), content,
							conversationID);
					queue.enqueue(ltmMessage);
				}
			} catch (JMSException | ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else {
			System.out.println(message.toString());
		}
	}
	@Override
	public void notify(LTMMessage msg) {
		// TODO Auto-generated method stub
		
	}

}
