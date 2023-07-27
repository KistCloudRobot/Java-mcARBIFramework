package kr.ac.uos.ai.arbi.model.functions;

import kr.ac.uos.ai.arbi.model.*;

public class GreaterThanEquals extends AbstractFunction {
	public GreaterThanEquals(Expression... expressions) {
		super("ge", expressions);
		if (_expressions.length < 2) {
			throw new IllegalArgumentException("");
		}
	}

	public Expression evaluate(Binding binding) {
		boolean check = false;
		Value value = Value.UNDEFINED;
		for (Expression expression : _expressions) {
			Expression evaluatedExpression = expression.evaluate(binding);
			if (evaluatedExpression.isValue()) {
				Value nextValue = evaluatedExpression.asValue();
				if (value == Value.UNDEFINED) {
					value = nextValue;
				} else {
					check = true;
					if (value.gt(nextValue) || value.eq(nextValue)) {
						value = nextValue;
					} else {
						return Expression.FALSE;
					}
				}
			}
		}
		if (!check) {
			throw new IllegalStateException("");
		}
		return Expression.TRUE;
	}
}
