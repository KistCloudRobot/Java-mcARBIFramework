package kr.ac.uos.ai.arbi.model.rule;

import java.util.LinkedList;
import java.util.List;

import com.lambdaworks.redis.api.sync.RedisCommands;

import kr.ac.uos.ai.arbi.model.Binding;
import kr.ac.uos.ai.arbi.model.BindingFactory;
import kr.ac.uos.ai.arbi.model.Expression;
import kr.ac.uos.ai.arbi.model.rule.action.Action;
import kr.ac.uos.ai.arbi.model.rule.condition.Condition;
import kr.ac.uos.ai.arbi.model.rule.condition.ConditionFactory;


public class Rule{
	
	private final Condition[]				conditions;
	private final Action[]					actions;

	public Rule(Condition[] conditions, Action[] actions) {
		this.conditions				= conditions.clone();
		this.actions					= actions.clone();
	}

	public Condition[] getConditions(){
		return conditions;
	}
	
	public Action[] getActions(){
		return actions;
	}
	
	public String toString() {
		StringBuilder result = new StringBuilder();
		
		for(int i = 0; i < conditions.length;i++) {
			result.append(conditions[i].toString());
			result.append("\n");
		}
		
		return result.toString();
	}

	
}
