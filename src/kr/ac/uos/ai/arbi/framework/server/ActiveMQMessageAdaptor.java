package kr.ac.uos.ai.arbi.framework.server;

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

import org.apache.activemq.ActiveMQConnectionFactory;

import kr.ac.uos.ai.arbi.agent.AgentMessageAction;
import kr.ac.uos.ai.arbi.agent.ArbiAgentMessage;
import kr.ac.uos.ai.arbi.framework.ArbiFrameworkServer;
import kr.ac.uos.ai.arbi.interaction.InteractionManager;
import kr.ac.uos.ai.arbi.ltm.LTMMessageAction;
import kr.ac.uos.ai.arbi.ltm.communication.LTMMessageFactory;
import kr.ac.uos.ai.arbi.ltm.communication.message.LTMMessage;

public class ActiveMQMessageAdaptor implements MessageListener, MessageDeliverAdaptor, LTMMessageAdaptor{
	private Connection 					mqConnection;
	private Session						mqSession;
	private MessageConsumer				mqConsumer;
	private MessageProducer				mqProducer;
	private MessageService				service;

	public ActiveMQMessageAdaptor(MessageService service) {
		this.service = service;
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
	public void initialize(String serverURL,String brokerURL) {
		try {
			
			ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory();
			Properties props = new Properties();

			connectionFactory.setProperties(props);
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
					//service.messageRecieved(agentMessage);
				} else if(command.startsWith("Long-Term-Memory")) {
					String client = mapMessage.getString("client");
					String action = mapMessage.getString("action");
					String content = mapMessage.getString("content");
					String queryID = mapMessage.getJMSCorrelationID();
					LTMMessageFactory f = LTMMessageFactory.getInstance();
					LTMMessage ltmMessage = f.newMessage(client, LTMMessageAction.valueOf(action), content, queryID);
					//service.messageRecieved(ltmMessage);
				} else if(command.startsWith("InteractionManager-Status")) {
					String status = mapMessage.getString("status");
					service.messageRecieved(status);
				}
			} catch(JMSException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void send(LTMMessage msg) {
		try {
			String receiverURL = msg.getClient();
			String receiverDestination = receiverURL + "/message";
			Destination destination = mqSession.createQueue(receiverDestination);
			
			MapMessage mqMessage = mqSession.createMapMessage();
			mqMessage.setString("client", msg.getClient());
			mqMessage.setString("command", "Long-Term-Memory");
			mqMessage.setString("action", msg.getAction().toString());
			mqMessage.setString("content", msg.getContent());
			mqMessage.setJMSCorrelationID(msg.getConversationID());
			
			mqProducer.send(destination, mqMessage);

		} catch(Exception e) {
			e.printStackTrace();
		}
		
		
	}
	
	public void deliverToMonitor(ArbiAgentMessage message) {
		try {
			
			String receiverDestination = InteractionManager.interactionAgentURI + "/message";
			Destination destination = mqSession.createQueue(receiverDestination);
			
			MapMessage mqMessage = mqSession.createMapMessage();
			mqMessage.setString("sender", "Server");
			mqMessage.setString("receiver", InteractionManager.interactionManagerURI);
			mqMessage.setString("command", "Arbi-Agent");
			mqMessage.setString("action", "System");
			String content ="(MessageLog (Type \"AgentMessage\") (Sender \""+message.getSender()+"\") (Receiver \""+message.getReceiver()+"\")"
					+ " (Action \""+message.getAction().toString()+"\") (Content \""+message.getContent().replace("\"", "\\\"")+"\"))";
			mqMessage.setString("content", content);
			mqMessage.setJMSCorrelationID(message.getConversationID());
			mqProducer.send(destination, mqMessage);

		} catch(Exception e) {
			System.out.println(e.toString());
			e.printStackTrace();
		}
		
	}

	public void deliverToMonitor(LTMMessage msg) {
		try {
			String receiverDestination = InteractionManager.interactionAgentURI +"/message";
			Destination destination = mqSession.createQueue(receiverDestination);
			
			MapMessage mqMessage = mqSession.createMapMessage();
			mqMessage.setString("sender", "Server");
			mqMessage.setString("receiver", InteractionManager.interactionManagerURI);
			mqMessage.setString("command", "Arbi-Agent");
			mqMessage.setString("action", "System");
			String content = "(MessageLog (Type\"LTMMessage\") (Client \"" +msg.getClient()+"\") (Action \""+msg.getAction().toString()+"\")"
					+ " (Content \""+msg.getContent().replace("\"", "\\\"")+"\"))";
			mqMessage.setString("content", content);
			mqMessage.setJMSCorrelationID(msg.getConversationID());
			
			mqProducer.send(destination, mqMessage);

		} catch(Exception e) {
			e.printStackTrace();
		}
		
	}


	@Override
	public void notify(LTMMessage msg) {
		// TODO Auto-generated method stub
		
	}
	

}
