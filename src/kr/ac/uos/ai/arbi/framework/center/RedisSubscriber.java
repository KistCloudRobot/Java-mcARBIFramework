package kr.ac.uos.ai.arbi.framework.center;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.eclipse.jetty.util.BlockingArrayQueue;

import kr.ac.uos.ai.arbi.model.Binding;
import kr.ac.uos.ai.arbi.model.BindingFactory;
import kr.ac.uos.ai.arbi.model.Expression;
import kr.ac.uos.ai.arbi.model.rule.Rule;
import kr.ac.uos.ai.arbi.model.rule.action.Action;
import kr.ac.uos.ai.arbi.model.rule.condition.Condition;
import kr.ac.uos.ai.arbi.model.rule.condition.ConditionFactory;
import kr.ac.uos.ai.arbi.utility.Configuration;
import kr.ac.uos.ai.arbi.utility.DebugUtilities;

import static kr.ac.uos.ai.arbi.framework.center.RedisUtil.*;

import com.lambdaworks.redis.RedisClient;
import com.lambdaworks.redis.pubsub.RedisPubSubListener;
import com.lambdaworks.redis.pubsub.StatefulRedisPubSubConnection;
import com.lambdaworks.redis.pubsub.api.async.RedisPubSubAsyncCommands;

public class RedisSubscriber implements RedisPubSubListener<String, String>, Runnable {
	private ConcurrentHashMap<String, List<Rule>> subscribedRulesByPredicateName;
	private ConcurrentHashMap<String, Rule> subscribedRuleByID;
	private LTMNotificationHandler handler;
	private RedisPubSubAsyncCommands<String, String> subsriptionCommand;
	private BlockingArrayQueue<String> messageQueue = new BlockingArrayQueue<String>();
	private ExecutorService messageThreadPool;

	public RedisSubscriber() {
		RedisClient queryClient = RedisClient.create("redis://127.0.0.1:6379/0");
		StatefulRedisPubSubConnection<String, String> psConnection = queryClient.connectPubSub();
		psConnection.addListener(this);

		this.messageThreadPool = Executors.newFixedThreadPool(5);
		this.subsriptionCommand = psConnection.async();
		this.subscribedRulesByPredicateName = new ConcurrentHashMap<String, List<Rule>>();
		this.subscribedRuleByID = new ConcurrentHashMap<String, Rule>();
		this.subsriptionCommand.subscribe(SubscribeChannel);

	}

	public void addNotificationHandler(LTMNotificationHandler handler) {
		this.handler = handler;
	}

	// [adru]-n:[*],1:[*],2:[*]

	class HandleSubscriptionWork implements Runnable {
		private Condition c;

		public HandleSubscriptionWork(Condition c) {
			this.c = c;

		}

		@Override
		public void run() {
			checkSubscribedRules(this.c);
		}
	}

	@Override
	public void psubscribed(String event, long arg1) {
	}

	private void checkSubscribedRules(Condition c) {
		String predicateName = c.getPredicateName();
		System.out.println("new condition : " + c.toString());
		if (Configuration.getLogAvailability() == true) {
			System.out.println(DebugUtilities.getDate() + "RedisSubscriber checkSubscribedRules : predicateName : "
					+ predicateName);

		}
		List<Rule> rules = subscribedRulesByPredicateName.get(predicateName);
		if (rules == null || rules.isEmpty()) {

			return;

		}
		boolean conditionSatisfied;
		PredicateContainer container = createContainer(null, c.toString());
		
		
		for (Rule rule : rules) {
			Binding b = BindingFactory.newBinding();
			
			conditionSatisfied = true;


			

			for (Condition con : rule.getConditions()) {
				Binding tempBind = BindingFactory.newBinding();
				
				boolean checkResult = checkIfRelatedCondition(c, con);
				

				if ((tempBind = evaluate(con,null)) != null && checkResult == true) {
					b.copy(tempBind);
				} 
				
				Binding initBind = BindingFactory.newBinding();
				PredicateContainer ruleContainer = createContainer(null, con.toString());
				initBind = ruleContainer.getPredicate().unify(container.getPredicate(),null);			
				if(initBind != null && checkResult == true) {
					b.copy(initBind);
				}
				System.out.println("b : " + b.toString());
				System.out.println("init : " + initBind.toString());
				if(initBind == null && tempBind == null)
				{
					conditionSatisfied = false;
					break;
				}
			}
			
			if (conditionSatisfied) {
				
				for (Action a : rule.getActions()) {
			
					a.bind(b);
					handler.notify(a);
				}
			}
		}

	}

