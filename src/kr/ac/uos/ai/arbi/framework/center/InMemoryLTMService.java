package kr.ac.uos.ai.arbi.framework.center;

import static kr.ac.uos.ai.arbi.framework.center.RedisUtil.createContainer;
import static kr.ac.uos.ai.arbi.framework.center.RedisUtil.queryMatchData;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

import kr.ac.uos.ai.arbi.framework.center.RedisSubscriber.HandleSubscriptionWork;
import kr.ac.uos.ai.arbi.model.Binding;
import kr.ac.uos.ai.arbi.model.BindingFactory;
import kr.ac.uos.ai.arbi.model.GeneralizedList;
import kr.ac.uos.ai.arbi.model.rule.Rule;
import kr.ac.uos.ai.arbi.model.rule.RuleFactory;
import kr.ac.uos.ai.arbi.model.rule.action.Action;
import kr.ac.uos.ai.arbi.model.rule.condition.Condition;
import kr.ac.uos.ai.arbi.model.rule.condition.ConditionFactory;
import kr.ac.uos.ai.arbi.utility.Configuration;
import kr.ac.uos.ai.arbi.utility.DebugUtilities;

public class InMemoryLTMService implements LTMServiceInterface{
	private ConcurrentHashMap<String,LinkedBlockingQueue<PredicateContainer>> ltmMap;
	private ConcurrentHashMap<String, PredicateContainer> cashMap;
	private LTMSubscriptionManager subscriptionManager;
	private Thread t;
	
	public InMemoryLTMService() {
		ltmMap = new ConcurrentHashMap<String,LinkedBlockingQueue<PredicateContainer>>();
		cashMap = new ConcurrentHashMap<String, PredicateContainer>();
		subscriptionManager = new LTMSubscriptionManager(this);
		t = new Thread(subscriptionManager);
		t.setName("LTMNotificationThread");
		
	}
	
	
	
	public LinkedBlockingQueue<PredicateContainer> newGLName(String glName) {
		LinkedBlockingQueue<PredicateContainer> queue = new LinkedBlockingQueue<PredicateContainer>();
		
		ltmMap.put(glName, queue);
		return queue;
	}
	
	
	
	@Override
	public void addLTMNotificationHandler(LTMNotificationHandler handler) {
		subscriptionManager.setNotificationHandler(handler);
		t.start();
	}
	
	public PredicateContainer matchContainer(PredicateContainer container) {
		String predicateName = container.getPredicate().getName();
		if(cashMap.containsKey(predicateName) == true) {
			PredicateContainer cashContainer = cashMap.get(predicateName);
			Binding binding = BindingFactory.newBinding();
			boolean checkResult = unify(container, cashContainer,binding);
			if(checkResult == true) {
				return cashContainer;
			}
		}
		if(ltmMap.containsKey(predicateName) == true) {
			LinkedBlockingQueue<PredicateContainer> queue = ltmMap.get(predicateName);
			for(PredicateContainer ltmData : queue) {
				Binding binding = BindingFactory.newBinding();
				boolean checkResult = unify(container, ltmData,binding);
				if(checkResult == true) {
					return ltmData;
				}
			}
		}
		
		
		return null;
	}
	
	@Override
	public String match(String author, String fact) {
		PredicateContainer container = new PredicateContainer(author, System.currentTimeMillis(), fact);
		PredicateContainer foundContainer = this.matchContainer(container);
		
		if(foundContainer != null) {
			Binding b = foundContainer.getPredicate().unify(container.getPredicate(),null);
			return this.buildBindingString(b);
			
		}

		return "(error)";
	}
	
	public String buildBindingString(Binding binding) {
		StringBuilder sb = new StringBuilder();
		sb.append("(bind");
		for (String var : binding.getBoundedVariableNames()) {
			sb.append(" (").append(var).append(" ").append(binding.retrieve(var))
					.append(")");
		}
		sb.append(")");
		return sb.toString();
	}
	
	public boolean unify(PredicateContainer first, PredicateContainer second, Binding binding) {
		boolean result = true;
		binding = first.getPredicate().unify(second.getPredicate(),binding);
		
		if(binding != null) {
			result = true;
		}else {
			result = false;
		}
		
		return result;
	}
	
	
	@Override
	public String retrieveFact(String author, String fact) {
		PredicateContainer container = new PredicateContainer(author, System.currentTimeMillis(), fact);
		PredicateContainer foundContainer = this.matchContainer(container);
		
		if(foundContainer != null) {
			return foundContainer.getPredicate().toString();
			
		}

		return "(error)";
	}

	@Override
	public String retractFact(String author, String fact) {
		PredicateContainer container = new PredicateContainer(author, System.currentTimeMillis(), fact);
		PredicateContainer foundContainer = this.matchContainer(container);
		
		if(foundContainer != null) {
			String retractName = foundContainer.getPredicate().getName();
			if(cashMap.get(retractName).getCreateTime() == foundContainer.getCreateTime()) {
				cashMap.remove(retractName);
			}
			this.ltmMap.get(retractName).remove(foundContainer);			
			
			return foundContainer.getPredicate().toString();			
		}

		return "(error)";
	}

	@Override
	public String updateFact(String author, String fact) {
		
		PredicateContainer container = new PredicateContainer(author,System.currentTimeMillis(),fact);
		GeneralizedList gl1 = container.getPredicate().getExpression(0).asGeneralizedList();
		GeneralizedList gl2 = container.getPredicate().getExpression(1).asGeneralizedList();
		
		String result = retractFact(author,gl1.toString());
		String result2 = assertFact(author,gl2.toString());
		
		StringBuilder finalResult = new StringBuilder();

		return "(ok)";

	}

	@Override
	public String assertFact(String author, String string) {
		PredicateContainer container = new PredicateContainer(author, System.currentTimeMillis(), string);
		LinkedBlockingQueue<PredicateContainer> queue = null;
		if(ltmMap.containsKey(container.getPredicate().getName()) == false) {
			queue = this.newGLName(container.getPredicate().getName());
		}else {
			queue = ltmMap.get(container.getPredicate().getName());
		}
		
		queue.add(container);
		cashMap.put(container.getPredicate().getName(), container);
		subscriptionManager.addPredicate(container);
		
		return "(ok)";
	}
	
	@Override
	
	public String subscribe(String author, String ruleString) {
		String ruleID = this.subscriptionManager.subscribe(author,ruleString);
		

		return ruleID;
	}
	
	@Override
	public String unsubscribe(String author, String id) {
		this.subscriptionManager.unsubscribe(author,id);
		
		return "(ok)";
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
		PredicateContainer p = createContainer(author, fact);
		PredicateContainer queried;
		try {
			queried = queryMatchData(p);
			if (queried != null)
				return Long.toString(queried.getCreateTime());
			else
				return "(fail)";
		} catch (RedisKeyNotFoundException e) {
			return "(fail)";
		}
	}
	
	

	
}
