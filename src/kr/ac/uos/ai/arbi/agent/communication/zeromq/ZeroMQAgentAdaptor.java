package kr.ac.uos.ai.arbi.agent.communication.zeromq;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.zeromq.ZMQ;
import org.zeromq.ZMQ.Context;
import org.zeromq.ZMQ.Socket;

import kr.ac.uos.ai.arbi.agent.AgentMessageAction;
import kr.ac.uos.ai.arbi.agent.ArbiAgentMessage;
import kr.ac.uos.ai.arbi.agent.communication.ArbiMessageAdaptor;
import kr.ac.uos.ai.arbi.agent.communication.ArbiMessageQueue;

public class ZeroMQAgentAdaptor implements ArbiMessageAdaptor {
	private String arbiAgentURI;

	private MessageRecvTask messageRecvTask;

	private Context zmqContext;
	private Socket zmqProducer;
	private Socket zmqConsumer;
	private String broker;
	private ArbiMessageQueue queue;
	private boolean isAlive;
	
	public ZeroMQAgentAdaptor(String broker, String myURI, ArbiMessageQueue queue){
		this.arbiAgentURI = myURI;
		this.queue = queue;
		this.broker = broker;
		zmqContext = ZMQ.context(1);
		this.broker = broker;
		
		zmqProducer = zmqContext.socket(ZMQ.DEALER);
		zmqProducer.connect(broker);
		zmqProducer.setIdentity(arbiAgentURI.getBytes());
		//zmqProducer.setSndHWM(0);
		
		zmqConsumer = zmqContext.socket(ZMQ.DEALER);
		zmqConsumer.connect(broker);
		zmqConsumer.setIdentity((arbiAgentURI + "/message").getBytes());
		//zmqConsumer.setRcvHWM(0);
		
		messageRecvTask = new MessageRecvTask();
		messageRecvTask.start();
		
		isAlive = true;
		System.out.println("Broker Connected : " + broker);
	}
	
	public void setSocketSize(int size) {
		for(int i = 0; i < size; i++) {
			zmqProducer = zmqContext.socket(ZMQ.DEALER);
			zmqProducer.connect(broker);
			zmqProducer.setIdentity((arbiAgentURI + Integer.toString(i)).getBytes());
			
		}
	}
	
	public boolean isAlive() {
		return isAlive;
	}

	public void setAlive(boolean isAlive) {
		this.isAlive = isAlive;
	}

	public void close() {
		if (zmqProducer != null)
			zmqProducer.close();

		if (zmqConsumer != null)
			zmqConsumer.close();

		if (zmqContext != null)
			zmqContext.term();
	}

	public synchronized void send(ArbiAgentMessage message) {
		JSONObject messageObject = new JSONObject();
		messageObject.put("sender", message.getSender());
		messageObject.put("receiver", message.getReceiver());
		messageObject.put("command", "Arbi-Agent");
		messageObject.put("action", message.getAction().toString());
		messageObject.put("content", message.getContent());
		messageObject.put("conversationID", message.getConversationID());
		messageObject.put("timestamp", message.getTimestamp());
		
		zmqProducer.sendMore(message.getReceiver());
		zmqProducer.sendMore("");
		zmqProducer.send(messageObject.toJSONString());
	}

	
	private class MessageRecvTask extends Thread {
		@Override
		public void run() {
			String message = "";
			while (isAlive == true) {
				
				try {
					
					message = zmqConsumer.recvStr();
					
					while(zmqConsumer.hasReceiveMore() == true) {
						message = zmqConsumer.recvStr();
					}
					
					JSONParser jsonParser = new JSONParser();
					JSONObject messageObject = (JSONObject) jsonParser.parse(message);

					if (messageObject == null)
						continue;
					
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
				}
				catch (org.zeromq.ZMQException e) {
					isAlive = false;
					break;
				}
				catch (ParseException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
