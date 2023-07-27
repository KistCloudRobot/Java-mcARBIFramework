package kr.ac.uos.ai.arbi.framework.center;

import java.util.List;

import com.lambdaworks.redis.api.sync.RedisCommands;

import kr.ac.uos.ai.arbi.model.Binding;
import kr.ac.uos.ai.arbi.model.Expression;
import kr.ac.uos.ai.arbi.model.ExpressionList;
import kr.ac.uos.ai.arbi.model.GLFactory;
import kr.ac.uos.ai.arbi.model.GeneralizedList;
import kr.ac.uos.ai.arbi.model.Value;
import kr.ac.uos.ai.arbi.utility.Configuration;
import kr.ac.uos.ai.arbi.utility.DebugUtilities;

public class RedisUtil {
	public static final String PredicatePrefix = "predicate:";
	public static final String PredicateNamePrefix = "predicateName:";
	public static final String CreateTimeKey = "CreateTime";
	public static final String PredicateNameKey = "Predicate";
	public static final String AuthorKey = "Author";
	public static final String ExpressionNumberKey = "ExpressionNumber";
	public static final String ExpressionPrefix = "Expression:";
	public static final String ExpressionTypePrefix = "ExpressionType:";
	public static final String SubscribeChannel = "LTMSubscribe";
	public static int	   	   ExpireTime = 60;
	
	private static RedisCommands<String, String> command;

	public static void setCommand(RedisCommands<String, String> command) {
		RedisUtil.command = command;
		
	}

	public static PredicateContainer createContainer(String author,
			GeneralizedList predicateGL) {
		return createContainer(author, System.currentTimeMillis(), predicateGL);
	}

	public static PredicateContainer createContainer(String author,
			long createTime, GeneralizedList predicateGL) {
		return new PredicateContainer(author, createTime, predicateGL);
	}

	public static PredicateContainer createContainer(String author,
			String predicateString) {
		return createContainer(author, System.currentTimeMillis(), predicateString);
	}

	public static PredicateContainer createContainer(String author,
			long createTime, String predicateString) {
		return new PredicateContainer(author, createTime, predicateString);
	}

	

	
	
	public static Expression getExpressionWithType(String expression, String dataType) {
		Value value = null;
		if(dataType.equals("INT")) {
			value = GLFactory.newValue(Integer.parseInt(expression));
		}else if(dataType.equals("FLOAT")) {
			value = GLFactory.newValue(Float.parseFloat(expression));
		}else {
			value = GLFactory.newValue(expression);
		}
		
		return GLFactory.newExpression(value);
	}

	public static void retractData(PredicateContainer queried) {
		if (queried == null) {
			return;
		}
		retractGL(queried.getCreateTime(), queried.getPredicate());
		command.zrem(
				PredicateNamePrefix + queried.getPredicate().getName(),
				makePredicateKey(queried.getPredicate().getName(),
						queried.getCreateTime()));
	}

	private static void retractGL(long createTime, GeneralizedList predicate) {
		for (int i = 0; i < predicate.getExpressionsSize(); i++) {
			if (predicate.getExpression(i).isGeneralizedList()) {
				retractGL(createTime, predicate.getExpression(i)
						.asGeneralizedList());
			}
			String key = makePredicateKey(predicate.getName(), createTime);
			command.del(key);
		}

	}

	public static void assertData(PredicateContainer data) {
		assertGL(data.getAuthor(), data.getCreateTime(), data.getPredicate());

		command.zadd(
				PredicateNamePrefix + data.getPredicate().getName(),
				data.getCreateTime(),
				makePredicateKey(data.getPredicate().getName(),
						data.getCreateTime()));
		
//		if(ExpireTime != 0)
//			command.expire(PredicateNamePrefix + data.getPredicate().getName(), ExpireTime);
	}

