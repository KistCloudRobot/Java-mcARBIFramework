package kr.ac.uos.ai.arbi.framework.center;

import static kr.ac.uos.ai.arbi.framework.center.RedisUtil.createContainer;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

import kr.ac.uos.ai.arbi.model.Binding;
import kr.ac.uos.ai.arbi.model.BindingFactory;
import kr.ac.uos.ai.arbi.model.rule.Rule;
import kr.ac.uos.ai.arbi.model.rule.RuleFactory;
import kr.ac.uos.ai.arbi.model.rule.action.Action;
import kr.ac.uos.ai.arbi.model.rule.condition.Condition;
import kr.ac.uos.ai.arbi.model.rule.condition.ConditionFactory;
import kr.ac.uos.ai.arbi.utility.Configuration;
import kr.ac.uos.ai.arbi.utility.DebugUtilities;

class LTMSubscriptionManager implements Runnable{
	private ConcurrentHashMap<String,LinkedBlockingQueue<Rule>> subscribedRulesByPredicateName;
	private ConcurrentHashMap<String, Rule> subscribedRuleByID;
	private boolean isNotificationAlive = true;
	private LinkedBlockingQueue<PredicateContainer> predicateReceived;
	private LTMNotificationHandler notificationHandler;
	private InMemoryLTMService ltmService;
	
	public LTMSubscriptionManager(InMemoryLTMService ltmService) {
		subscribedRuleByID = new ConcurrentHashMap<String, Rule>();
		subscribedRulesByPredicateName = new ConcurrentHashMap<String, LinkedBlockingQueue<Rule>>();
		predicateReceived = new LinkedBlockingQueue<PredicateContainer>();
		this.ltmService = ltmService;
	}
	
	public void addPredicate(PredicateContainer predicate) {
		predicateReceived.add(predicate);
	}
	
	public void run() {
		while(isNotificationAlive) {
			if (predicateReceived.isEmpty() == false) {
				PredicateContainer event = predicateReceived.poll();
				
				Condition c = ConditionFactory.newConditionFromGLString("(fact " + event.getPredicate().toString() +")");
				this.checkSubscribedRules(c);
			}
		}
	}
	private void checkSubscribedRules(Condition c) {
		String predicateName = c.getPredicateName();

		LinkedBlockingQueue<Rule> rules = subscribedRulesByPredicateName.get(predicateName);
		
		if (rules == null || rules.isEmpty()) {
			return;
		}
		
		boolean conditionSatisfied;

		for (Rule rule : rules) {
			Binding binding = null;
			conditionSatisfied = false;
			Condition checkedCondition = null;
			
			for(Condition condition : rule.getConditions()) {
				Binding checkResult = checkIfRelatedCondition(c, condition);
				if(checkResult != null) {
					checkedCondition = condition;
					binding = checkResult;
					conditionSatisfied = true;
					break;
				}
			}
			
			if(conditionSatisfied) {
				for(Condition condition : rule.getConditions()) {
					if(condition == checkedCondition) continue;
					
					Binding evaluateResult = evaluate(condition);
					if(evaluateResult != null) binding.copy(evaluateResult);
					else {
						conditionSatisfied = false;
						break;
					}
				}
			}
			
			if (conditionSatisfied) {
				for (Action action : rule.getActions()) {
					action.bind(binding);
					notificationHandler.notify(action);
				}
			}
		}

	}

	public boolean isNotificationAlive() {
		return isNotificationAlive;
	}

	public void setNotificationAlive(boolean isNotificationAlive) {
		this.isNotificationAlive = isNotificationAlive;
	}
	
	private Binding checkIfRelatedCondition(Condition receivedCondition, Condition ruleCondition) {
		PredicateContainer receivedPredicate = createContainer(null, receivedCondition.toString());
		PredicateContainer rulePredicate = createContainer(null, ruleCondition.toString());

		Binding binding = receivedPredicate.getPredicate().unify(rulePredicate.getPredicate(), null);

		return binding;
	}

	private Binding evaluate(Condition con) {
		PredicateContainer data = createContainer(null, con.toString());
		PredicateContainer queriedData;
		queriedData = this.ltmService.matchContainer(data);
		
		if (queriedData != null) {
			Binding b = data.getPredicate().unify(queriedData.getPredicate(), null);
			return b;
		}else {
			return null;	
		}
	}
	
	public void setNotificationHandler(LTMNotificationHandler handler) {
		this.notificationHandler = handler;		
	}

	public String subscribe(String author, String ruleString) {
		Rule rule = RuleFactory.newRuleFromRuleString(author, ruleString);
		String ruleID = "Subscribe:" + System.currentTimeMillis();
		subscribedRuleByID.put(ruleID, rule);
		
		for(int i = 0; i < rule.getConditions().length;i++) {
			Condition con = rule.getConditions()[i];
			LinkedBlockingQueue<Rule> ruleList = null;
			if(subscribedRulesByPredicateName.containsKey(con.getPredicateName()) == false) {
				ruleList = new LinkedBlockingQueue<Rule>();
				subscribedRulesByPredicateName.put(con.getPredicateName(),ruleList);
				ruleList.add(rule);
			}else {
				ruleList = subscribedRulesByPredicateName.get(con.getPredicateName());
				ruleList.add(rule);
			}
		}
		return ruleID;
	}

	public void unsubscribe(String author, String id) {
		Rule rule = subscribedRuleByID.get(id);
		subscribedRuleByID.remove(id);
		for(int i = 0; i < rule.getConditions().length;i++) {
			Condition con = rule.getConditions()[i];
			subscribedRulesByPredicateName.remove(con.getPredicateName());
		}
		
	}
}

