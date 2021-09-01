package kr.ac.uos.ai.arbi.ltm.communication;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import kr.ac.uos.ai.arbi.ltm.communication.message.LTMMessage;

public class LTMMessageQueue {

	private final Queue<LTMMessage> _queue;
	private final Lock _lock;
	private final Condition _newMessageArrived;

	public LTMMessageQueue() {
		_queue = new LinkedList<LTMMessage>();
		_lock = new ReentrantLock();
		_newMessageArrived = _lock.newCondition();
	}

	public void enqueue(LTMMessage message) {
		System.out.println("before lock");
		_lock.lock();
		System.out.println("after lock");
		try {
			_queue.add(message);
			_newMessageArrived.signalAll();
		} finally {
			System.out.println("before unlock");
			_lock.unlock();
			System.out.println("after unlock");
		}
	}

	private LTMMessage dequeue(String id) {

		if (id == null) {
			return (_queue.size() > 0) ? _queue.remove() : null;
		}
		for (Iterator<LTMMessage> iter = _queue.iterator(); iter.hasNext();) {
			LTMMessage message = iter.next();
			String correlationID = message.getConversationID();
			if (id.equals(correlationID)) {
				iter.remove();
				return message;
			}
		}

		return null;
	}

	public LTMMessage blockingDequeue(String id, long millis) {
		_lock.lock();
		try {
			LTMMessage message = dequeue(id);
			long timeToWait = millis;
			while (message == null) {
				long startTime = System.currentTimeMillis();
				try {
					if (timeToWait == 0) {
						_newMessageArrived.await();
					} else {
						_newMessageArrived.await(timeToWait, TimeUnit.MILLISECONDS);
					}
				} catch (InterruptedException ignore) {
				}
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
