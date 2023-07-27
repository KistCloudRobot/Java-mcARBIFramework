package kr.ac.uos.ai.arbi.ltm.communication.adaptor;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.zeromq.SocketType;
import org.zeromq.ZMQ;
import org.zeromq.ZMQ.Context;
import org.zeromq.ZMQ.Socket;

import kr.ac.uos.ai.arbi.ltm.LTMMessageAction;
import kr.ac.uos.ai.arbi.ltm.communication.LTMMessageFactory;
import kr.ac.uos.ai.arbi.ltm.communication.LTMMessageQueue;
import kr.ac.uos.ai.arbi.ltm.communication.message.LTMMessage;

public class ZeroMQLTMAdaptor implements LTMMessageAdaptor {
	private String clientURI;
	private String brokerURL;
	private Context zmqContext;
	private boolean isAlive;
	private Socket zmqProducer;
	private Socket zmqConsumer;

	private MessageRecvTask messageRecvTask;

	private LTMMessageQueue queue;

	public ZeroMQLTMAdaptor(String brokerHost, int brokerPort, String clientURI, LTMMessageQueue queue) {
		this.brokerURL = "tcp://" + brokerHost + ":" + brokerPort;
		this.clientURI = clientURI;
		this.queue = queue;
		zmqContext = ZMQ.context(1);
		
		zmqProducer = zmqContext.socket(SocketType.DEALER);
		
		zmqConsumer = zmqContext.socket(SocketType.DEALER);
		
		messageRecvTask = new MessageRecvTask();
		
		isAlive = true;
	}
	
	public void start() {
		zmqProducer.connect(brokerURL);
		zmqProducer.setIdentity(clientURI.getBytes());
		zmqConsumer.connect(brokerURL);
		zmqConsumer.setIdentity((clientURI + "/message").getBytes());
		isAlive = true;
		messageRecvTask.start();
	}

	public void close() {
		if (zmqProducer != null)
			zmqProducer.close();

		if (zmqConsumer != null)
			zmqConsumer.close();

		if (zmqContext != null)
			zmqContext.term();

		isAlive = false;
	}

	public synchronized void send(LTMMessage message) {

		JSONObject messageObject = new JSONObject();
		messageObject.put("client", message.getClient());
		messageObject.put("command", "Long-Term-Memory");
		messageObject.put("action", message.getAction().toString());
		messageObject.put("content", message.getContent());
		messageObject.put("conversationID", message.getConversationID());

		zmqProducer.sendMore("");
		zmqProducer.send(messageObject.toJSONString());
		
	}

	private class MessageRecvTask extends Thread {

		@Override
		public void run() {

			String message = "";
			while (isAlive) {
				try {
					while(true) {
						Thread.sleep(1);
						message =  zmqConsumer.recvStr();
						if(message != null) {
							if(message.contains("{") || message.contains("}")) break;
						}
					}
					
					JSONParser jsonParser = new JSONParser();
					JSONObject messageObject = (JSONObject) jsonParser.parse(message);

					if (messageObject == null)
						continue;

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

				} catch (ParseException e) {
					e.printStackTrace();
				}catch (InterruptedException e1) {
					e1.printStackTrace();
				}

			}
		}

	}

	@Override
	public void notify(LTMMessage msg) {
		
	}

}
