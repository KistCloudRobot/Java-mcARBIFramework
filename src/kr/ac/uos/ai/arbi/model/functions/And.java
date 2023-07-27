package kr.ac.uos.ai.arbi.model.functions;

import kr.ac.uos.ai.arbi.model.*;

public class And extends AbstractFunction {
	public And(Expression... expressions) {
		super("and", expressions);
		if (_expressions.length < 2) {
			throw new IllegalArgumentException("");
		}
	}
	
	public Expression evaluate(Binding binding) {
		boolean check = false;
		for (Expression expression : _expressions) {
			Expression evaluatedExpression = expression.evaluate(binding);
			if (evaluatedExpression.isValue()) {
				check = true;
				if (!evaluatedExpression.asValue().booleanValue()) {
					return Expression.FALSE;
				}
			}
		}
		if (!check) {
			throw new IllegalStateException("");
		}
		return Expression.TRUE;
	}
}
