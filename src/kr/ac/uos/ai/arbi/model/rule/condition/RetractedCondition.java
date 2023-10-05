package kr.ac.uos.ai.arbi.model.rule.condition;

import java.util.LinkedList;
import java.util.List;

import kr.ac.uos.ai.arbi.model.Binding;
import kr.ac.uos.ai.arbi.model.Expression;
import kr.ac.uos.ai.arbi.model.GLFactory;
import kr.ac.uos.ai.arbi.model.GeneralizedList;
import kr.ac.uos.ai.arbi.model.rule.condition.ConditionFactory.ConditionType;

public class RetractedCondition implements Condition{
	
	private final GeneralizedList			_predicate;

	public RetractedCondition(GeneralizedList predicate) {
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
		return _predicate.getName();
	}

	@Override
	public Expression[] getExpressions() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public ConditionType getType() {
		return ConditionType.retracted;
	}

	public Expression getEvaluatedExpression(Binding b) {

		return GLFactory.newExpression(_predicate);
	}
	
	

}
