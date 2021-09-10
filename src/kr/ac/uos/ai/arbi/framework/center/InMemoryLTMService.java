package kr.ac.uos.ai.arbi.framework.center;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

public class InMemoryLTMService implements LTMServiceInterface{
	private ConcurrentHashMap<String,LinkedBlockingQueue<PredicateContainer>> ltmMap;
	
	public InMemoryLTMService() {
		ltmMap = new ConcurrentHashMap<String,LinkedBlockingQueue<PredicateContainer>>();
	}
	
	public LinkedBlockingQueue<PredicateContainer> newGLName(String glName) {
		LinkedBlockingQueue<PredicateContainer> queue = new LinkedBlockingQueue<PredicateContainer>();
		ltmMap.put(glName, queue);
		
		return queue;
	}
	
	@Override
	public void addLTMNotificationHandler(LTMNotificationHandler handler) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String match(String author, String fact) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String retrieveFact(String author, String fact) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String retractFact(String author, String fact) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String updateFact(String author, String fact) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String assertFact(String author, String string) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String subscribe(String author, String rule) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String unsubscribe(String author, String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String requestStream(String author, String string) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String releaseStream(String author, String string) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getLastModifiedTime(String author, String fact) {
		// TODO Auto-generated method stub
		return null;
	}

}
