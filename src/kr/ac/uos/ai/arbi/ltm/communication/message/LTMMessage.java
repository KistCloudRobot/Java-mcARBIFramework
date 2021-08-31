package kr.ac.uos.ai.arbi.ltm.communication.message;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import kr.ac.uos.ai.arbi.ltm.LTMMessageAction;

public abstract class LTMMessage {
	private final String					client;
	private final LTMMessageAction			messageAction;
	private final String					content;
	
	private final String					conversationID;
	
	private final ResultLock				resultLock;
	private LTMMessage						result;
	
	private class ResultLock {
		private final Lock						lock;
		private final Condition					responseArrived;
		public ResultLock() {
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
	

	public LTMMessage(String client, LTMMessageAction action, String content, String conversationID) {
		this.client = client;
		this.messageAction = action;
		this.content = content;
		this.conversationID = conversationID;
		this.resultLock = new ResultLock();
	}

	public String getConversationID() {
		return conversationID;
	}

	public LTMMessageAction getAction() {
		return messageAction;
	}

	public String getContent() {
		return content;
	}


	public String getClient() {
		return client;
	}

	public String getResponse() {
		resultLock.lock();
		try {
			while(result == null) {
				try {
					resultLock.await();
				} catch(InterruptedException ignore) {}
			}
			return result.getContent();
		} finally {
			resultLock.unlock();
		}
	}

	public void setResponse(LTMMessage response) {
		resultLock.lock();
		try {
			this.result = response;
			resultLock.signal();
		} finally {
			resultLock.unlock();
		}
	}
}
