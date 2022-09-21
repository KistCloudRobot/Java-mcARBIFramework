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
	private Socket zmqConsumer;
	private Object recvLock = new Object();
	private Object sendLock = new Object();
	private MessageRecvTask messageRecvTask;
	
	private String brokerName = "";
	private String brokerURL = "";
	
	public ZeroMQMessageAdaptor(MessageService service,ArbiMessageQueue arbiQueue,LTMMessageQueue ltmQueue) {
		super(service, arbiQueue, ltmQueue);
	}

	@Override
	public void initialize(String brokerURL) {
		this.brokerURL = brokerURL;
		zmqContext = ZMQ.context(1);
		zmqConsumer = zmqContext.socket(SocketType.ROUTER);
		zmqConsumer.setReceiveTimeOut(200);
		zmqConsumer.bind(brokerURL);
		
		messageRecvTask = new MessageRecvTask();
		messageRecvTask.setName("serverAdapterThread");
		messageRecvTask.start();
	}


	private class MessageRecvTask extends Thread {

		@Override
		public void run() {
			try {
				while (true) {

					Thread.sleep(1);
						
					String message = "";
					message = zmqConsumer.recvStr();
					//System.out.println(DebugUtilities.getDate() + " ZEROMQServerMessageAdaptor recvd message : " + message);
					
					while(zmqConsumer.hasReceiveMore() == true) {
						Thread.sleep(1);
						message =  zmqConsumer.recvStr();
						//System.out.println(DebugUtilities.getDate() + " ZEROMQServerMessageAdaptor recvd message : " + message);
						
					}
					
					if(message != null)
						handleMessage(message);
					//testing
				}
			} catch (InterruptedException e) {
			// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	@Override
	protected void send(JSONObject msg) {
		String receiverURL = msg.get("client").toString();
		String receiverDestination = receiverURL + "/message";
		
		zmqConsumer.sendMore(receiverDestination);
		zmqConsumer.sendMore("");
		zmqConsumer.send(msg.toJSONString());
	}
}
