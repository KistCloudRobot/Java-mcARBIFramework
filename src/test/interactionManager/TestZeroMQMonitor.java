package test.interactionManager;

import java.net.SocketTimeoutException;
import java.util.Base64;
import java.util.Base64.Decoder;
import java.util.HashMap;

import org.apache.activemq.transport.stomp.StompConnection;
import org.apache.activemq.transport.stomp.StompFrame;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.zeromq.SocketType;
import org.zeromq.ZMQ;
import org.zeromq.ZMQ.Context;
import org.zeromq.ZMQ.Socket;

import kr.ac.uos.ai.arbi.agent.communication.ArbiMessageQueue;
import kr.ac.uos.ai.arbi.interaction.InteractionManager;
import kr.ac.uos.ai.arbi.interaction.InteractionManagerBrokerConfiguration;
import kr.ac.uos.ai.arbi.interaction.MonitorMessageQueue;

public class TestZeroMQMonitor {
	private Context zmqContext;
	private Socket zmqSocket;
//	private Socket zmqProducer;
//	private Socket zmqConsumer;
	private MessageRecvTask messageRecvTask;
	private static final String MONITOR_ID = "testMonitor";
	
	public TestZeroMQMonitor() {
		zmqContext = ZMQ.context(1);
		zmqSocket = zmqContext.socket(SocketType.DEALER);
		zmqSocket.connect("tcp://127.0.0.1:61614");
		zmqSocket.setIdentity(MONITOR_ID.getBytes());
//		zmqProducer = zmqContext.socket(SocketType.DEALER);
//		zmqConsumer = zmqContext.socket(SocketType.DEALER);
//		zmqProducer.connect(brokerURL);
//		zmqProducer.setIdentity(agentURI.getBytes());
//		zmqConsumer.connect(brokerURL);
//		zmqConsumer.setIdentity((agentURI + "/message").getBytes());

		messageRecvTask = new MessageRecvTask();
		messageRecvTask.start();
		
		createMonitor();
	}
	
	private class MessageRecvTask extends Thread {
		@Override
		public void run() {
			while (true) {
				System.out.println("message receiving");
				String message = "";
				while(message.length() == 0) {
					message = zmqSocket.recvStr();
				}
				System.out.println("on message : " + message);

				try {
					JSONParser jsonParser = new JSONParser();
					JSONObject messageObject = (JSONObject) jsonParser.parse(message);
					String content = messageObject.get("Content").toString();
					System.out.println("before content : " + content);
					
					Decoder decoder = Base64.getDecoder();
					content = new String(decoder.decode(content.getBytes()));
					System.out.println("after content : " + content);
					
					
				} catch (Exception e) {
					System.err.println("parsing error");
				}
			} 
		}
	}
	
	private void createMonitor() {
		JSONObject createMonitorMessage = new JSONObject();
		createMonitorMessage.put("Action", "create monitor");
		createMonitorMessage.put("ID", MONITOR_ID);
		createMonitorMessage.put("Protocol", "ZeroMQ");

		JSONArray filterArray = new JSONArray();
		
		JSONObject filter1 = new JSONObject();
		filter1.put("LogType", "MessageLog");
		filter1.put("Action", "Inform");
		filter1.put("Flag", true);
		filterArray.add(filter1);
		
		JSONObject filter2 = new JSONObject();
		filter2.put("LogType", "MessageLog");
		filter2.put("Action", "AssertFact");
		filter2.put("Flag", true);
		filterArray.add(filter2);
		
		JSONObject filter3 = new JSONObject();
		filter3.put("LogType", "SystemLog");
		filter3.put("Actor", "testAgent");
		filter3.put("Action", "testAction");
		filter3.put("Flag", true);
		filterArray.add(filter3);
		
		JSONObject filter4 = new JSONObject();
		filter4.put("LogType", "MessageLog");
		filter4.put("Action", "UpdateFact");
		filter4.put("Flag", true);
		filterArray.add(filter4);
		
		createMonitorMessage.put("Filter", filterArray);
		
		zmqSocket.sendMore("");
		zmqSocket.send(createMonitorMessage.toJSONString());
	}
	
	public static void main(String[] args) {
		TestZeroMQMonitor monitor = new TestZeroMQMonitor();
		while(true);
	}
}
