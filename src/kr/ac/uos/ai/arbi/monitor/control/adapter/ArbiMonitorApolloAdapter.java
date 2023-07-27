package kr.ac.uos.ai.arbi.monitor.control.adapter;

import java.util.Properties;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;

import kr.ac.uos.ai.arbi.monitor.ArbiFrameworkMonitor;

public class ArbiMonitorApolloAdapter implements ArbiMonitorAdapter, MessageListener {

	private Connection jmsConnection;
	private Session jmsSession;
	private MessageProducer jmsProducer;
	private MessageConsumer jmsConsumer;
	private Destination interactionManagerDestination;
	private ArbiFrameworkMonitor monitor;
	
	public ArbiMonitorApolloAdapter(ArbiFrameworkMonitor monitor) {
		this.monitor = monitor;
	}
	
	public boolean connect(String brokerURL, String monitorID, String interactionManagerURL) {
		try{
			ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(brokerURL);
			Properties props = new Properties();
			props.setProperty("brokerURL", brokerURL);
			connectionFactory.setProperties(props);
			this.jmsConnection = connectionFactory.createConnection();
			this.jmsSession = this.jmsConnection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			
			this.interactionManagerDestination = this.jmsSession.createQueue(interactionManagerURL);
			this.jmsProducer = this.jmsSession.createProducer(interactionManagerDestination);
			
			Destination monitorDestination = jmsSession.createQueue(monitorID);
			this.jmsConsumer = this.jmsSession.createConsumer(monitorDestination);
			this.jmsConnection.start();
			
		}catch(JMSException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}


	public void disconnect() {
		try {
			if (this.jmsConsumer != null) {
				this.jmsConsumer.close();
				this.jmsConsumer = null;
			}

			if (this.jmsSession != null) {
				this.jmsSession.close();
				this.jmsSession = null;
			}

			if (this.jmsConnection != null) {
				this.jmsConnection.close();
				this.jmsConnection = null;
			}
		} catch (JMSException localJMSException) {
		}
	}
	
	public void send(String message) {
		TextMessage jmsMessage;
		try {
			jmsMessage = jmsSession.createTextMessage();
			jmsMessage.setText(message);
			
			jmsProducer.send(interactionManagerDestination, jmsMessage);
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return;
	}

	public boolean isValid() {
		if (this.jmsConnection == null)
			return false;
		if (this.jmsSession == null)
			return false;
		return (this.jmsConsumer != null);
	}

	@Override
	public void onMessage(Message m) {
		if(m instanceof TextMessage) {
			TextMessage tMessage = (TextMessage)m;
			try {
				monitor.onLogMessage(tMessage.getText());
			} catch (JMSException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
}
