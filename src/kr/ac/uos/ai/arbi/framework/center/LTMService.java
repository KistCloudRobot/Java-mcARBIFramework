package kr.ac.uos.ai.arbi.framework.center;

import static kr.ac.uos.ai.arbi.framework.center.RedisUtil.*;
import kr.ac.uos.ai.arbi.model.Binding;
import kr.ac.uos.ai.arbi.model.GLFactory;
import kr.ac.uos.ai.arbi.model.GeneralizedList;
import kr.ac.uos.ai.arbi.model.parser.ParseException;
import kr.ac.uos.ai.arbi.model.rule.Rule;
import kr.ac.uos.ai.arbi.model.rule.RuleFactory;

import com.lambdaworks.redis.RedisClient;
import com.lambdaworks.redis.api.StatefulRedisConnection;
import com.lambdaworks.redis.api.sync.RedisCommands;
import com.lambdaworks.redis.pubsub.StatefulRedisPubSubConnection;
import com.lambdaworks.redis.pubsub.api.async.RedisPubSubAsyncCommands;

public class LTMService {
	private RedisClient queryClient;
	private RedisCommands<String, String> command;
	private RedisPubSubAsyncCommands<String, String> psCommand;
	private RedisSubscriber subscriber;

	public LTMService() {
		queryClient = RedisClient.create("redis://127.0.0.1:6379/0");
		StatefulRedisConnection<String, String> connection = queryClient.connect();
		StatefulRedisPubSubConnection<String, String> psConnection = queryClient.connectPubSub();
		psCommand = psConnection.async();
		command = connection.sync();
		command.flushall();
		RedisUtil.setCommand(command);
		subscriber = new RedisSubscriber();
		
		Thread t = new Thread(subscriber);
		t.start();
	}

	public void addLTMNotificationHandler(LTMNotificationHandler handler) {
		subscriber.addNotificationHandler(handler);
	}

	public String match(String author, String fact) {
		PredicateContainer p = createContainer(author, fact);
		PredicateContainer queried;
		try {
			queried = queryMatchData(p);
		} catch (RedisKeyNotFoundException e) {
			return "(fail)";
		}
		if (queried != null) {
			StringBuilder sb = new StringBuilder();
			sb.append("(bind");
			Binding b = p.getPredicate().unify(queried.getPredicate(), null);
			for (String var : b.getBoundedVariableNames()) {
				sb.append(" (").append(var).append(" ").append(b.retrieve(var))
						.append(")");
			}
			sb.append(")");
			return sb.toString();
		} else {
			return "(fail)";
		}
	}

	public String retrieveFact(String author, String fact) {
		PredicateContainer p = createContainer(author, fact);
		PredicateContainer queried;
		try {
			queried = queryMatchData(p);
		} catch (RedisKeyNotFoundException e) {
			return "(fail)";
		}
		if(queried!= null)
			return queried.getPredicate().toString();
		else
			return "(fail)";
	}

	public String retractFact(String author, String fact) {
		PredicateContainer p = createContainer(author, fact);
		PredicateContainer queried;
		try {
			queried = queryMatchData(p);
		} catch (RedisKeyNotFoundException e) {
			return "(failed)";
		}
		if(queried!=null){
			retractData(queried);
		}
		return "(ok)";
	}

	public String updateFact(String author, String fact) {
		GeneralizedList updataGL = null;
		try {
			updataGL = GLFactory.newGLFromGLString(fact);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		PredicateContainer before = createContainer(author, updataGL
				.getExpression(0).asGeneralizedList());
		PredicateContainer after = createContainer(author, updataGL
				.getExpression(1).asGeneralizedList());
		PredicateContainer queried;
		try {
			queried = queryMatchData(before);
		} catch (RedisKeyNotFoundException e) {
			return "(failed)";
		}
		Binding b = queried.getPredicate().unify(before.getPredicate(), null);
		after = createContainer(author, after.getPredicate().evaluate(b));
		retractData(queried);
		assertData(after);
		psCommand.publish(SubscribeChannel, after.getPredicateKey());
		return "(ok)";
	}

	public String assertFact(String author, String string) {
		PredicateContainer p = createContainer(author, string);
		assertData(p);
		psCommand.publish(SubscribeChannel, p.getPredicateKey());
		return "(ok)";
	}

	public String subscribe(String author, String rule) {
		System.out.println("before rule add");
		Rule r = RuleFactory.newRuleFromRuleString(author, rule);
		System.out.println("rule added");
		String id = subscriber.addRule(r);
		System.out.println("befores return : "+id);
		return id;
	}

	public String unsubscribe(String author, String id) {
		subscriber.removeRule(id);
		return "(ok)";
	}

	public String requestStream(String author, String string) {
		return null;
	}

	public String releaseStream(String author, String string) {
		return null;
	}

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
