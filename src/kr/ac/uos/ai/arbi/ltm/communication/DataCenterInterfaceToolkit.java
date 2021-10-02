package kr.ac.uos.ai.arbi.ltm.communication;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

import kr.ac.uos.ai.arbi.ltm.LTMMessageAction;
import kr.ac.uos.ai.arbi.agent.datastream.DataStreamToolkit;
import kr.ac.uos.ai.arbi.ltm.DataSource;
import kr.ac.uos.ai.arbi.ltm.communication.activemq.ActiveMQAdaptor;
import kr.ac.uos.ai.arbi.ltm.communication.message.LTMMessage;
import kr.ac.uos.ai.arbi.ltm.communication.task.DispatchOnNotifyTask;
import kr.ac.uos.ai.arbi.ltm.communication.zeromq.ZeroMQLTMAdaptor;
import kr.ac.uos.ai.arbi.utility.DebugUtilities;

public class DataCenterInterfaceToolkit extends Thread {
	private final int nThread = 5;
	
	private final LTMMessageQueue queue;
	private ExecutorService messageThreadPool;
	private final DataSource dataSource;
	private final LinkedBlockingQueue<LTMMessage> waitingResult;
	private final LTMMessageAdaptor adaptor;

	private final LTMMessageFactory factory;

	public DataCenterInterfaceToolkit(String brokerURL, String dataSourceURI, DataSource dataSource, int brokerType) {

		this.factory = LTMMessageFactory.getInstance();
		this.dataSource = dataSource;
		this.queue = new LTMMessageQueue();
		if (brokerType == 2) {
			this.adaptor = new ZeroMQLTMAdaptor(brokerURL, dataSourceURI, queue);
		} else {
			this.adaptor = new ActiveMQAdaptor(brokerURL, dataSourceURI, queue);
		}
		this.waitingResult = new LinkedBlockingQueue<LTMMessage>();
		this.messageThreadPool = Executors.newFixedThreadPool(nThread);
		this.start();
	}

	public void run() {
		while (dataSource.isRunning()) {
			LTMMessage message = queue.blockingDequeue(null, 500);
			if (message != null) {
				dispatch(message);
			}
		}
	}
	
	public void close() {
		this.adaptor.close();
		
	}

	private void dispatch(LTMMessage message) {
		Runnable task;
		LTMMessageAction action = message.getAction();
		switch(action) {
		case Notify:
			task = new DispatchOnNotifyTask(this, message);
			break;
		default:
			task = null;
			DebugUtilities.addException("debugFile.txt", "ghost msg : " + message.getContent() + " action: " + message.getAction() + "client : " + message.getClient());
			break;
		}
		
		if(task != null) {
			messageThreadPool.execute(task);
		}
		else {
			dispatchResponse(message);
		}
	}
		
	private void dispatchResponse(LTMMessage message) {
		LTMMessage responsedMessage = null;
		for (LTMMessage ltmMessage : waitingResult) {
			if (ltmMessage.getConversationID().equals(message.getConversationID())) {
				responsedMessage = ltmMessage;
			}
		}
		if(responsedMessage != null) {
			responsedMessage.setResponse(message);
			waitingResult.remove(responsedMessage);
		}
		else {
			DebugUtilities.addException("debugFile.txt", "ghost msg : " + message.getContent() + " action: " + message.getAction() + "client : " + message.getClient());
		}
	}
	
	public void onNotify(String content) {
		dataSource.onNotify(content);
	}

	public void assertFact(String uri, String fact) {
		LTMMessage message = factory.newAssertFactMessage(uri, fact);
		waitingResult.add(message);
		adaptor.send(message);
		
	}

	public String retractFact(String uri, String fact) {
		LTMMessage message = factory.newRetractFactMessage(uri, fact);
		waitingResult.add(message);
		adaptor.send(message);
		return message.getResponse();
	}

	public String retrieveFact(String uri, String fact) {
		LTMMessage message = factory.newRetrieveFactMessage(uri, fact);
		waitingResult.add(message);
		adaptor.send(message);
		return message.getResponse();

	}

	public void updateFact(String uri, String fact) {
		LTMMessage message = factory.newUpdateFactMessage(uri, fact);
		waitingResult.add(message);
		adaptor.send(message);
	}

	public String match(String uri, String fact) {
		LTMMessage message = factory.newMatchMessage(uri, fact);
		waitingResult.add(message);
		adaptor.send(message);
		return message.getResponse();

	}

	public String subscribe(String uri, String rule) {
		LTMMessage message = factory.newSubscribeMessage(uri, rule);
		waitingResult.add(message);
		adaptor.send(message);
		return message.getResponse();
	}

	public void unsubscribe(String uri, String subID) {
		LTMMessage message = factory.newUnsubscribeMessage(uri, subID);
		waitingResult.add(message);
		adaptor.send(message);

	}

	public DataStreamToolkit registerStream(String uri, String rule) {
		LTMMessage message = factory.newRequestStreamMessage(uri, rule);
		adaptor.send(message);
		return null;
	}

	public String getLastModifiedTime(String uri, String fact) {
		LTMMessage message = factory.newGetLastModifiedTimeMessage(uri, fact);
		waitingResult.add(message);
		adaptor.send(message);
		
		
		
		return message.getResponse();
	}

}
