package kr.ac.uos.ai.arbi.monitor.control;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.zeromq.ZMQ;
import org.zeromq.ZMQ.Context;
import org.zeromq.ZMQ.Socket;

import kr.ac.uos.ai.arbi.agent.AgentMessageAction;
import kr.ac.uos.ai.arbi.agent.ArbiAgentMessage;
import kr.ac.uos.ai.arbi.agent.communication.ArbiMessageQueue;

public class ZeroMQMonitorAdapter {
	private String arbiAgentURI;

	private MessageRecvTask messageRecvTask;

	private Context zmqContext;
	private Socket zmqProducer;
	private Socket zmqConsumer;

	private ArbiMessageQueue queue;
	
	public ZeroMQMonitorAdapter(String broker, String myURI, ArbiMessageQueue queue) {
		this.arbiAgentURI = myURI;
		this.queue = queue;
		zmqContext = ZMQ.context(1);
		
		zmqProducer = zmqContext.socket(ZMQ.DEALER);
		zmqProducer.connect(broker);
		zmqProducer.setIdentity(arbiAgentURI.getBytes());
		
		
		zmqConsumer = zmqContext.socket(ZMQ.DEALER);
		zmqConsumer.connect(broker);
		zmqConsumer.setIdentity((arbiAgentURI + "/message").getBytes());

		messageRecvTask = new MessageRecvTask();
		messageRecvTask.start();
		
		System.out.println("Broker Connected : " + broker);
	}

	public void close() {

		if (zmqProducer != null)
			zmqProducer.close();

		if (zmqConsumer != null)
			zmqConsumer.close();

		if (zmqContext != null)
			zmqContext.term();

		messageRecvTask.stop();
	}

	public void send(ArbiAgentMessage message) {
		boolean isRequireResponse = false;

		JSONObject messageObject = new JSONObject();
		messageObject.put("sender", message.getSender());
		messageObject.put("receiver", message.getReceiver());
		messageObject.put("command", "Arbi-Agent");
		messageObject.put("action", message.getAction().toString());
		messageObject.put("content", message.getContent());
		messageObject.put("conversationID", message.getConversationID());
		
		
		zmqProducer.sendMore("");
		zmqProducer.send(messageObject.toJSONString());
	}

	
	private class MessageRecvTask extends Thread {

		@Override
		public void run() {

			while (true) {

				zmqConsumer.recvStr();
				String message = zmqConsumer.recvStr();
				
				try {

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
						ArbiAgentMessage agentMessage = new ArbiAgentMessage(sender, receiver,
								AgentMessageAction.valueOf(action), content, conversationID);
						queue.enqueue(agentMessage);
					}

				} catch (ParseException e) {
					zmqConsumer.recvStr();
					System.out.println("error checking");
					continue;
				}
			}
		}

	}
}
