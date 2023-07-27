package test;

import com.lambdaworks.redis.RedisClient;
import com.lambdaworks.redis.api.StatefulRedisConnection;
import com.lambdaworks.redis.api.sync.RedisCommands;

import kr.ac.uos.ai.arbi.model.Binding;
import kr.ac.uos.ai.arbi.model.BindingFactory;
import kr.ac.uos.ai.arbi.model.Expression;
import kr.ac.uos.ai.arbi.model.Function;
import kr.ac.uos.ai.arbi.model.GLFactory;
import kr.ac.uos.ai.arbi.model.GeneralizedList;
import kr.ac.uos.ai.arbi.model.Value;
import kr.ac.uos.ai.arbi.model.parser.ParseException;

public class Test {

	public Test(){
		RedisClient client = RedisClient.create("redis://localhost:6379/0");
		StatefulRedisConnection<String, String> connection = client.connect();
		RedisCommands<String, String> syncCommands = connection.sync();
		syncCommands.set("test", "Hello Worlds");
		String worlds = syncCommands.get("test");
		System.out.println(worlds);
		String glTest = "(test $testArg)";
		Value eVal = GLFactory.newValue(5);
		String newGL = "(test 1)";
		
		Binding b = BindingFactory.newBinding();
		b.bind("testArg",GLFactory.newExpression(eVal));
	
		
		
		try {
			String expLefthand = "(test (testExpression $arg1 $arg2))";
			String expRighthand = "(test (testExpression \"a\" 2))";
			GeneralizedList glLefthand = GLFactory.newGLFromGLString(expLefthand);
			GeneralizedList glRighthand  = GLFactory.newGLFromGLString(expRighthand);

			Binding bt = glLefthand.unify(glRighthand,null);
			System.out.println(b.toString());
			System.out.println(glLefthand.getExpression(0));
			System.out.println(bt.toString());
			System.out.println();
			
			
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		
		String str = "(GoalRequest (RackLoadingPrepared &quot;agent://www.mcarbi.com/AMR_LIFT3&quot; &quot;agent://www.mcarbi.com/AMR_LIFT3_1&quot; &quot;(RackLoadingPrepared &amp;quot;AMR_LIFT3&amp;quot; 2)&quot;))";
		try {
			GeneralizedList gl = GLFactory.newGLFromGLString(str);
			System.out.println(gl);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
				
		new Test();
	}
}
