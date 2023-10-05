package kr.ac.uos.ai.arbi.model.rule.condition;

import kr.ac.uos.ai.arbi.model.Binding;
import kr.ac.uos.ai.arbi.model.Expression;
import kr.ac.uos.ai.arbi.model.GLFactory;
import kr.ac.uos.ai.arbi.model.GeneralizedList;
import kr.ac.uos.ai.arbi.model.rule.condition.ConditionFactory.ConditionType;

public class ExclusiveCondition implements Condition {
private final GeneralizedList			_predicate;
	
	ExclusiveCondition(GeneralizedList predicate) {
		_predicate = predicate;
	}

	@Override
	public boolean checkCondition() {
		return false;
	}

	@Override
	public String getPredicateName() {
		return _predicate.getName();
	}

	@Override
	public Expression[] getExpressions() {
		Expression[] exps = new Expression[_predicate.getExpressionsSize()];
		for(int i = 0; i< exps.length; i++){
			exps[i]= _predicate.getExpression(i);
		}
		return exps;
	}

	@Override
	public String toString() {
		return _predicate.toString();
	}
	
	public ConditionType getType() {
		return ConditionType.exclusive;
	}

	@Override
	public Expression getEvaluatedExpression(Binding b) {

		return GLFactory.newExpression(_predicate).evaluate(b);
	}
}
