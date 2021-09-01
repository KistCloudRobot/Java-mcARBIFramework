package kr.ac.uos.ai.arbi.framework.server;

import java.util.HashMap;
import java.util.LinkedList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.zeromq.ZMQ;
import org.zeromq.ZMQ.Context;
import org.zeromq.ZMQ.Socket;

import kr.ac.uos.ai.arbi.agent.AgentMessageAction;
import kr.ac.uos.ai.arbi.agent.ArbiAgentMessage;
import kr.ac.uos.ai.arbi.agent.communication.ArbiMessageQueue;
import kr.ac.uos.ai.arbi.interaction.InteractionManager;
import kr.ac.uos.ai.arbi.ltm.LTMMessageAction;
import kr.ac.uos.ai.arbi.ltm.communication.LTMMessageFactory;
import kr.ac.uos.ai.arbi.ltm.communication.LTMMessageQueue;
import kr.ac.uos.ai.arbi.ltm.communication.message.LTMMessage;

public class McArbiServerAdaptor implements MessageDeliverAdaptor, LTMMessageAdaptor{

	private Context zmqContext;
	private Socket zmqConsumer;
	private Object recvLock = new Object();
	private Object sendLock = new Object();
	private MessageRecvTask messageRecvTask;
	private Socket zmqProducer;
	private MessageService service;
	private HashMap<String,Socket> socketMap;
	private LinkedList<RouterInfo> routerList;
	private ArbiMessageQueue messageQueue;
	private LTMMessageQueue ltmMessageQueue;

	public McArbiServerAdaptor(MessageService service,ArbiMessageQueue messageQueue, LTMMessageQueue ltmMessageQueue) {
		this.service = service;
		this.ltmMessageQueue = ltmMessageQueue;
		this.messageQueue = messageQueue;
		socketMap = new HashMap<String,Socket>();
		routerList = new LinkedList<RouterInfo>();
		
	}


	@Override
	public void initialize(String serverURL,String brokerURL) {

		zmqContext = ZMQ.context(1);
		zmqConsumer = zmqContext.socket(ZMQ.ROUTER);
		System.out.println("consumer server url : " + serverURL);
		System.out.println("center run");
		zmqConsumer.bind(serverURL);
		socketMap = new HashMap<String,Socket>();
	
		messageRecvTask = new MessageRecvTask();
		messageRecvTask.setName("ServerAdapterThread");
		messageRecvTask.start();

	}
	
	public String getAgentID(String address) {
		String adrList[] = address.split("/");
		
		return adrList[2];
	}
	
	public void routerConnected(String serverName, String ipAddress) {
		Socket dealer = this.zmqContext.socket(ZMQ.DEALER);
		
		dealer.connect(ipAddress);
		dealer.setIdentity("center".getBytes());
		this.socketMap.put(serverName,dealer);
		RouterInfo rInfo = new RouterInfo(serverName,ipAddress);
		
		this.routerList.add(rInfo);
		
		String sendData = generatePublishMessage();
		
		for (int i = 0; i < routerList.size();i++) {
			Socket rSocket = socketMap.get(routerList.get(i).getAgentName());
			
			rSocket.send("center");
			rSocket.send("");
			rSocket.send(sendData);
		}
		
	}
	
	public String generatePublishMessage() {
		JSONObject messageObject = new JSONObject();

		messageObject.put("sender","center");

		messageObject.put("command", "Address-List");
		JSONArray jArray = new JSONArray();
		for(int i = 0; i < routerList.size();i++) {
			JSONObject jObject = new JSONObject();
			jObject.put("name",routerList.get(i).getAgentName());
			jObject.put("address", routerList.get(i).getAddress());
			jArray.add(jObject);
		}
		messageObject.put("content", jArray);
		System.out.println("published");
		return messageObject.toJSONString();
	}
	
	public void connectRouter(String serverName, String ipAddress) {
		Socket dealer = this.zmqContext.socket(ZMQ.DEALER);
		dealer.connect(ipAddress);
		this.socketMap.put(serverName,dealer);
	}
	
