package kr.ac.uos.ai.arbi.framework.broker;

import java.util.UUID;

import org.apache.activemq.broker.BrokerService;

public class ActiveMQBroker implements Broker {
	private BrokerService				broker;
	
	public ActiveMQBroker() {
		broker = new BrokerService();
	}
	
	public void setURL(String url){
		try {
			String id = UUID.randomUUID().toString();
			broker.setBrokerName(id);
			broker.addConnector(url);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void start(){
		try {
			broker.start();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	
}
