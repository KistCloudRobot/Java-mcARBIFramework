package kr.ac.uos.ai.arbi.framework;

import kr.ac.uos.ai.arbi.framework.center.InMemoryLTMService;
import kr.ac.uos.ai.arbi.framework.center.LTMMessageProcessor;
import kr.ac.uos.ai.arbi.framework.center.LTMServiceInterface;
import kr.ac.uos.ai.arbi.framework.center.RedisLTMService;
import kr.ac.uos.ai.arbi.framework.server.MessageService;

public class ArbiFrameworkServer {
	public static final String URL = "arbi.server";
	private MessageService				messageService;
	private LTMServiceInterface					ltmService;
	private int brokerType;
	private String brokerName;
	
	public ArbiFrameworkServer(int brokerType,String brokerName) {
		this.brokerType = brokerType;
		ltmService = new InMemoryLTMService();
		
		
		LTMMessageProcessor msgProcessor = new LTMMessageProcessor(ltmService);
		this.brokerName = brokerName;
		
		messageService = new MessageService(msgProcessor, brokerType);
		msgProcessor.setMessageService(messageService);
	}
	
	public void start(String serverURL,String brokerURL) {
		messageService.initialize(serverURL,brokerURL,this.brokerName);
	}

}
