package test;

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

import kr.ac.uos.ai.arbi.agent.ArbiAgent;
import kr.ac.uos.ai.arbi.agent.ArbiAgentExecutor;
import kr.ac.uos.ai.arbi.interaction.InteractionManager;

public class MonitorTest implements MessageListener {

	private Connection mqConnection;
	private Session mqSession;
	private MessageProducer mqProducer;
	private MessageConsumer mqConsumer;
	private Destination serverDestination;

	public MonitorTest() {

	}

	public void init(String broker, String monitorURI) {
		try {

			ActiveMQConnectionFactory connectionFActory = new ActiveMQConnectionFactory(broker);
			mqConnection = connectionFActory.createConnection();
			mqSession = mqConnection.createSession(false, Session.AUTO_ACKNOWLEDGE);

			serverDestination = mqSession.createQueue(InteractionManager.interactionManagerURI);
			mqProducer = mqSession.createProducer(serverDestination);
			
			Destination monitorDestination = mqSession.createQueue(monitorURI);
			mqConsumer = mqSession.createConsumer(monitorDestination);
			mqConnection.start();
			mqConsumer.setMessageListener(this);

		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void send(String message) {
		TextMessage textMessage;
		try {
			textMessage = mqSession.createTextMessage();
			textMessage.setText(message);
			mqProducer.send(serverDestination, textMessage);
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return;
	}

	@Override
	public void onMessage(Message message) {
		if (message instanceof TextMessage) {
			TextMessage mapMessage = (TextMessage)message;
			try {
					String log = mapMessage.getText();
					System.out.println("Enter on message = " +log);
			} catch(JMSException e) {
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) {
		
	
		MonitorTest test = new MonitorTest();
		test.init("tcp://192.168.0.2:61616", "monitor1");
		test.send("{\"ID\":\"monitor1\",\"Action\":\"Create Monitor\",\"Protocol\":\"ActiveMQ\","
				+ "\"Filter\" : [{\"LogType\":\"SystemLog\", \"Actor\":\"taskManager\", \"Type\":\"WorldModel\", \"Action\":\"Assert\", \"Flag\":\"true\"},"
				+ "{\"LogType\":\"SystemLog\", \"Actor\":\"taskManager\", \"Type\":\"WorldModel\", \"Action\":\"Retract\", \"Flag\":\"true\"},"
				+ "{\"LogType\":\"SystemLog\", \"Actor\":\"taskManager\", \"Type\":\"WorldModel\", \"Action\":\"Retrieve\", \"Flag\":\"true\"},"
				+ "{\"LogType\":\"SystemLog\", \"Actor\":\"taskManager\", \"Type\":\"Goal\", \"Action\":\"New\", \"Flag\":\"true\"},"
				+ "{\"LogType\":\"SystemLog\", \"Actor\":\"taskManager\", \"Type\":\"Goal\", \"Action\":\"Remove\", \"Flag\":\"true\"},"
				+ "{\"LogType\":\"SystemLog\", \"Actor\":\"taskManager\", \"Type\":\"Intention\", \"Action\":\"Intend\", \"Flag\":\"true\"},"
				+ "{\"LogType\":\"SystemLog\",\"Actor\":\"contextManager\",\"Type\":\"sendLogQuery\",\"Action\":\"Query\",\"Flag\":\"true\"},"
				+ "{\"LogType\":\"SystemLog\",\"Actor\":\"knowledgeManager\",\"Type\":\"TestType\",\"Action\":\"Test\",\"Flag\":\"true\"},"
				+ "{\"LogType\":\"SystemLog\",\"Actor\":\"initiator\",\"Action\":\"initiate\",\"Type\":\"Rule\",\"Flag\":\"true\"},"
				+ "{\"LogType\":\"SystemLog\",\"Actor\":\"initiator\",\"Action\":\"initiate\",\"Type\":\"OWL\",\"Flag\":\"true\"},"
				+ "{\"LogType\":\"SystemLog\",\"Actor\":\"initiator\",\"Action\":\"initiate\",\"Type\":\"Plan\",\"Flag\":\"true\"},"
				+ "{\"LogType\":\"SystemLog\",\"Actor\":\"initiator\",\"Action\":\"initiate\",\"Type\":\"Robot\",\"Flag\":\"true\"},"
				+ "{\"LogType\":\"MessageLog\", \"Type\":\"AgentMessage\",\"Action\":\"Request\", \"Flag\":\"true\"},"
				+ "{\"LogType\":\"MessageLog\", \"Type\":\"AgentMessage\",\"Action\":\"Response\", \"Flag\":\"true\"},"
				+ "{\"LogType\":\"MessageLog\", \"Type\":\"AgentMessage\",\"Action\":\"Query\", \"Flag\":\"true\"},"
				+ "{\"LogType\":\"MessageLog\", \"Type\":\"LTMMessage\",\"Action\" :\"AssertFact\",\"Flag\":\"true\"},"
				+ "{\"LogType\":\"MessageLog\", \"Type\":\"LTMMessage\",\"Action\" :\"Result\",\"Flag\":\"true\"}]}");
	
	}

}
