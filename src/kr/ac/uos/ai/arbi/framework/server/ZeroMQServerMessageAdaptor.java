package kr.ac.uos.ai.arbi.framework.server;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Properties;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.MapMessage;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Session;

import org.apache.activemq.ActiveMQConnectionFactory;
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
import kr.ac.uos.ai.arbi.framework.ArbiFrameworkServer;
import kr.ac.uos.ai.arbi.interaction.InteractionManager;
import kr.ac.uos.ai.arbi.ltm.LTMMessageAction;
import kr.ac.uos.ai.arbi.ltm.communication.LTMMessageFactory;
import kr.ac.uos.ai.arbi.ltm.communication.LTMMessageQueue;
import kr.ac.uos.ai.arbi.ltm.communication.message.LTMMessage;
import kr.ac.uos.ai.arbi.utility.Configuration;
import kr.ac.uos.ai.arbi.utility.DebugUtilities;
import zmq.Poller;

public class ZeroMQServerMessageAdaptor implements MessageDeliverAdaptor, LTMMessageAdaptor {
	private Context zmqContext;
	private Socket zmqConsumer;
	private Object recvLock = new Object();
	private Object sendLock = new Object();
	private MessageRecvTask messageRecvTask;
	private Socket zmqProducer;
	private MessageService service;
	private HashMap<String,Socket> socketMap;
	private LinkedList<String> socketNameList;
	
	private ArbiMessageQueue messageQueue;
	private LTMMessageQueue ltmMessageQueue;
	private String brokerName = "";
	private String centerURL = "";
	private String brokerURL = "";
	
	public ZeroMQServerMessageAdaptor(MessageService service,ArbiMessageQueue arbiQueue,LTMMessageQueue ltmQueue, String brokerName) {
		this.service = service;
		messageQueue = arbiQueue;
		ltmMessageQueue = ltmQueue;
		this.brokerName = brokerName;
		socketNameList = new LinkedList<String>();
	}

	@Override
	public void initialize(String centerURL,String brokerURL) {
		this.centerURL = centerURL;
		this.brokerURL = brokerURL;
		zmqContext = ZMQ.context(1);
		zmqConsumer = zmqContext.socket(ZMQ.ROUTER);
		System.out.println("consumer server url : " + brokerURL);

		zmqConsumer.bind(brokerURL);
		
		//zmqConsumer.setRcvHWM(1000000);
		//zmqConsumer.setSndHWM(1000000);
		socketMap = new HashMap<String,Socket>();
		
		zmqProducer = zmqContext.socket(ZMQ.DEALER);
		zmqProducer.setIdentity(this.brokerName.getBytes());
		System.out.println("connected to center " + centerURL );
		zmqProducer.connect(centerURL);
		
		this.sendInitMessage();
		
		messageRecvTask = new MessageRecvTask();
		messageRecvTask.setName("serverAdapterThread");
		messageRecvTask.start();
		

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
			}else if(command.startsWith("Address-List")) {
				this.handleRouterList(messageObject);
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
		}

	}

	private class MessageRecvTask extends Thread {

		@Override
		public void run() {
			try {
				while (true) {

					Thread.sleep(5);
						
					String message = "";
					message = zmqConsumer.recvStr();
					while(zmqConsumer.hasReceiveMore() == true) {
						message =  zmqConsumer.recvStr();
					}
					
					if(Configuration.getLogAvailability() == true) {
						System.out.println(DebugUtilities.getDate() + " ZEROMQServerMessageAdaptor recvd message : " + message);
					}
					handleMessage(message);
<<<<<<< HEAD
=======
					
>>>>>>> refs/remotes/origin/origin
				}
			} catch (InterruptedException e) {
			// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	@Override
	public void send(LTMMessage msg) {
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
			zmqConsumer.sendMore("");
			zmqConsumer.send(messageObject.toJSONString());
<<<<<<< HEAD
			
=======
>>>>>>> refs/remotes/origin/origin
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	@Override
	public void notify(LTMMessage msg) {
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
	public void deliver(ArbiAgentMessage message) {
		try {
			
			
			String receiverURL = message.getReceiver();
			
			String receiverDestination = receiverURL;
			
			Socket sendSocket = null;
			
			JSONObject messageObject = new JSONObject();
			messageObject.put("sender", message.getSender());
			messageObject.put("receiver", message.getReceiver());
			messageObject.put("command", "Arbi-Agent");
			messageObject.put("action", message.getAction().toString());
			messageObject.put("content", message.getContent());
			messageObject.put("conversationID", message.getConversationID());
			messageObject.put("timestamp", message.getTimestamp());
			
			String receiverID = message.getReceiver();
			String receiverName = this.getAgentName(receiverID);
			
			if(receiverName.equals(this.brokerName) == false) {
				sendSocket = this.socketMap.get(receiverName);
			}else {
				sendSocket = zmqConsumer;
				receiverDestination += "/message";
			}
			
			sendSocket.sendMore(receiverDestination);
			sendSocket.sendMore("");
			sendSocket.send(messageObject.toJSONString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void sendInitMessage() {
		JSONObject messageObject = new JSONObject();
		messageObject.put("sender",brokerName);
		messageObject.put("address", this.brokerURL);
		messageObject.put("command", "RegisterRouter");

		zmqProducer.sendMore("");
		zmqProducer.send(messageObject.toJSONString());
	}
	
	public String getAgentName(String input) {
		String resultStringList[] = input.split("/");
		if(resultStringList.length > 3) {
			return resultStringList[3];
		}
		return "";
		
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
	
	public void handleRouterList(JSONObject jsonObject) {
		JSONArray whitelist = (JSONArray)jsonObject.get("content");
		
		
		for(int i = 0; i < whitelist.size();i++) {
			JSONObject addressData = (JSONObject)whitelist.get(i);
			String name = addressData.get("name").toString();
			String address = addressData.get("address").toString();
			
			if(name.equals(this.brokerName) == false && this.socketMap.containsKey(name) == false) {
				Socket newSocket = zmqContext.socket(ZMQ.DEALER);
				newSocket.setIdentity(this.brokerName.getBytes());
				newSocket.connect(address);
				
				this.socketMap.put(name, newSocket);
				this.socketNameList.add(name);
				System.out.println("router added " + name + " : " + address );
			}
		}
		
		System.out.println("router size : " + this.socketMap.size());
	}
	
	public  void deliverToMonitor(ArbiAgentMessage message) {

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
			e.printStackTrace();
		}

	}

	public void deliverToMonitor(LTMMessage msg) {
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
