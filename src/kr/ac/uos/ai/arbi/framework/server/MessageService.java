package kr.ac.uos.ai.arbi.framework.server;

import kr.ac.uos.ai.arbi.agent.ArbiAgentMessage;
import kr.ac.uos.ai.arbi.agent.communication.ArbiMessageQueue;
import kr.ac.uos.ai.arbi.ltm.communication.LTMMessageQueue;
import kr.ac.uos.ai.arbi.ltm.communication.activemq.ActiveMQAdaptor;
import kr.ac.uos.ai.arbi.ltm.communication.message.LTMMessage;
import kr.ac.uos.ai.arbi.ltm.communication.zeromq.ZeroMQLTMAdaptor;
import kr.ac.uos.ai.arbi.ltm.LTMMessageAction;

public class MessageService{
	private MessageDeliverAdaptor deliverAdaptor;
	private kr.ac.uos.ai.arbi.ltm.communication.LTMMessageAdaptor ltmAdaptor;
	private LTMMessageListener ltmListener;
	private boolean interactionManagerStatus;
	private int brokerType;
	private LTMMessageQueue ltmMessageQueue;
	private LTMMessageAdaptor ltmMessageAdaptor;
	private ArbiMessageQueue arbiMessageQueue;
	private ArbiAgentMessageService agentMessageService;
	private LTMMessageService ltmMessageService;
	private boolean isRunning = true;
	
	public MessageService(LTMMessageListener listener, int brokerType) {
		this.ltmListener = listener;
		this.brokerType = brokerType;
		ltmAdaptor = null;
		interactionManagerStatus = true;
		arbiMessageQueue = new ArbiMessageQueue();
		ltmMessageQueue = new LTMMessageQueue();	
		
		agentMessageService = new ArbiAgentMessageService(arbiMessageQueue);
		ltmMessageService = new LTMMessageService(ltmMessageQueue);
		
	}

	public void agentMessageReceived(ArbiAgentMessage agentMessage) {
//		System.out.println("[Agent Message]\t<" + agentMessage.getAction().toString() + ">\t" + agentMessage.getSender()
//				+ " --> " + agentMessage.getReceiver() + " : " + agentMessage.getContent());
		deliverAdaptor.deliver(agentMessage);
		
		if (interactionManagerStatus) {
			deliverAdaptor.deliverToMonitor(agentMessage);
		}

	}

	public void send(LTMMessage message) {
//		System.out.println("[LTM Message]\t<" + message.getAction().toString() + ">\t" + message.getClient() + " : "
//				+ message.getContent());
		
		ltmMessageAdaptor.send(message);
		
		if (interactionManagerStatus) {
			deliverAdaptor.deliverToMonitor(message);
		}

	}
	
	public void notify(LTMMessage message) {
		ltmAdaptor.send(message);
	}
	
	public void ltmMessageReceived(LTMMessage ltmMessage) {
//		System.out.println("[LTM Message]\t<" + ltmMessage.getAction().toString() + ">\t" + ltmMessage.getClient()
//				+ " : " + ltmMessage.getContent());

		if (interactionManagerStatus) {
			deliverAdaptor.deliverToMonitor(ltmMessage);
		}
		if(ltmMessage.getAction() == LTMMessageAction.Notify) {
			ltmMessageAdaptor.send(ltmMessage);
		}else {
			ltmListener.messageRecieved(ltmMessage);
		}
			
	}

	public void messageRecieved(String status) {
		if (status.equals("ON"))
			interactionManagerStatus = true;
		else if (status.equals("OFF"))
			interactionManagerStatus = false;

	}

	public void initialize(String serverURL, String brokerURL, String brokerName) {
	
		if(brokerType == 2) {
			this.deliverAdaptor = new ZeroMQServerMessageAdaptor(this,arbiMessageQueue,ltmMessageQueue,brokerName);
			this.ltmAdaptor = new ZeroMQLTMAdaptor(brokerURL, "tcp://ltmServer", ltmMessageQueue);
		}else if (brokerType == 4){
			this.deliverAdaptor = new McArbiServerAdaptor(this,arbiMessageQueue,ltmMessageQueue);	
			this.ltmAdaptor = new ZeroMQLTMAdaptor(brokerURL, "tcp://ltmServer",ltmMessageQueue);
		}else {
			this.deliverAdaptor = new ActiveMQMessageAdaptor(this);
			this.ltmAdaptor = new ActiveMQAdaptor(brokerURL, "tcp://ltmServer", ltmMessageQueue);
		}
		deliverAdaptor.initialize(serverURL,brokerURL);
		ltmMessageAdaptor = (LTMMessageAdaptor)deliverAdaptor;
		Thread t1 = new Thread(this.agentMessageService);
		Thread t2 = new Thread(this.ltmMessageService);
		t1.start();
		t2.start();
	
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
				ArbiAgentMessage msg = queue.blockingDequeue(null, 10);
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
