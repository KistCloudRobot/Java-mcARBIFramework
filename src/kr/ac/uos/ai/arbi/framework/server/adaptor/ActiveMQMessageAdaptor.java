package kr.ac.uos.ai.arbi.framework.server.adaptor;

import java.io.EOFException;
import java.net.SocketTimeoutException;

import org.apache.activemq.transport.stomp.StompConnection;
import org.apache.activemq.transport.stomp.StompFrame;
import org.json.simple.JSONObject;

import kr.ac.uos.ai.arbi.agent.ArbiAgentMessage;
import kr.ac.uos.ai.arbi.agent.communication.ArbiMessageQueue;
import kr.ac.uos.ai.arbi.framework.ArbiFrameworkServer;
import kr.ac.uos.ai.arbi.framework.server.MessageService;
import kr.ac.uos.ai.arbi.ltm.communication.LTMMessageQueue;

public class ActiveMQMessageAdaptor extends MessageAdaptor {
	private StompConnection	connection;
	private MessageRecvTask messageRecvTask;

	public ActiveMQMessageAdaptor(MessageService service,ArbiMessageQueue arbiQueue,LTMMessageQueue ltmQueue, String host, int port) {
		super(service, arbiQueue, ltmQueue);
		try {
			connection = new StompConnection();
			connection.open(host, port);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void start() {	
		try {	
			connection.connect("system", "manager");
			connection.subscribe(ArbiFrameworkServer.URL);
			messageRecvTask = new MessageRecvTask();
			messageRecvTask.start();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void deliver(ArbiAgentMessage message) {
		try {
			JSONObject messageObject = new JSONObject();
			
			String receiver = message.getReceiver();
			messageObject.put("sender", message.getSender());
			messageObject.put("receiver", receiver);
			messageObject.put("command", "Arbi-Agent");
			messageObject.put("action", message.getAction().toString());
			messageObject.put("content", message.getContent());
			messageObject.put("conversationID", message.getConversationID());
			messageObject.put("timestamp", message.getTimestamp());
			
			this.send(receiver, messageObject);
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
//				System.out.println("on message : " + message);
				handleMessage(message);
			} 
		}
	}

	@Override
	protected void send(String receiver, JSONObject msg) {
		try {
			receiver = receiver + "/message";
			connection.send(receiver, msg.toJSONString());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
