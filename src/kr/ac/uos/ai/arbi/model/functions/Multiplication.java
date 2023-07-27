package kr.ac.uos.ai.arbi.model.functions;

import kr.ac.uos.ai.arbi.model.*;

public class Multiplication extends AbstractFunction {
	public Multiplication(Expression... expressions) {
		super("mul", expressions);
	}

	public Expression evaluate(Binding binding) {
		if (_expressions.length == 0) {
			return GLFactory.newValueExpression(0);
		}
		Value result = null;
		for (Expression expression : _expressions) {
			Expression evaluatedExpression = expression.evaluate(binding);
			if (evaluatedExpression.isValue()) {
				result = (result == null) ? evaluatedExpression.asValue() : result.mul(evaluatedExpression.asValue());
			}
		}
		return (result != null) ? GLFactory.newExpression(result) : GLFactory.newValueExpression(0);
	}
}
