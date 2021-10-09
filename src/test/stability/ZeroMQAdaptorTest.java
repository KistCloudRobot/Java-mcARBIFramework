package test.stability;


import java.util.Scanner;

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

public class ZeroMQAdaptorTest implements LTMMessageAdaptor {
	private String clientURI;
	private Context zmqContext;
	private boolean isAlive;
	private Socket zmqProducer;
	private Socket zmqConsumer;

	private MessageRecvTask messageRecvTask;

	private LTMMessageQueue queue;

	public ZeroMQAdaptorTest(String broker, String myURI, LTMMessageQueue queue) {
		this.clientURI = myURI;
		this.queue = queue;

		zmqContext = ZMQ.context(1);
		zmqProducer = zmqContext.socket(ZMQ.DEALER);
		System.out.println(broker);
		zmqProducer.connect(broker);
		zmqProducer.setIdentity((clientURI).getBytes());
		// zmqProducer.setSndHWM(0);
		zmqConsumer = zmqContext.socket(ZMQ.DEALER);
		zmqConsumer.connect(broker);
		zmqConsumer.setIdentity((clientURI + "/message").getBytes());
		// zmqConsumer.setRcvHWM(0);

		messageRecvTask = new MessageRecvTask();
		isAlive = true;
		
		messageRecvTask.start();
		
		Scanner sc = new Scanner(System.in);
		
		
		while(true) {
			String input = sc.nextLine();
			this.send(input);
			
		}
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

	public synchronized void send(String message) {

		JSONObject messageObject = new JSONObject();
		messageObject.put("client", "test");
		messageObject.put("command", "Long-Term-Memory");
		messageObject.put("action", "Inform");
		messageObject.put("content", message);
		messageObject.put("conversationID", System.currentTimeMillis());
		zmqConsumer.sendMore("test");
		zmqConsumer.sendMore("");
		zmqConsumer.send(messageObject.toJSONString());

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
					System.out.println("rcvd : " + text);
					while (zmqConsumer.hasReceiveMore() == true) {
						message = zmqConsumer.recvStr();
						System.out.println("rcvd : " + message);
					}
				} catch (org.zeromq.ZMQException e) {
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

	public static void main(String[] ar) {
		new ZeroMQAdaptorTest("tcp://127.0.0.1:61114", "test", new LTMMessageQueue());
	}

	@Override
	public void notify(LTMMessage msg) {
		// TODO Auto-generated method stub

	}

	@Override
	public void send(LTMMessage msg) {
		// TODO Auto-generated method stub
		
	}

}
