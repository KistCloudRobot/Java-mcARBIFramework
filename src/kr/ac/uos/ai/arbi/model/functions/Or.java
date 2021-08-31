package kr.ac.uos.ai.arbi.model.functions;

import kr.ac.uos.ai.arbi.model.*;

public class Or extends AbstractFunction {
	public Or(Expression... expressions) {
		super("or", expressions);
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
				if (evaluatedExpression.asValue().booleanValue()) {
					return Expression.TRUE;
				}
			}
		}
		if (!check) {
			throw new IllegalStateException("");
		}
		return Expression.FALSE;
	}
}