	public static PredicateContainer queryMatchData(PredicateContainer predicate) throws RedisKeyNotFoundException {
		String name = predicate.getPredicate().getName();
		List<String> predicateKeyList = command.zrange(PredicateNamePrefix
				+ name, 0, -1);
		
		PredicateContainer resultGL = null;
		
		for (int i = 0; i < predicateKeyList.size(); i++) {
			PredicateContainer queriedGL = queryPredicateDataByKey(predicateKeyList.get(i));
			Binding b = predicate.getPredicate().unify(
					queriedGL.getPredicate(), null);
			if (b != null) {
				resultGL = queriedGL;
			}
		}
		return resultGL;
	}

	
	public static PredicateContainer queryPredicateDataByKey(String key) throws RedisKeyNotFoundException {
		PredicateContainer container = null;
		if (!key.startsWith(PredicatePrefix)) {
			key = PredicatePrefix + key;
		}
		String sizeString = command.hget(key, ExpressionNumberKey);
		if (sizeString == null) {
			throw new RedisKeyNotFoundException();
		}
		int size = Integer.valueOf(sizeString);
		
		
		
		ExpressionList exps = new ExpressionList();

		for (int i = 0; i < size; i++) {
			String expression = command.hget(key, ExpressionPrefix + i);	
			
			if(expression == null) {
				throw new RedisKeyNotFoundException();
			}
			
			if (expression.startsWith(PredicatePrefix)) {
				exps.add(GLFactory.newExpression(queryPredicateDataByKey(
						expression).getPredicate()));
			} else {
				String dataType = command.hget(key, ExpressionTypePrefix + i);
				if(dataType == null) {
					throw new RedisKeyNotFoundException();
				}
				Expression resultExpression = getExpressionWithType(expression,dataType);
				
				
				exps.add(resultExpression);
			}
		}
		String glString =  command.hget(key, PredicateNameKey);
		if(glString == null) {
			throw new RedisKeyNotFoundException();
		}
		GeneralizedList predicate = GLFactory.newGL(glString, exps);
		
		String createTimeKey = command.hget(key, CreateTimeKey);
		if (createTimeKey == null) {
			throw new RedisKeyNotFoundException();
		}
		
		String result = command.hget(key, AuthorKey);
		if (result == null) {
			throw new RedisKeyNotFoundException();
		}
		container = new PredicateContainer(result,Long.valueOf(createTimeKey), predicate);
		//System.out.println("containter null? : " + container);
		//System.out.println("containter info : " + container.getPredicateKey().toString());
		
		
		return container;
	}

	private static String assertGL(String author, long createTime,
			GeneralizedList predicate) {
		String key = makePredicateKey(predicate.getName(), createTime);
		
		if(Configuration.getLogAvailability() == true) {
			System.out.println(DebugUtilities.getDate() + "RedisUtil assertGL key generated : " + key + " : " + predicate.toString());
		}
		command.hset(key, AuthorKey, author);
		command.hset(key, CreateTimeKey, String.valueOf(createTime));
		command.hset(key, PredicateNameKey, predicate.getName());
		command.hset(key, ExpressionNumberKey,
				String.valueOf(predicate.getExpressionsSize()));
		for (int i = 0; i < predicate.getExpressionsSize(); i++) {
			if (predicate.getExpression(i).isGeneralizedList()) {
				String subkey = assertGL(author, createTime, predicate
						.getExpression(i).asGeneralizedList());
				command.hset(key, ExpressionPrefix + i, subkey);
			} else {
				String typeData = predicate.getExpression(i).asValue().getType().toString();
				command.hset(key, ExpressionPrefix + i, predicate.getExpression(i).toString().replaceAll("\"", ""));
				command.hset(key, ExpressionTypePrefix + i , typeData);
			}
		}
		
//		if(ExpireTime != 0)
//			command.expire(key, ExpireTime);
		
		return key;
	}

	private static String makePredicateKey(String name, long time) {
		return PredicatePrefix + name + ":" + time;
	}

}
