package kr.ac.uos.ai.arbi.agent;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class ArbiAgentExecutor {

	public static void execute(String agentXML, ArbiAgent agent) {

		try {
			File inputFile = new File(agentXML);
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(inputFile);
			doc.getDocumentElement().normalize();

			Element agentElement = (Element) doc.getElementsByTagName("Agent-Property").item(0);
			if (agentElement != null) {
				Element serverURLElement = (Element) agentElement.getElementsByTagName("ServerURL").item(0);
				Element agentNameElement = (Element) agentElement.getElementsByTagName("AgentName").item(0);
				String brokerTypeElement = agentElement.getElementsByTagName("BrokerType").item(0).getTextContent();
				
				System.out.println("agentName : " +agentNameElement.getTextContent());
				
				int brokerType = 2;
				if (brokerTypeElement.toLowerCase().equals("activemq")) {
					System.out.println("broker type : activamq");
					brokerType = 0;
				} else if (brokerTypeElement.toLowerCase().equals("apollo")) {
					System.out.println("broker type : apollo");
					brokerType = 1;
				} else if(brokerTypeElement.toLowerCase().equals("zeromq")) {
					brokerType = 2;
					System.out.println("broker type : zeromq");
				}
				

				if (serverURLElement != null && agentNameElement != null) {
					agent.initialize(serverURLElement.getTextContent(), agentNameElement.getTextContent(), brokerType);
				} else {
					System.out.println("Execute Error : AgentXML file was lacked");
				}
			} else {
				System.out.println("Execute Error : AgentXML file was wrong");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void execute(String agentName, ArbiAgent agent, int brokerType) {
		agent.initialize(agentName, brokerType);
	}
	public static void execute(String brokerAddress, String agentName, ArbiAgent agent, int brokerType) {
		agent.initialize(brokerAddress,agentName, brokerType);
	}

}
