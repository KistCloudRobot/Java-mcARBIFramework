package kr.ac.uos.ai.arbi.interaction;

import java.util.HashMap;
import java.util.Map;

import kr.ac.uos.ai.arbi.interaction.adaptor.ActiveMQAdaptor;
import kr.ac.uos.ai.arbi.interaction.adaptor.ActiveMQStompAdaptor;
import kr.ac.uos.ai.arbi.interaction.adaptor.InteractionMessageAdaptor;
import kr.ac.uos.ai.arbi.interaction.adaptor.SocketAdaptor;
import kr.ac.uos.ai.arbi.interaction.adaptor.ZeroMQAdaptor;


public class MonitorMessageToolkit extends Thread {

	private final int nThread = 5;
	private MonitorMessageQueue queue;
	private HashMap<String, InteractionMessageAdaptor> adaptorMap;
	private InteractionMessageAdaptor activeMQAdaptor;
	private InteractionMessageAdaptor socketAdaptor;
	private InteractionMessageAdaptor activeMQStompAdaptor;
	private InteractionMessageAdaptor zeroMQAdaptor;
	
	private InteractionManager interactionManager;
	
	public MonitorMessageToolkit(InteractionManager interactionManager) {
		this.interactionManager = interactionManager;
		this.queue = new MonitorMessageQueue();
		this.adaptorMap = new HashMap<String, InteractionMessageAdaptor>();
		initAdaptors();
		this.start();
	}
	
	private void initAdaptors() {
		this.activeMQAdaptor = null;
		this.zeroMQAdaptor = null;
		this.socketAdaptor = null;
		this.activeMQStompAdaptor = null;
		
		if(InteractionManagerBrokerConfiguration.ApolloBroker != null) {
			this.activeMQAdaptor = new ActiveMQAdaptor(queue);
			this.adaptorMap.put("ActiveMQ", activeMQAdaptor);
		}
		if(InteractionManagerBrokerConfiguration.StompBroker != null) {
			this.activeMQStompAdaptor = new ActiveMQStompAdaptor(queue);
			this.adaptorMap.put("Stomp", activeMQStompAdaptor);
		}
		if(InteractionManagerBrokerConfiguration.ZeroMQBroker != null) {
			this.zeroMQAdaptor = new ZeroMQAdaptor( queue);
			this.adaptorMap.put("ZeroMQ", zeroMQAdaptor);
		}
		
		if(InteractionManagerBrokerConfiguration.SocketBroker != null) {
			this.socketAdaptor = new SocketAdaptor(queue);
			this.adaptorMap.put("Socket", socketAdaptor);
		}
	}
	
	public void stopThread() {
		for(Map.Entry<String, InteractionMessageAdaptor> elem : adaptorMap.entrySet()) {
			elem.getValue().close();
		}
	}
	
	public void run() {
		while(true){
			String message = queue.blockingDequeue(500);
			if(message != null) {
				interactionManager.messageRecieved(message);
			}
		}
	}
	
	public void sendMessage(String monitorID, String protocol, String message) {
		System.out.println("protocol?" + protocol);
		if(adaptorMap.containsKey(protocol))
			adaptorMap.get(protocol).send(monitorID, message);
	}
	
	public void sendStatus(String serverURL, String status, int brokerType) {
		if(brokerType == 2) {
			zeroMQAdaptor.sendStatus(status);
		} else {
		//	activeMQAdaptor.sendStatus(status);
		}
		
		
	}
}
