package kr.ac.uos.ai.arbi.interaction;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class MonitorMessageQueue {
	private final Queue<String> _queue;
	private final Lock _lock;
	private final Condition _newMessageArrived;
	
	public MonitorMessageQueue() {
		_queue = new LinkedList<String>();
		_lock = new ReentrantLock();
		_newMessageArrived = _lock.newCondition();
	}
	
	public void enqueue(String message) {
		_lock.lock();
		try {
			_queue.add(message);
			_newMessageArrived.signalAll();
		} finally {
			_lock.unlock();
		}
		
	}
	
	public String dequeue() {
		_lock.lock();
		try {
			for (Iterator<String> iter = _queue.iterator(); iter.hasNext(); ) {
				String message = iter.next();
				iter.remove();
				return message;
				
			}
		} finally {
			_lock.unlock();
		}
		return null;
	}
	
	public String blockingDequeue(long millis) {
		_lock.lock();
		try {
			String message = dequeue();
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
				message = dequeue();
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
