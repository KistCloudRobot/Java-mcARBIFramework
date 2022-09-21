package kr.ac.uos.ai.arbi.framework.server.adaptor;

import java.util.LinkedList;

import javax.json.JsonObject;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import kr.ac.uos.ai.arbi.agent.AgentMessageAction;
import kr.ac.uos.ai.arbi.agent.ArbiAgentMessage;
import kr.ac.uos.ai.arbi.agent.communication.ArbiMessageQueue;
import kr.ac.uos.ai.arbi.framework.server.MessageService;
import kr.ac.uos.ai.arbi.interaction.InteractionManager;
import kr.ac.uos.ai.arbi.ltm.LTMMessageAction;
import kr.ac.uos.ai.arbi.ltm.communication.LTMMessageFactory;
import kr.ac.uos.ai.arbi.ltm.communication.LTMMessageQueue;
import kr.ac.uos.ai.arbi.ltm.communication.message.LTMMessage;

public abstract class MessageAdaptor {
	private MessageService service;
	private ArbiMessageQueue messageQueue;
	private LTMMessageQueue ltmMessageQueue;
	
	public MessageAdaptor(MessageService service, ArbiMessageQueue arbiQueue,LTMMessageQueue ltmQueue) {
		this.service = service;
		messageQueue = arbiQueue;
		ltmMessageQueue = ltmQueue;
	}
	
	public abstract void initialize(String brokerURL);
	
	protected abstract void send(JSONObject msg);
	
	public void send(LTMMessage msg) {
		JSONObject messageObject = new JSONObject();
		messageObject.put("client", msg.getClient());
		messageObject.put("command", "Long-Term-Memory");
		messageObject.put("action", msg.getAction().toString());
		messageObject.put("content", msg.getContent());
		messageObject.put("conversationID", msg.getConversationID());
		
		this.send(messageObject);
	}
	
	public void deliver(ArbiAgentMessage message) {
		JSONObject messageObject = new JSONObject();
		messageObject.put("sender", message.getSender());
		messageObject.put("receiver", message.getReceiver());
		messageObject.put("command", "Arbi-Agent");
		messageObject.put("action", message.getAction().toString());
		messageObject.put("content", message.getContent());
		messageObject.put("conversationID", message.getConversationID());
		messageObject.put("timestamp", message.getTimestamp());
		
		this.send(messageObject);
	}
	
	public void deliverToMonitor(ArbiAgentMessage message) {
		JSONObject messageObject = new JSONObject();
		messageObject.put("sender", "Server");
		messageObject.put("receiver", InteractionManager.interactionManagerURI);
		messageObject.put("command", "Arbi-Agent");
		messageObject.put("action", "System");
		String content = "(MessageLog (Sender \"" + message.getSender() + "\") (Receiver \""
				+ message.getReceiver() + "\")" + " (Type \"AgentMessage\") (Action \"" + message.getAction().toString() + "\") (Content \""
				+ message.getContent().replace("\"", "\\\"") + "\"))";
		messageObject.put("content", content);
		messageObject.put("conversationID", message.getConversationID());
		messageObject.put("timestamp", message.getTimestamp());

		this.send(messageObject);
	}
	
	public void deliverToMonitor(LTMMessage message) {
		JSONObject messageObject = new JSONObject();
		messageObject.put("sender", "Server");
		messageObject.put("receiver", InteractionManager.interactionManagerURI);
		messageObject.put("command", "Arbi-Agent");
		messageObject.put("action", "System");
		String content = "(MessageLog (Client \"" + message.getClient() + "\")  (Type \"LTMMessage\") (Action \""
				+ message.getAction().toString() + "\")" + " (Content \"" + message.getContent().replace("\"", "\\\"")
				+ "\"))";
		messageObject.put("content", content);
		messageObject.put("conversationID", message.getConversationID());
		//TODO LTM message timestamp
		messageObject.put("timestamp", System.currentTimeMillis());

		this.send(messageObject);
	}

	public void handleMessage(String message) {
		try {
			JSONParser jsonParser = new JSONParser();
			JSONObject messageObject = (JSONObject) jsonParser.parse(message);

			if (messageObject == null)
				return;

			String command = messageObject.get("command").toString();

			if (command == null)
				return;

			if (command.startsWith("Arbi-Agent")) {
				String sender = messageObject.get("sender").toString();
				String receiver = messageObject.get("receiver").toString();
				String action = messageObject.get("action").toString();
				String content = messageObject.get("content").toString();
				String conversationID = messageObject.get("conversationID").toString();
				long timestamp = Long.parseLong(messageObject.get("timestamp").toString());
				ArbiAgentMessage agentMessage = new ArbiAgentMessage(sender, receiver,
						AgentMessageAction.valueOf(action), content, conversationID, timestamp);
				messageQueue.enqueue(agentMessage);
			} else if (command.startsWith("Long-Term-Memory")) {
				String client = messageObject.get("client").toString();
				String action = messageObject.get("action").toString();
				String content = messageObject.get("content").toString();
				String queryID = messageObject.get("conversationID").toString();
				LTMMessageFactory f = LTMMessageFactory.getInstance();
				LTMMessage ltmMessage = f.newMessage(client, LTMMessageAction.valueOf(action), content, queryID);
				ltmMessageQueue.enqueue(ltmMessage);
			} else if (command.startsWith("InteractionManager-Status")) {
				String status = messageObject.get("status").toString();
				service.messageRecieved(status);
			}
			else {
				System.out.println("undefined message command");
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
		}
	}
}