	public void route(String serverName,String message) {
		socketMap.get(serverName).send(message);
		System.out.println("routed : " + serverName +  " : " + message);
	}
	
	
	public void handleMessage(String message) {
		try {
			JSONParser jsonParser = new JSONParser();
			JSONObject messageObject = (JSONObject) jsonParser.parse(message);
			System.out.println(message);
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
				this.messageQueue.enqueue(agentMessage);
				
			} else if (command.startsWith("Long-Term-Memory")) {
				String client = messageObject.get("client").toString();
				String action = messageObject.get("action").toString();
				String content = messageObject.get("content").toString();
				String queryID = messageObject.get("conversationID").toString();
				LTMMessageFactory f = LTMMessageFactory.getInstance();
				LTMMessage ltmMessage = f.newMessage(client, LTMMessageAction.valueOf(action), content, queryID);
				this.ltmMessageQueue.enqueue(ltmMessage);
				
			} else if (command.startsWith("InteractionManager-Status")) {
				String status = messageObject.get("status").toString();
				service.messageRecieved(status);
			}else if(command.startsWith("RegisterRouter")) {
				String sender = messageObject.get("sender").toString();
				String address = messageObject.get("address").toString();
				this.routerConnected(sender,address );
			}

		} catch (ParseException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
		}

	}
	
	private class RouterInfo {
		private String agentName;
		private String address;
		
		public RouterInfo(String agentName, String address) {
			this.agentName = agentName;
			this.address = address;
			
		}

		public String getAgentName() {
			return agentName;
		}


		public String getAddress() {
			return address;
		}
		
		
	}
	
	private class MessageRecvTask extends Thread {

		@Override
		public void run() {
			try {
				while (true) {

					Thread.sleep(1);
					String message = "";
					zmqConsumer.recvStr();
					while(zmqConsumer.hasReceiveMore() == true) {
						message =  zmqConsumer.recvStr();
						System.out.println("on message" + message);
					}
					
					handleMessage(message);
				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	@Override
	public synchronized void send(LTMMessage msg) {
		try {
			JSONObject messageObject = new JSONObject();
			messageObject.put("client", msg.getClient());
			messageObject.put("command", "Long-Term-Memory");
			messageObject.put("action", msg.getAction().toString());
			messageObject.put("content", msg.getContent());
			messageObject.put("conversationID", msg.getConversationID());

			String receiverURL = msg.getClient();
			String receiverDestination = receiverURL + "/message";
			zmqConsumer.sendMore(receiverDestination);
			System.out.println(receiverDestination);
			zmqConsumer.sendMore("");

			zmqConsumer.send(messageObject.toJSONString());
			System.out.println("message sent!! " + messageObject.toJSONString());
			System.out.println("content : " + msg.getContent());
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	@Override
	public synchronized void notify(LTMMessage msg) {
		try {
			JSONObject messageObject = new JSONObject();
			messageObject.put("client", msg.getClient());
			messageObject.put("command", "Long-Term-Memory");
			messageObject.put("action", msg.getAction().toString());
			messageObject.put("content", msg.getContent());
			messageObject.put("conversationID", msg.getConversationID());

			String receiverURL = msg.getClient();
			String receiverDestination = receiverURL + "/message";
			zmqProducer.sendMore(receiverDestination);

			zmqProducer.sendMore("");

			zmqProducer.send(messageObject.toJSONString());

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	@Override
	public synchronized void deliver(ArbiAgentMessage message) {
		try {
			String receiverURL = message.getReceiver();
			String receiverDestination = receiverURL + "/message";
			zmqConsumer.sendMore(receiverDestination);
			zmqConsumer.sendMore("");

			JSONObject messageObject = new JSONObject();
			messageObject.put("sender", message.getSender());
			messageObject.put("receiver", message.getReceiver());
			messageObject.put("command", "Arbi-Agent");
			messageObject.put("action", message.getAction().toString());
			messageObject.put("content", message.getContent());
			messageObject.put("conversationID", message.getConversationID());
			messageObject.put("timestamp", message.getTimestamp());

			zmqConsumer.send(messageObject.toJSONString());

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public  synchronized void deliverToMonitor(ArbiAgentMessage message) {

		try {

			String receiverDestination = InteractionManager.interactionAgentURI + "/message";
			zmqConsumer.sendMore(receiverDestination);
			zmqConsumer.sendMore("");

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

			zmqConsumer.send(messageObject.toJSONString());

		} catch (Exception e) {
			System.out.println(e.toString());
			e.printStackTrace();
		}

	}

	public synchronized void deliverToMonitor(LTMMessage msg) {
		try {

			String receiverDestination = InteractionManager.interactionAgentURI + "/message";
			zmqConsumer.sendMore(receiverDestination);
			zmqConsumer.sendMore("");

			JSONObject messageObject = new JSONObject();
			messageObject.put("sender", "Server");
			messageObject.put("receiver", InteractionManager.interactionManagerURI);
			messageObject.put("command", "Arbi-Agent");
			messageObject.put("action", "System");
			String content = "(MessageLog (Client \"" + msg.getClient() + "\")  (Type \"LTMMessage\") (Action \""
					+ msg.getAction().toString() + "\")" + " (Content \"" + msg.getContent().replace("\"", "\\\"")
					+ "\"))";
			messageObject.put("content", content);
			messageObject.put("conversationID", msg.getConversationID());
			messageObject.put("timestamp", 0);

			zmqConsumer.send(messageObject.toJSONString());

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
