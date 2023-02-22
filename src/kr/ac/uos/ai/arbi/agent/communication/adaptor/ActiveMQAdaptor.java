package kr.ac.uos.ai.arbi.agent.communication.adaptor;

import java.io.EOFException;
import java.io.IOException;
import java.net.SocketTimeoutException;

import kr.ac.uos.ai.arbi.agent.ArbiAgentMessage;
import kr.ac.uos.ai.arbi.agent.AgentMessageAction;
import kr.ac.uos.ai.arbi.agent.communication.ArbiMessageQueue;
import kr.ac.uos.ai.arbi.framework.ArbiFrameworkServer;

import org.apache.activemq.transport.stomp.StompConnection;
import org.apache.activemq.transport.stomp.StompFrame;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;



public class ActiveMQAdaptor implements ArbiMessageAdaptor {
	private ArbiMessageQueue			queue;
	private String agentURI;

	private StompConnection	connection;
	private MessageRecvTask messageRecvTask;

	public ActiveMQAdaptor(String brokerHost, int brokerPort, String agentURI, ArbiMessageQueue queue) {
		try {
			connection = new StompConnection();
			connection.open(brokerHost, brokerPort);
			
			this.agentURI = agentURI;
			this.queue = queue;
			
			messageRecvTask = new MessageRecvTask();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	private class MessageRecvTask extends Thread {
		@Override
		public void run() {
			while (true) {
				String message = "";
				try {
					StompFrame messageFrame;
					messageFrame = connection.receive();
					if(messageFrame != null) message = messageFrame.getBody();
					else continue;
				}
				catch(SocketTimeoutException e) {
					continue;
				}
				catch(EOFException e) {
					System.err.println("server connection disconnected.");	
					break;
				}
				catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					continue;
				}
				if(message.isEmpty()) continue;
				onMessage(message);
			} 
		}
	}
	
	@Override
	public void start() {	
		try {	
			connection.connect("system", "manager");
			connection.subscribe(agentURI + "/message");
			messageRecvTask.start();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void close() {
		try {
			connection.close();
			messageRecvTask.stop();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void send(ArbiAgentMessage message)  {
		try {
			JSONObject messageObject = new JSONObject();
			
			messageObject.put("sender", message.getSender());
			messageObject.put("receiver", message.getReceiver());
			messageObject.put("command", "Arbi-Agent");
			messageObject.put("action", message.getAction().toString());
			messageObject.put("content", message.getContent());
			messageObject.put("conversationID", message.getConversationID());
			messageObject.put("timestamp", message.getTimestamp());
			
			connection.send(ArbiFrameworkServer.URL, messageObject.toJSONString());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void onMessage(String message) {
		try {
			JSONParser jsonParser = new JSONParser();
			JSONObject messageObject = (JSONObject) jsonParser.parse(message);
			
			String command = messageObject.get("command").toString();
			if (command.startsWith("Arbi-Agent")) {
				String sender = messageObject.get("sender").toString();
				String receiver = messageObject.get("receiver").toString();
				String action = messageObject.get("action").toString();
				String content = messageObject.get("content").toString();
				String conversationID = messageObject.get("conversationID").toString();
				long timestamp = Long.parseLong(messageObject.get("timestamp").toString());
				ArbiAgentMessage agentMessage = new ArbiAgentMessage(sender, receiver,
						AgentMessageAction.valueOf(action), content, conversationID, timestamp);
				queue.enqueue(agentMessage);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
