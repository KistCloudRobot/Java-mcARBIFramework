package kr.ac.uos.ai.arbi.framework;

import kr.ac.uos.ai.arbi.BrokerType;
import kr.ac.uos.ai.arbi.framework.broker.ActiveMQBroker;
import kr.ac.uos.ai.arbi.framework.broker.ApolloBroker;
import kr.ac.uos.ai.arbi.framework.broker.Broker;
import kr.ac.uos.ai.arbi.framework.center.InMemoryLTMService;
import kr.ac.uos.ai.arbi.framework.center.LTMMessageProcessor;
import kr.ac.uos.ai.arbi.framework.center.LTMServiceInterface;
import kr.ac.uos.ai.arbi.framework.center.RedisLTMService;
import kr.ac.uos.ai.arbi.framework.server.MessageService;

public class ArbiFrameworkServer {
	public static final String 	URL = "arbi.server";
	private MessageService		messageService;
	private LTMServiceInterface	ltmService;
	private Broker 				broker;
		
	public ArbiFrameworkServer(BrokerType brokerType, String host, int port) {
		switch(brokerType) {
		case ACTIVEMQ:
			this.broker = new ActiveMQBroker(host, port);
			break;
		case APOLLO:
			this.broker = new ApolloBroker(host, port);
			break;
		case ZEROMQ:
			break;
		default:
			System.out.println("undefined broker type : " + brokerType.toString());
			break;
		}
		
		ltmService = new InMemoryLTMService();
		LTMMessageProcessor msgProcessor = new LTMMessageProcessor(ltmService);
		
		messageService = new MessageService(msgProcessor, brokerType, host, port);
		msgProcessor.setMessageService(messageService);
	}
	
	public void start() {
		if(broker != null) {
			broker.start();
		}
		messageService.start();
		System.out.println("Server start!");
	}

}