	private boolean checkIfRelatedCondition(Condition receivedCondition, Condition ruleCondition) {
		PredicateContainer receivedPredicate = createContainer(null, receivedCondition.toString());
		PredicateContainer rulePredicate = createContainer(null, ruleCondition.toString());

		Binding b = receivedPredicate.getPredicate().unify(rulePredicate.getPredicate(), null);

		boolean result = false;
		if (b != null) {
			result = true;
		}
		return result;
	}

	private Binding evaluate(Condition con,Binding b) {
		PredicateContainer data = createContainer(null, con.toString());
		PredicateContainer queriedData;

		try {
			queriedData = RedisUtil.queryMatchData(data);
		} catch (RedisKeyNotFoundException e) {
			return null;
		}

		if (queriedData != null) {
			b = data.getPredicate().unify(queriedData.getPredicate(), b);
			return b;
		}
		return null;
	}

	public String addRule(Rule r) {

		Condition[] condition = r.getConditions();
		String subID = "Subscribe:" + System.nanoTime();
		for (int i = 0; i < condition.length; i++) {
			if (subscribedRulesByPredicateName.get(condition[i].getPredicateName()) == null) {

				subscribedRulesByPredicateName.put(condition[i].getPredicateName(), new ArrayList<Rule>());
			}
			subscribedRulesByPredicateName.get(condition[i].getPredicateName()).add(r);
			String pattern = makePatternString(condition[i]);
			subsriptionCommand.psubscribe(pattern);
		}
		subscribedRuleByID.put(subID, r);

		return subID;

	}

	private String makePatternString(Condition condition) {
		StringBuilder sb = new StringBuilder();
//		sb.append("(fact (").append(condition.getPredicateName());
//		for (Expression e : condition.getExpressions()) {
//			sb.append(" ");
//			if (e.isVariable()) {
//				sb.append("*");
//			} else {
//				sb.append(e.toString());
//			}
//		}
		sb.append("))");
		return sb.toString();
	}

	public void removeRule(String subID) {

		Rule r = subscribedRuleByID.remove(subID);
		Condition[] conditions = r.getConditions();
		for (Condition condition : conditions) {
			subscribedRulesByPredicateName.get(condition.getPredicateName()).remove(r);
			// String pattern = makePatternString(condition);
			// subsriptionCommand.punsubscribe(pattern);
		}

	}

	@Override
	public void message(String arg0, String event) {
		// System.out.println("////////////////new notification///////////////");
		PredicateContainer container = null;

		String conditionString = "(fact " + event + ")";
		messageQueue.add(conditionString);

		// System.out.println("////////////notification finished/////////////// ");

	}

	@Override
	public void message(String pattern, String channel, String event) {

	}

	@Override
	public void punsubscribed(String arg0, long arg1) {

	}

	@Override
	public void subscribed(String arg0, long arg1) {
	}

	@Override
	public void unsubscribed(String arg0, long arg1) {
	}

	@Override
	public void run() {
		while (true) {
			if (messageQueue.isEmpty() == false) {
				String event = messageQueue.poll();

				Condition c = ConditionFactory.newConditionFromGLString(event);
				
				HandleSubscriptionWork hsw = new HandleSubscriptionWork(c);
			
				messageThreadPool.execute(hsw);
			}
		}
	}

}
