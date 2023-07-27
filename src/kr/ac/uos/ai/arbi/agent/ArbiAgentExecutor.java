package kr.ac.uos.ai.arbi.agent;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import kr.ac.uos.ai.arbi.BrokerType;

public class ArbiAgentExecutor {
//	public static void execute(String agentXML, ArbiAgent agent) {
//
//		try {
//			File inputFile = new File(agentXML);
//			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
//			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
//			Document doc = dBuilder.parse(inputFile);
//			doc.getDocumentElement().normalize();
//
//			Element agentElement = (Element) doc.getElementsByTagName("Agent-Property").item(0);
//			if (agentElement != null) {
//				Element serverURLElement = (Element) agentElement.getElementsByTagName("ServerURL").item(0);
//				Element agentNameElement = (Element) agentElement.getElementsByTagName("AgentName").item(0);
//				String brokerTypeElement = agentElement.getElementsByTagName("BrokerType").item(0).getTextContent();
//				
//				BrokerType brokerType = BrokerType.ZEROMQ;
//				if (brokerTypeElement.toLowerCase().equals("activemq")) {
//					brokerType = BrokerType.ACTIVEMQ;
//				} else if (brokerTypeElement.toLowerCase().equals("apollo")) {
//					brokerType = BrokerType.APOLLO;
//				} else if(brokerTypeElement.toLowerCase().equals("zeromq")) {
//					brokerType = BrokerType.ZEROMQ;
//				}
//				
//
//				if (serverURLElement != null && agentNameElement != null) {
//					agent.initialize(serverURLElement.getTextContent(), agentNameElement.getTextContent(), brokerType);
//				} else {
//					System.out.println("Execute Error : AgentXML file was lacked");
//				}
//			} else {
//				System.out.println("Execute Error : AgentXML file was wrong");
//			}
//
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
	
	public static void execute(String brokerHost, int brokerPort, String agentName, ArbiAgent agent, BrokerType brokerType) {
		agent.initialize(brokerType, brokerHost, brokerPort, agentName);
	}

}
