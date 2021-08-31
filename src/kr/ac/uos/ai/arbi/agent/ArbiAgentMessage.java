package kr.ac.uos.ai.arbi.agent;

import java.util.UUID;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


public class ArbiAgentMessage {
	private final String					sender;
	private final String					receiver;
	private final AgentMessageAction		messageAction;
	private final String					content;
	
	private final String					conversationID;
	private final long						timestamp;
	
	private final ResponseLock				responceLock;
	private ArbiAgentMessage				response;
	
	private class ResponseLock {
		private final Lock						lock;
		private final Condition					responseArrived;
		public ResponseLock() {
			this.lock = new ReentrantLock();
			responseArrived = lock.newCondition();
		}
		private void lock(){
			this.lock.lock();
		}
		private void signal(){
			this.responseArrived.signalAll();
		}
		private void await() throws InterruptedException {
			this.responseArrived.await();
			
		}
		private void unlock() {
			this.lock.unlock();
			
		}
	}
	
	public ArbiAgentMessage(String sender, String receiver, AgentMessageAction action, String content) {
		this(sender, receiver, action, content, UUID.randomUUID().toString());
	}
	
	public ArbiAgentMessage(String sender, String receiver, AgentMessageAction action, String content, String conversationID) {
		this(sender, receiver, action, content, conversationID, System.currentTimeMillis());
 	}

	public ArbiAgentMessage(String sender, String receiver, AgentMessageAction action, String content, String conversationID, long timestamp) {
 		this.sender = sender;
		this.receiver = receiver;
		this.messageAction = action;
		this.content = content;
		this.conversationID = conversationID;
		this.timestamp = timestamp;
		switch(messageAction){
		case Query:
		case Request:
		case Subscribe:
		case RequestStream:
			responceLock = new ResponseLock();
			break;
		case Inform:
		case Notify:
		case Response:
		case Unsubscribe:
		case System:
		default:
			responceLock = null;
		}
	}
	
	public long getTimestamp() {
		return timestamp;
	}

	public String getConversationID() {
		return conversationID;
	}

	public AgentMessageAction getAction() {
		return messageAction;
	}

	public String getContent() {
		return content;
	}

	public String getReceiver() {
		return receiver;
	}

	public String getSender() {
		
		return sender;
	}

	public String getResponse() {
		responceLock.lock();
		try {
			while(response == null) {
				try {
					responceLock.await();
				} catch(InterruptedException ignore) {}
			}
			return response.getContent();
		} finally {
			responceLock.unlock();
		}
	}

	public void setResponse(ArbiAgentMessage response) {
		responceLock.lock();
		try {
			this.response = response;
			responceLock.signal();
		} finally {
			responceLock.unlock();
		}
	}
	
}
