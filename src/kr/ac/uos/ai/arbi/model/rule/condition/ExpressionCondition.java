package kr.ac.uos.ai.arbi.model.rule.condition;

import java.util.LinkedList;
import java.util.List;

import kr.ac.uos.ai.arbi.model.Expression;


class ExpressionCondition implements Condition {
	private final Expression				_expression;
	
	ExpressionCondition(Expression expression) {
		_expression = expression;
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
