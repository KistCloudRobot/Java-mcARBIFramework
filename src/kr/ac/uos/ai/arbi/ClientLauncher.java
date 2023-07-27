package kr.ac.uos.ai.arbi;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class ClientLauncher extends Thread {

	private HashMap<String, String> agentMainClassMap;
	private ArrayList<String> psList;
	private String osElement;

	public ClientLauncher() {
		File configFile = new File("configuration/ClientConfiguration.xml");
		agentMainClassMap = new HashMap<>();
		configParse(configFile);
	}

	private void configParse(File configFile) {

		try {
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(configFile);
			doc.getDocumentElement().normalize();
			Element configElement = (Element) doc.getElementsByTagName("ARBIFrameworkClientConfiguration").item(0);

			if (configElement != null) {

				osElement = doc.getElementsByTagName("OS").item(0).getTextContent();
				osElement = osElement.toUpperCase();
				NodeList nodeList = doc.getElementsByTagName("ClientAgent");

				for (int i = 0; i < nodeList.getLength(); i++) {
					Element nodeElement = (Element) nodeList.item(i);
					String agentClassPath = nodeElement.getElementsByTagName("AgentClassPath").item(0).getTextContent();
					String agentMainClass = nodeElement.getElementsByTagName("AgentMainClass").item(0).getTextContent();

					String scriptStr = "cd " + agentClassPath + ";";
					scriptStr += "java " + agentMainClass;

					if (osElement.equals("LINUX") || osElement.equals("UNIX")) {
						FileWriter fw = new FileWriter("management/" + agentMainClass + ".sh");
						PrintWriter pw = new PrintWriter(fw);
						pw.println("#!/bin/bash");
						pw.println(scriptStr);
						pw.close();

						agentMainClassMap.put(agentMainClass, "management/" + agentMainClass + ".sh");

					} else if (osElement.equals("WINDOW")) {

						FileWriter fw = new FileWriter("management/" + agentMainClass + ".bat");
						PrintWriter pw = new PrintWriter(fw);
						pw.println(scriptStr);
						pw.close();

						agentMainClassMap.put(agentMainClass, "management/" + agentMainClass + ".bat");

					}
				}

			}

		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public void run() {
		while (true) {
			try {
				List<String> cmdList = new ArrayList<String>();
				if (osElement.equals("LINUX") || osElement.equals("UNIX")) {
					cmdList.add("/bin/bash");
					cmdList.add("-c");
					cmdList.add("jps -l");

				} else if (osElement.equals("WINDOW")) {
					cmdList.add("cmd");
					cmdList.add("/c");
					cmdList.add("jps -l");
				}

				Process process = new ProcessBuilder(cmdList).start();
				Scanner scanner = new Scanner(process.getInputStream());
				psList = new ArrayList<>();
				while (scanner.hasNextLine() == true) {
					String ps = scanner.nextLine();
					System.out.println("[PS] " + ps);
					String[] psSplit = ps.split(" ");
					if(psSplit.length >= 2)
						psList.add(ps.split(" ")[1]);
				}

				for (String agentMainClass : agentMainClassMap.keySet()) {
					System.out.println("agent Main Class = " +agentMainClass);
					boolean isRun = false;
					for (int i = 0; i < psList.size(); i++) {
						if (agentMainClass.equals(psList.get(i))) {
							isRun = true;
						}
					}
					if (!isRun) {
						System.out.println("agent Main Class2 = " +agentMainClass);
						executeCMD(agentMainClass);
					}
				}

			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();

			}

			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();

			}
		}
	}

	private void executeCMD(String agentMainClass) {

		try {
			List<String> cmdList = new ArrayList<String>();
			if (osElement.equals("LINUX") || osElement.equals("UNIX")) {
				cmdList.add("/bin/bash");
				cmdList.add("-c");
				cmdList.add("sh management/" + agentMainClass + ".sh");

			} else if (osElement.equals("WINDOW")) {
				cmdList.add("cmd");
				cmdList.add("/c");
				cmdList.add("management/" + agentMainClass + ".bat");
			}

			Process process = new ProcessBuilder(cmdList).start();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		ClientLauncher cl = new ClientLauncher();
		cl.start();
	}
}
