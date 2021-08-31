package kr.ac.uos.ai.arbi.agent.datastream;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class StreamDataQueue {
	private final Queue<String>			_queue;
	private final Lock						_lock;
	private final Condition					_newMessageArrived;
	
	public StreamDataQueue() {
		_queue 					= new LinkedList<String>();
		_lock 					= new ReentrantLock();
		_newMessageArrived 		= _lock.newCondition();
	}
	
	public void enqueue(String data) {
		_lock.lock();
		try {
			_queue.add(data);
			_newMessageArrived.signalAll();
		} finally {
			_lock.unlock();
		}
	}
	
	public String dequeue() {
		_lock.lock();
		try {
			for (Iterator<String> iter = _queue.iterator(); iter.hasNext(); ) {
				String data = iter.next();
				iter.remove();
				return data;
			}
		} finally {
			_lock.unlock();
		}
		return null;
	}
	
	public String blockingDequeue(long millis) {
		_lock.lock();
		try {
			String data = dequeue();
			long timeToWait = millis;
			while(data == null) {
				long startTime = System.currentTimeMillis();
				try {
					if (timeToWait == 0) {
						_newMessageArrived.await();
					} else {
						_newMessageArrived.await(timeToWait, TimeUnit.MILLISECONDS);
					}
				} catch(InterruptedException ignore) {}
				long elapsedTime = System.currentTimeMillis() - startTime;
				data = dequeue();
				if (millis != 0) {
					timeToWait -= elapsedTime;
					if (timeToWait <= 0) {
						break;
					}
				}
			}
			return data;
		} finally {
			_lock.unlock();
		}
	}


}
