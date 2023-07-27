package kr.ac.uos.ai.arbi.agent.communication;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.jms.JMSException;
import javax.jms.MapMessage;

import kr.ac.uos.ai.arbi.agent.ArbiAgentMessage;

public class ArbiMessageQueue {
	private final Queue<ArbiAgentMessage>			_queue;
	private final Lock						_lock;
	private final Condition					_newMessageArrived;
	
	public ArbiMessageQueue() {
		_queue 					= new LinkedList<ArbiAgentMessage>();
		_lock 					= new ReentrantLock();
		_newMessageArrived 		= _lock.newCondition();
	}
	
	public void enqueue(ArbiAgentMessage message) {
		_lock.lock();
		try {
			_queue.add(message);
			_newMessageArrived.signalAll();
		} finally {
			_lock.unlock();
		}
	}
	
	public ArbiAgentMessage dequeue(String id) {
		_lock.lock();
		try {
			if (id == null) {
				return (_queue.size() > 0) ? _queue.remove() : null;
			}
			for (Iterator<ArbiAgentMessage> iter = _queue.iterator(); iter.hasNext(); ) {
				ArbiAgentMessage message = iter.next();
				String correlationID = message.getConversationID();
				if (id.equals(correlationID)) {
					iter.remove();
					return message;
				}
			}
		} finally {
			_lock.unlock();
		}
		return null;
	}
	
	public ArbiAgentMessage blockingDequeue(String id, long millis) {
		_lock.lock();
		try {
			ArbiAgentMessage message = dequeue(id);
			long timeToWait = millis;
			while(message == null) {
				long startTime = System.currentTimeMillis();
				try {
					if (timeToWait == 0) {
						_newMessageArrived.await();
					} else {
						_newMessageArrived.await(timeToWait, TimeUnit.MILLISECONDS);
					}
				} catch(InterruptedException ignore) {}
				long elapsedTime = System.currentTimeMillis() - startTime;
				message = dequeue(id);
				if (millis != 0) {
					timeToWait -= elapsedTime;
					if (timeToWait <= 0) {
						break;
					}
				}
			}
			return message;
		} finally {
			_lock.unlock();
		}
	}
}
