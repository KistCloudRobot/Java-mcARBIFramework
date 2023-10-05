package kr.ac.uos.ai.arbi.ltm.communication.adaptor;

import java.io.EOFException;
import java.io.IOException;
import java.net.SocketException;
import java.net.SocketTimeoutException;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import kr.ac.uos.ai.arbi.agent.communication.ArbiMessageQueue;
import kr.ac.uos.ai.arbi.framework.ArbiFrameworkServer;
import kr.ac.uos.ai.arbi.ltm.LTMMessageAction;
import kr.ac.uos.ai.arbi.ltm.communication.LTMMessageFactory;
import kr.ac.uos.ai.arbi.ltm.communication.LTMMessageQueue;
import kr.ac.uos.ai.arbi.ltm.communication.message.LTMMessage;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.transport.stomp.StompConnection;
import org.apache.activemq.transport.stomp.StompFrame;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class ActiveMQAdaptor implements LTMMessageAdaptor {
	private LTMMessageQueue			queue;
	private String					clientURI;

	private StompConnection	connection;
	private MessageRecvTask messageRecvTask;

	public ActiveMQAdaptor(String brokerHost, int brokerPort, String clientURI, LTMMessageQueue queue) {
		try {
			connection = new StompConnection();
			System.out.println(brokerHost + "  " + brokerPort);
			connection.open(brokerHost, brokerPort);
			
			this.clientURI = clientURI;
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
			connection.subscribe(clientURI + "/message");
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

	public void send(LTMMessage message)  {
		try {
			JSONObject messageObject = new JSONObject();
			messageObject.put("client", message.getClient());
			messageObject.put("command", "Long-Term-Memory");
			messageObject.put("action", message.getAction().toString());
			messageObject.put("content", message.getContent());
			messageObject.put("conversationID", message.getConversationID());
				
			connection.send(ArbiFrameworkServer.URL, messageObject.toJSONString());
		} 
		catch (SocketException e) {
			System.err.println("DataSource send failed. Connection is disconnected.");
		}
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void onMessage(String message) {	
		try {
			JSONParser jsonParser = new JSONParser();
			JSONObject messageObject = (JSONObject) jsonParser.parse(message);
			
			String command = messageObject.get("command").toString();
			if (command.startsWith("Long-Term-Memory")) {
				String client = messageObject.get("client").toString();
				String action = messageObject.get("action").toString();
				String content = messageObject.get("content").toString();
				String conversationID = messageObject.get("conversationID").toString();
				LTMMessageFactory f = LTMMessageFactory.getInstance();
				LTMMessage ltmMessage = f.newMessage(client, LTMMessageAction.valueOf(action), content,
						conversationID);
				queue.enqueue(ltmMessage);
			}
		} 
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public void notify(LTMMessage msg) {
		// TODO Auto-generated method stub
		
	}

}
