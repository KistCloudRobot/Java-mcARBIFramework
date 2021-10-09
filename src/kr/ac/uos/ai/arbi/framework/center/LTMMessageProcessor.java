package kr.ac.uos.ai.arbi.framework.center;

import java.util.HashMap;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;


import kr.ac.uos.ai.arbi.framework.center.command.*;
import kr.ac.uos.ai.arbi.framework.server.LTMMessageListener;
import kr.ac.uos.ai.arbi.framework.server.MessageService;
import kr.ac.uos.ai.arbi.ltm.LTMMessageAction;
import kr.ac.uos.ai.arbi.ltm.communication.LTMMessageFactory;
import kr.ac.uos.ai.arbi.ltm.communication.message.LTMMessage;
import kr.ac.uos.ai.arbi.model.rule.action.Action;

public class LTMMessageProcessor implements LTMMessageListener, LTMNotificationHandler{
	private static final HashMap<LTMMessageAction, LTMCommand> commandMap;

	private ArrayBlockingQueue<LTMMessage> messageQueue = null;

	private LTMServiceInterface ltmService;
	private MessageService msgService;
	
	static {
		commandMap = new HashMap<LTMMessageAction, LTMCommand>();
		commandMap.put(LTMMessageAction.AssertFact, new AssertFactCommand());
		commandMap.put(LTMMessageAction.RetractFact, new RetractFactCommand());
		commandMap.put(LTMMessageAction.RetrieveFact, new RetrieveFactCommand());
		commandMap.put(LTMMessageAction.UpdateFact, new UpdateFactCommand());
		commandMap.put(LTMMessageAction.Match, new MatchCommand());
		commandMap.put(LTMMessageAction.Subscribe, new SubscribeCommand());
		commandMap.put(LTMMessageAction.Unsubscribe, new UnsubscribeCommand());
		commandMap.put(LTMMessageAction.RequestStream, new RequestStreamCommand());
		commandMap.put(LTMMessageAction.ReleaseStream, new ReleaseStreamCommand());
		commandMap.put(LTMMessageAction.GetLastModifiedTime, new GetLastModifiedTimeCommand());
	}

	public LTMMessageProcessor(LTMServiceInterface service) {
		this.ltmService = service;
		service.addLTMNotificationHandler(this);


	}

	@Override
	public synchronized void messageRecieved(LTMMessage msg) {

		String result = commandMap.get(msg.getAction()).deploy(ltmService, msg.getClient(), msg.getContent());
		LTMMessageFactory factory = LTMMessageFactory.getInstance();
		LTMMessage resultMSG = factory.newMessage(msg.getClient(), LTMMessageAction.Result, result,	msg.getConversationID());
		msgService.send(resultMSG);

	}

	public void setMessageService(MessageService messageService) {
		this.msgService = messageService;
	}

	@Override
	public void notify(Action action) {

		LTMMessageFactory factory = LTMMessageFactory.getInstance();
		LTMMessage msg = factory.newNotifyMessage(action.getSubscriber(), action.toActionContent());

		msgService.notify(msg);

	}




}
