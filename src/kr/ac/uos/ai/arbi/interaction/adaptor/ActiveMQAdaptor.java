package kr.ac.uos.ai.arbi.interaction.adaptor;

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

import kr.ac.uos.ai.arbi.framework.ArbiFrameworkServer;
import kr.ac.uos.ai.arbi.interaction.InteractionManager;
import kr.ac.uos.ai.arbi.interaction.InteractionManagerBrokerConfiguration;
import kr.ac.uos.ai.arbi.interaction.MonitorMessageQueue;

public class ActiveMQAdaptor implements MessageListener, InteractionMessageAdaptor{

	private Connection mqConnection;
	private Session mqSession;
	private MessageConsumer mqConsumer;
	
	private MonitorMessageQueue queue;
	
	public ActiveMQAdaptor(MonitorMessageQueue queue) {
		this.queue = queue;
		try {
			ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory();
			Properties props = new Properties();
			
			props.setProperty("brokerURL", InteractionManagerBrokerConfiguration.getApolloBroker());
			connectionFactory.setProperties(props);
			mqConnection = connectionFactory.createConnection();
			mqSession = mqConnection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			Destination interactionManagerrDestination = mqSession.createQueue(InteractionManager.interactionManagerURI);
			mqConsumer = mqSession.createConsumer(interactionManagerrDestination);
			mqConnection.start();
			mqConsumer.setMessageListener(this);
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void close() {
		
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

	@Override
	public void onMessage(Message message) {
		
		
		
		if(message instanceof TextMessage) {
			TextMessage textMessage = (TextMessage)message;
			System.out.println(textMessage.toString());
			try {
				String monitorAction = textMessage.getText().toString();
				queue.enqueue(monitorAction);
			} catch (JMSException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	@Override
	public void send(String monitorID, String message) {
		
		TextMessage textMessage;
		try {
			textMessage = mqSession.createTextMessage();
			Destination monitorDestination = mqSession.createQueue(monitorID);
			MessageProducer mqProducer = mqSession.createProducer(monitorDestination);
			textMessage.setText(message);
			mqProducer.send(textMessage);
			
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void sendStatus(String status) {
		MapMessage mqMessage;
		try {
			mqMessage = mqSession.createMapMessage();
			Destination monitorDestination = mqSession.createQueue(ArbiFrameworkServer.URL);
			MessageProducer mqProducer = mqSession.createProducer(monitorDestination);
			mqMessage.setString("command", "InteractionManager-Status");
			mqMessage.setString("status", status);
			mqProducer.send(mqMessage);
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
