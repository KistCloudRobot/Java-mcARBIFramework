package kr.ac.uos.ai.arbi.framework.server;

import kr.ac.uos.ai.arbi.BrokerType;
import kr.ac.uos.ai.arbi.agent.AgentMessageAction;
import kr.ac.uos.ai.arbi.agent.ArbiAgentMessage;
import kr.ac.uos.ai.arbi.agent.communication.ArbiMessageQueue;
import kr.ac.uos.ai.arbi.framework.server.adaptor.ActiveMQMessageAdaptor;
import kr.ac.uos.ai.arbi.framework.server.adaptor.MessageAdaptor;
import kr.ac.uos.ai.arbi.framework.server.adaptor.ZeroMQMessageAdaptor;
import kr.ac.uos.ai.arbi.ltm.communication.LTMMessageQueue;
import kr.ac.uos.ai.arbi.ltm.communication.adaptor.ActiveMQAdaptor;
import kr.ac.uos.ai.arbi.ltm.communication.adaptor.ZeroMQLTMAdaptor;
import kr.ac.uos.ai.arbi.ltm.communication.message.LTMMessage;
import kr.ac.uos.ai.arbi.ltm.LTMMessageAction;

public class MessageService {
	private MessageAdaptor messageAdaptor;
//	private LTMMessageAdaptor ltmAdaptor;
	private LTMMessageListener ltmListener;
	private boolean interactionManagerStatus;
	private LTMMessageQueue ltmMessageQueue;
	private ArbiMessageQueue arbiMessageQueue;
	private ArbiAgentMessageService agentMessageService;
	private LTMMessageService ltmMessageService;
	private boolean isRunning = false;
	
	public MessageService(LTMMessageListener listener, BrokerType brokerType, String host, int port) {
		this.ltmListener = listener;
		interactionManagerStatus = false;
		arbiMessageQueue = new ArbiMessageQueue();
		ltmMessageQueue = new LTMMessageQueue();	
		
		agentMessageService = new ArbiAgentMessageService(arbiMessageQueue);
		ltmMessageService = new LTMMessageService(ltmMessageQueue);

		switch(brokerType) {
			case ACTIVEMQ:
				this.messageAdaptor = new ActiveMQMessageAdaptor(this, arbiMessageQueue, ltmMessageQueue, host, port);
	//			this.ltmAdaptor = new ActiveMQAdaptor(brokerURL, "tcp://ltmServer", ltmMessageQueue);
				break;
			case APOLLO:
				//TODO apollo
				System.out.println("Apollo not developted");
				break;
			case ZEROMQ:
				this.messageAdaptor = new ZeroMQMessageAdaptor(this, arbiMessageQueue, ltmMessageQueue, host, port);
	//			this.ltmAdaptor = new ZeroMQLTMAdaptor(brokerURL, "tcp://ltmServer", ltmMessageQueue);
				break;
			default:
				System.out.println("undefined broker type : " + brokerType.toString());
				break;
		}
	}


	public void start() {
		Thread agentMessageServiceThread = new Thread(this.agentMessageService);
		Thread ltmMessageServiceThread = new Thread(this.ltmMessageService);
		agentMessageServiceThread.start();
		ltmMessageServiceThread.start();
		this.messageAdaptor.start();
	}
	

	public void agentMessageReceived(ArbiAgentMessage agentMessage) {
		if(agentMessage.getAction() != AgentMessageAction.Notify) {
			System.out.println("[Agent Message]\t<" + agentMessage.getAction().toString() + ">\t" + agentMessage.getSender()
					+ " --> " + agentMessage.getReceiver() + " : " + agentMessage.getContent());
		}
		messageAdaptor.deliver(agentMessage);
		
		if (interactionManagerStatus) {
			messageAdaptor.deliverToMonitor(agentMessage);
		}
	}

	public synchronized void send(LTMMessage message) {
//		System.out.println("[LTM Message]\t<" + message.getAction().toString() + ">\t" + message.getClient() + " : "
//				+ message.getContent());
//		
		message.setSendingFromServer(true);
		
		ltmMessageQueue.enqueue(message);
	}
	
	public synchronized void notify(LTMMessage message) {
		
		message.setSendingFromServer(true);
		
		ltmMessageQueue.enqueue(message);
	}
	
	public void ltmMessageReceived(LTMMessage ltmMessage) {
		if (interactionManagerStatus) {
			messageAdaptor.deliverToMonitor(ltmMessage);
		}
		if(ltmMessage.getAction() == LTMMessageAction.Notify || ltmMessage.isSendingFromServer() == true) {
			messageAdaptor.send(ltmMessage);
		}else {
//			System.out.println("[LTM Message]\t<" + ltmMessage.getAction().toString() + ">\t" + ltmMessage.getClient()
//			+ " : " + ltmMessage.getContent());
			ltmListener.messageRecieved(ltmMessage);
		}
		
		
	}

	public void messageRecieved(String status) {
		if (status.equals("ON"))
			interactionManagerStatus = true;
		else if (status.equals("OFF"))
			interactionManagerStatus = false;
	}
	
	class ArbiAgentMessageService implements Runnable{
		private ArbiMessageQueue queue;
		private boolean isRunning = true;
		
		
		public ArbiAgentMessageService(ArbiMessageQueue queue) {
			this.queue = queue;
		}

		@Override
		public void run() {
			while(isRunning == true) {
				ArbiAgentMessage msg = queue.blockingDequeue(null, 100);
				if(msg != null) {
					agentMessageReceived(msg);
				}			
				
			}
			
		}

		public boolean isRunning() {
			return isRunning;
		}

		public void setRunning(boolean isRunning) {
			this.isRunning = isRunning;
		}

	}
	
	class LTMMessageService implements Runnable{
		private LTMMessageQueue queue;
		private boolean isRunning = true;
		
		public LTMMessageService(LTMMessageQueue queue) {
			this.queue = queue;
		}

		@Override
		public void run() {
			while(isRunning == true) {
				LTMMessage msg = queue.blockingDequeue(null, 10);

				if(msg != null) {
					ltmMessageReceived(msg);
				}
			}
		}
	}

}
