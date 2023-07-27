package kr.ac.uos.ai.arbi.model.rule.condition;

import java.util.LinkedList;
import java.util.List;

import kr.ac.uos.ai.arbi.model.Expression;
import kr.ac.uos.ai.arbi.model.GeneralizedList;
import kr.ac.uos.ai.arbi.model.rule.condition.ConditionFactory.Type;

public class RetractedCondition implements Condition{
	
	private final GeneralizedList			_predicate;

	RetractedCondition(GeneralizedList predicate) {
		_predicate = predicate;
	}

	@Override
	public boolean checkCondition() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getPredicateName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Expression[] getExpressions() {
		// TODO Auto-generated method stub
		return null;
	}
	


}
