package kr.ac.uos.ai.arbi.framework.server.adaptor;

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
import org.zeromq.SocketType;
import org.zeromq.ZMQ;
import org.zeromq.ZMQ.Context;
import org.zeromq.ZMQ.Socket;

import kr.ac.uos.ai.arbi.agent.AgentMessageAction;
import kr.ac.uos.ai.arbi.agent.ArbiAgentMessage;
import kr.ac.uos.ai.arbi.agent.communication.ArbiMessageQueue;
import kr.ac.uos.ai.arbi.framework.ArbiFrameworkServer;
import kr.ac.uos.ai.arbi.framework.server.MessageService;
import kr.ac.uos.ai.arbi.interaction.InteractionManager;
import kr.ac.uos.ai.arbi.ltm.LTMMessageAction;
import kr.ac.uos.ai.arbi.ltm.communication.LTMMessageFactory;
import kr.ac.uos.ai.arbi.ltm.communication.LTMMessageQueue;
import kr.ac.uos.ai.arbi.ltm.communication.message.LTMMessage;
import kr.ac.uos.ai.arbi.utility.Configuration;
import kr.ac.uos.ai.arbi.utility.DebugUtilities;

public class ZeroMQMessageAdaptor extends MessageAdaptor {
	private Context zmqContext;
	private Socket zmqRouter;
	private Object recvLock = new Object();
	private Object sendLock = new Object();
	private MessageRecvTask messageRecvTask;
	
	private String brokerName = "";
	private String brokerURL = "";
	
	public ZeroMQMessageAdaptor(MessageService service,ArbiMessageQueue arbiQueue,LTMMessageQueue ltmQueue, String host, int port) {
		super(service, arbiQueue, ltmQueue);
		String url = "tcp://" + host + ":" + port;
		zmqContext = ZMQ.context(1);
		zmqRouter = zmqContext.socket(SocketType.ROUTER);
		zmqRouter.setReceiveTimeOut(1);
		zmqRouter.bind(url);
	}

	@Override
	public void start() {
		messageRecvTask = new MessageRecvTask();
		messageRecvTask.start();
	}


	private class MessageRecvTask extends Thread {

		@Override
		public void run() {
			try {
				while (true) {
					String message = "";
					
//					message = zmqConsumer.recvStr();
//					message = zmqConsumer.recvStr();
//					message = zmqConsumer.recvStr();
					
					while(true) {
						Thread.sleep(1);
						message =  zmqRouter.recvStr();
						if(message != null) {
							if(message.contains("{") || message.contains("}")) break;
						}
					}
					System.out.println(message);
					handleMessage(message);
				} 
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	@Override
	protected synchronized void send(String receiver, JSONObject msg) {
		String receiverDestination = receiver + "/message";
		
		zmqRouter.sendMore(receiverDestination);
		zmqRouter.sendMore("");
		zmqRouter.send(msg.toJSONString());
	}
}
