package kr.ac.uos.ai.arbi.model.functions;

import kr.ac.uos.ai.arbi.model.*;

public class GreaterThan extends AbstractFunction {
	public GreaterThan(Expression... expressions) {
		super("gt", expressions);
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
					if (value.gt(nextValue)) {
						value = nextValue;
					} else {
						return Expression.FALSE;
					}
				}
			}
		}
		if (!check) {
		}
		return Expression.TRUE;
	}
}
