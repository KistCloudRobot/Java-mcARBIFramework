package kr.ac.uos.ai.arbi.interaction;

import java.util.HashMap;
import java.util.Map;

import kr.ac.uos.ai.arbi.BrokerType;
import kr.ac.uos.ai.arbi.interaction.adaptor.ActiveMQAdaptor;
import kr.ac.uos.ai.arbi.interaction.adaptor.InteractionMessageAdaptor;
import kr.ac.uos.ai.arbi.interaction.adaptor.SocketAdaptor;
import kr.ac.uos.ai.arbi.interaction.adaptor.ZeroMQAdaptor;


public class MonitorMessageToolkit extends Thread {

	private final int nThread = 5;
	private MonitorMessageQueue queue;
	private HashMap<String, InteractionMessageAdaptor> adaptorMap;
	private InteractionMessageAdaptor activeMQAdaptor;
	private InteractionMessageAdaptor socketAdaptor;
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
		
		if(InteractionManagerBrokerConfiguration.getActiveMQBrokerHost() != null) {
			this.activeMQAdaptor = new ActiveMQAdaptor(queue);
			this.adaptorMap.put("ActiveMQ", activeMQAdaptor);
		}
		if(InteractionManagerBrokerConfiguration.getZeroMQBrokerHost() != null) {
			this.zeroMQAdaptor = new ZeroMQAdaptor( queue);
			this.adaptorMap.put("ZeroMQ", zeroMQAdaptor);
		}
		
		if(InteractionManagerBrokerConfiguration.getSocketBrokerHost() != null) {
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
//				System.out.println("message : " + message);
				interactionManager.messageRecieved(message);
			}
		}
	}
	
	public void sendMessage(String monitorID, String protocol, String message) {
		if(adaptorMap.containsKey(protocol))
			adaptorMap.get(protocol).send(monitorID, message);
	}
	
	public void sendStatus(String serverURL, String status, BrokerType brokerType) {
		if(brokerType == BrokerType.ZEROMQ) {
			zeroMQAdaptor.sendStatus(status);
		} 
		else if (brokerType == BrokerType.ACTIVEMQ){
			activeMQAdaptor.sendStatus(status);
		}
		
		
	}
}
