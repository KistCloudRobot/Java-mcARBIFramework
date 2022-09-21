package kr.ac.uos.ai.arbi;

import java.io.File;
import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import kr.ac.uos.ai.arbi.agent.ArbiAgentExecutor;
import kr.ac.uos.ai.arbi.framework.ArbiFrameworkServer;
import kr.ac.uos.ai.arbi.framework.broker.ActiveMQBroker;
import kr.ac.uos.ai.arbi.framework.broker.ApolloBroker;
import kr.ac.uos.ai.arbi.framework.center.RedisLTMService;
import kr.ac.uos.ai.arbi.framework.center.RedisUtil;
import kr.ac.uos.ai.arbi.interaction.InteractionManager;
import kr.ac.uos.ai.arbi.interaction.InteractionManagerBrokerConfiguration;
import kr.ac.uos.ai.arbi.monitor.ArbiFrameworkMonitor;

public class ServerLauncher {

	public static final int DefaultBrokerPort = 61616;
	public static final String DefualtBrokerHost = "localhost";

	public static BrokerType brokerType = BrokerType.ZEROMQ;

	private InteractionManager interactionManager;

	public ServerLauncher(String[] args) {

		int brokerPort = DefaultBrokerPort;
		String brokerHost = DefualtBrokerHost;
		
		boolean interactionManagerStart = false;
		String interactionManagerURI = null;
		boolean arbiFrameworkMonitorStart = false;
		boolean isCenter = true;
		String centerURL = "";
		String configurationPath = "configuration/ServerConfiguration.xml";
		
		if(args.length > 0) {
			configurationPath = args[0];
		}
			
		try {
			
			File inputFile = new File(configurationPath);
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(inputFile);
			doc.getDocumentElement().normalize();
			Element configurationElement = (Element) doc.getElementsByTagName("ARBIFrameworkServerConfiguration")
					.item(0);
			if (configurationElement != null) {

				Element brokerPropertyElement = (Element) configurationElement
						.getElementsByTagName("MessageBrokerProperty").item(0);
				Element interactionManagerStartElement = (Element) configurationElement
						.getElementsByTagName("InteractionManagerStart").item(0);
				Element arbiAgentMonitorStartElement = (Element) configurationElement
						.getElementsByTagName("ARBIFrameworkMonitorStart").item(0);
				Element interactionManagerBrokerElement = (Element) configurationElement
						.getElementsByTagName("InteractionManagerBrokerProperty").item(0);
				
				
				Element centerRouterElement = (Element) configurationElement
						.getElementsByTagName("RouterProperty").item(0);
				
				
				Element interactionManagerStompBrokerElement = (Element) interactionManagerBrokerElement
						.getElementsByTagName("StompBroker").item(0);

				if (interactionManagerStompBrokerElement.getAttribute("Available").equals("true")) {
					Element portElement = (Element) interactionManagerStompBrokerElement.getElementsByTagName("Port")
							.item(0);
					Element hostElement = (Element) interactionManagerStompBrokerElement.getElementsByTagName("Host")
							.item(0);
					InteractionManagerBrokerConfiguration.setStompBroker(
							"stomp://" + hostElement.getTextContent() + ":" + portElement.getTextContent());
					System.out.println("InteractionManager stomp addr : "
							+ InteractionManagerBrokerConfiguration.getStompBroker());
				}
				Element interactionManagerApolloBrokerElement = (Element) interactionManagerBrokerElement
						.getElementsByTagName("ApolloBroker").item(0);
				
				if (interactionManagerApolloBrokerElement.getAttribute("Available").equals("true")) {
					Element portElement = (Element) interactionManagerApolloBrokerElement.getElementsByTagName("Port")
							.item(0);
					Element hostElement = (Element) interactionManagerApolloBrokerElement.getElementsByTagName("Host")
							.item(0);
					InteractionManagerBrokerConfiguration.setApolloBroker(
							"tcp://" + hostElement.getTextContent() + ":" + portElement.getTextContent());
					System.out.println("InteractionManager apollo addr : "
							+ InteractionManagerBrokerConfiguration.getApolloBroker());
				}
				Element interactionManagerZeroMQBrokerElement = (Element) interactionManagerBrokerElement
						.getElementsByTagName("ZeroMQBroker").item(0);
				
				if (interactionManagerZeroMQBrokerElement.getAttribute("Available").equals("true")) {
					Element portElement = (Element) interactionManagerZeroMQBrokerElement.getElementsByTagName("Port")
							.item(0);
					Element hostElement = (Element) interactionManagerZeroMQBrokerElement.getElementsByTagName("Host")
							.item(0);
					InteractionManagerBrokerConfiguration.setZeroMQBroker(
							"tcp://" + hostElement.getTextContent() + ":" + portElement.getTextContent());
					System.out.println("InteractionManager zeroMQ addr : "
							+ InteractionManagerBrokerConfiguration.getZeroMQBroker());
				}
				Element interactionManagerSocketBrokerElement = (Element) interactionManagerBrokerElement
						.getElementsByTagName("SocketBroker").item(0);
				if (interactionManagerSocketBrokerElement.getAttribute("Available").equals("true")) {
					Element portElement = (Element) interactionManagerSocketBrokerElement.getElementsByTagName("Port")
							.item(0);
					Element hostElement = (Element) interactionManagerSocketBrokerElement.getElementsByTagName("Host")
							.item(0);
					InteractionManagerBrokerConfiguration.setSocketBroker(
							"tcp://" + hostElement.getTextContent() + ":" + portElement.getTextContent());
					System.out.println("InteractionManager socket addr : "
							+ InteractionManagerBrokerConfiguration.getSocketBroker());
				}
				
				
				if(centerRouterElement != null) {
					Element portElement = (Element) centerRouterElement.getElementsByTagName("Port").item(0);
					Element hostElement = (Element) centerRouterElement.getElementsByTagName("Host").item(0);
					centerURL = "tcp://" + hostElement.getTextContent() + ":" + portElement.getTextContent();
				}
				
				if (brokerPropertyElement != null) {
					Element portElement = (Element) brokerPropertyElement.getElementsByTagName("Port").item(0);
					Element hostElement = (Element) brokerPropertyElement.getElementsByTagName("Host").item(0);
					Element nameElement = (Element) brokerPropertyElement.getElementsByTagName("Name").item(0);
					boolean isTrue = brokerPropertyElement.getAttribute("Center").equals("true");
					System.out.println("wat? " + isTrue);
					
					if (portElement != null) {
						brokerPort = Integer.parseInt(portElement.getTextContent());
					}
					if (hostElement != null) {
						brokerHost = hostElement.getTextContent();
					}
				}

				if (interactionManagerStartElement != null
						&& interactionManagerStartElement.getTextContent().toLowerCase().equals("true"))
					interactionManagerStart = true;

				if (arbiAgentMonitorStartElement != null
						&& arbiAgentMonitorStartElement.getTextContent().toLowerCase().equals("true"))
					arbiFrameworkMonitorStart = true;

			}


			if (InteractionManagerBrokerConfiguration.getApolloBroker() != null) {
				ApolloBroker broker = new ApolloBroker();
				broker.setURL(InteractionManagerBrokerConfiguration.getApolloBroker());
				broker.start();
			}
			if (InteractionManagerBrokerConfiguration.getStompBroker() != null) {
				ActiveMQBroker stompBroker = new ActiveMQBroker();
				stompBroker.setURL(InteractionManagerBrokerConfiguration.getStompBroker());
				stompBroker.start();
			}
			
			
			String brokerURL = "tcp://" + brokerHost + ":" + brokerPort;
			
			ArbiFrameworkServer server = new ArbiFrameworkServer(brokerType);
			server.start(brokerURL);

			if (interactionManagerStart) {

				interactionManager = new InteractionManager();

				interactionManager.start(brokerURL, brokerType);

				ArbiAgentExecutor.execute(brokerURL, InteractionManager.interactionAgentURI, interactionManager,
						brokerType);

			}

			if (arbiFrameworkMonitorStart) {
				ArbiFrameworkMonitor m = new ArbiFrameworkMonitor();
				m.start();
			}


		} catch (Exception e) {
			e.printStackTrace();
		}

		if (args.length > 0) {
			args[0].equals("-RunDispatcher");

		}

		System.out.println("Arbi server started!");
	}

	public void stop() {
		interactionManager.stop();
	}

	public static void main(String[] args) {
		ServerLauncher launcher = new ServerLauncher(args);

		Runtime.getRuntime().addShutdownHook(new Thread() {
			public void run() {
				launcher.stop();
			}
		});
	}

}
