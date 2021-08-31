package kr.ac.uos.ai.arbi.model.functions;

import kr.ac.uos.ai.arbi.model.*;

public class Addition extends AbstractFunction {
	public Addition(Expression... expressions) {
		super("add", expressions);
	}
	
	public Expression evaluate(Binding binding) {
		Value result = GLFactory.newValue(0);
		for (Expression expression : _expressions) {
			Expression evaluatedExpression = expression.evaluate(binding);
			if (evaluatedExpression.isValue()) {
				result = result.add(evaluatedExpression.asValue());
			}
		}
		return GLFactory.newExpression(result);
	}
}
