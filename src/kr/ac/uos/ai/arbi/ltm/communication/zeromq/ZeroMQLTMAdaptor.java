package kr.ac.uos.ai.arbi.ltm.communication.zeromq;

import javax.jms.Connection;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Session;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.zeromq.ZMQ;
import org.zeromq.ZMQ.Context;
import org.zeromq.ZMQ.Socket;

import kr.ac.uos.ai.arbi.ltm.LTMMessageAction;
import kr.ac.uos.ai.arbi.ltm.communication.LTMMessageAdaptor;
import kr.ac.uos.ai.arbi.ltm.communication.LTMMessageFactory;
import kr.ac.uos.ai.arbi.ltm.communication.LTMMessageQueue;
import kr.ac.uos.ai.arbi.ltm.communication.message.LTMMessage;

public class ZeroMQLTMAdaptor implements LTMMessageAdaptor {
	private String clientURI;
	private Context zmqContext;
	private boolean isAlive;
	private Socket zmqProducer;
	private Socket zmqConsumer;

	private MessageRecvTask messageRecvTask;

	private LTMMessageQueue queue;

	public ZeroMQLTMAdaptor(String broker, String myURI, LTMMessageQueue queue) {
		this.clientURI = myURI;
		this.queue = queue;

		zmqContext = ZMQ.context(1);
		zmqProducer = zmqContext.socket(ZMQ.DEALER);
		System.out.println(broker);
		zmqProducer.connect(broker);
		zmqProducer.setIdentity((clientURI).getBytes());

		zmqConsumer = zmqContext.socket(ZMQ.DEALER);
		zmqConsumer.connect(broker);
		zmqConsumer.setIdentity((clientURI + "/message").getBytes());

		messageRecvTask = new MessageRecvTask();
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

			while (isAlive) {
				try {
					Thread.sleep(1);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				String text = "";
				String message = "";
				try {
					
					
					text = zmqConsumer.recvStr();
					
					while(zmqConsumer.hasReceiveMore() == true) {
						message = zmqConsumer.recvStr();

					}
				}catch (org.zeromq.ZMQException e) {
					System.out.println("thread terminated");
					isAlive = false;
					break;
				}
				
				try {
					
					
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
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}

	}

	@Override
	public void notify(LTMMessage msg) {
		// TODO Auto-generated method stub
		
	}

}
