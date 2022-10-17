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
//	private kr.ac.uos.ai.arbi.ltm.communication.LTMMessageAdaptor ltmAdaptor;
	private LTMMessageListener ltmListener;
	private boolean interactionManagerStatus;
	private BrokerType brokerType;
	private LTMMessageQueue ltmMessageQueue;
	private ArbiMessageQueue arbiMessageQueue;
	private ArbiAgentMessageService agentMessageService;
	private LTMMessageService ltmMessageService;
	private boolean isRunning = false;
	
	public MessageService(LTMMessageListener listener, BrokerType brokerType) {
		this.ltmListener = listener;
		this.brokerType = brokerType;
//		ltmAdaptor = null;
		interactionManagerStatus = false;
		arbiMessageQueue = new ArbiMessageQueue();
		ltmMessageQueue = new LTMMessageQueue();	
		
		agentMessageService = new ArbiAgentMessageService(arbiMessageQueue);
		ltmMessageService = new LTMMessageService(ltmMessageQueue);
		
	}


	public void initialize(String brokerURL) {
	
		switch(brokerType) {
		case ACTIVEMQ:
			this.messageAdaptor = new ActiveMQMessageAdaptor(this, arbiMessageQueue, ltmMessageQueue);
//			this.ltmAdaptor = new ActiveMQAdaptor(brokerURL, "tcp://ltmServer", ltmMessageQueue);
			break;
		case APOLLO:
			//TODO apollo
			System.out.println("Apollo not developted");
			break;
		case ZEROMQ:
			this.messageAdaptor = new ZeroMQMessageAdaptor(this, arbiMessageQueue, ltmMessageQueue);
//			this.ltmAdaptor = new ZeroMQLTMAdaptor(brokerURL, "tcp://ltmServer", ltmMessageQueue);
			break;
		default:
			System.out.println("undefined broker type : " + brokerType.toString());
			break;
		}

		System.out.println("broker type : " + brokerType.toString());
		System.out.println("broker url : " + brokerURL);
		
		messageAdaptor.initialize(brokerURL);
		Thread t1 = new Thread(this.agentMessageService);
		Thread t2 = new Thread(this.ltmMessageService);
		t1.start();
		t2.start();
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
