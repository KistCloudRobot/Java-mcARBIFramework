package kr.ac.uos.ai.arbi.model.rule.condition;

import java.util.LinkedList;
import java.util.List;

import kr.ac.uos.ai.arbi.model.Binding;
import kr.ac.uos.ai.arbi.model.Expression;
import kr.ac.uos.ai.arbi.model.GeneralizedList;
import kr.ac.uos.ai.arbi.model.rule.condition.ConditionFactory.ConditionType;


class ExpressionCondition implements Condition {
	private final Expression				_expression;
	
	public ExpressionCondition(Expression expression) {
		_expression = expression;
	}

	@Override
	public boolean checkCondition() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getPredicateName() {
		String name = "";
		if(_expression.isGeneralizedList() == true) {
			name = _expression.asGeneralizedList().getName();
			
		}else if(_expression.isFunction() == true) {
			name = _expression.asFunction().getName();
		}

		return name;
	}

	@Override
	public Expression[] getExpressions() {
		// TODO Auto-generated method stub
		return null;
	}

	public ConditionType getType() {
		return ConditionType.expression;
	}

	@Override
	public Expression getEvaluatedExpression(Binding b) {
		Expression result = _expression.evaluate(b);
		return result;
	}
}
