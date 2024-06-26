package kr.ac.uos.ai.arbi.interaction.adaptor;

import java.util.HashMap;

import org.json.simple.JSONObject;
import org.zeromq.ZMQ;
import org.zeromq.ZMQ.Context;
import org.zeromq.ZMQ.Socket;

import kr.ac.uos.ai.arbi.interaction.InteractionManagerBrokerConfiguration;
import kr.ac.uos.ai.arbi.interaction.MonitorMessageQueue;

public class ZeroMQAdaptor extends Thread implements InteractionMessageAdaptor {

	private Context zmqContext;
	private Socket zmqRouter;
	private Socket zmqProducer;
	private Socket zmqConsumer;
	
	private HashMap<String, ZMQ.Socket> zmqSocketMap;
	private MonitorMessageQueue queue;

	public ZeroMQAdaptor(MonitorMessageQueue queue) {
		this.queue = queue;
		
		String zeroMQBrokerHost = InteractionManagerBrokerConfiguration.getZeroMQBrokerHost();
		int zeroMQBrokerPort = InteractionManagerBrokerConfiguration.getZeroMQBrokerPort();
		
		String zeroMQURL = "tcp://" + zeroMQBrokerHost +":" +zeroMQBrokerPort;
		
		zmqContext = ZMQ.context(1);
		/*
		zmqProducer = zmqContext.socket(ZMQ.DEALER);
		zmqProducer.connect(InteractionManagerBrokerConfiguration.getZeroMQBroker());
		*/
		/*
		zmqConsumer = zmqContext.socket(ZMQ.SUB);
		zmqConsumer.bind(InteractionManagerBrokerConfiguration.getZeroMQBroker());
		zmqConsumer.subscribe(ZMQ.SUBSCRIPTION_ALL);
		*/
		
		zmqRouter = zmqContext.socket(ZMQ.ROUTER);

		zmqRouter.bind(zeroMQURL);
		System.out.println("ZeroMQ broker started");
		

		this.start();
	}

	@Override 
	public void run() {
		while (true) {
			String message = zmqRouter.recvStr();
			while(true) {
				message =  zmqRouter.recvStr();
				if(message == null || message.isEmpty()) continue;
				else break;
			}
			queue.enqueue(message);
		}
	}

	@Override
	public void close() {
		zmqRouter.close();
		zmqContext.term();
	}

	@Override
	public void send(String monitorID, String message) {

		//System.out.println("send message " + monitorID + " " + message);
		zmqRouter.sendMore(monitorID);
		zmqRouter.sendMore("");
		zmqRouter.send(message);
	}

	@Override
	public void sendStatus(String status) {
		// TODO Auto-generated method stub
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("command", "InteractionManager-Status");
		jsonObject.put("status", status);
		
		zmqRouter.sendMore("");
		zmqRouter.send(jsonObject.toJSONString());
	}

}
