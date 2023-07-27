package kr.ac.uos.ai.arbi.model.functions;

import kr.ac.uos.ai.arbi.model.*;

public class Not extends AbstractFunction {
	public Not(Expression... expressions) {
		super("not", expressions);
		if (_expressions.length != 1) {
			throw new IllegalArgumentException("");
		}
	}
	
	public Expression evaluate(Binding binding) {
		Expression evaluatedExpression = _expressions[0].evaluate(binding);
		if (evaluatedExpression.isValue()) {
			return evaluatedExpression.asValue().booleanValue() ? Expression.FALSE : Expression.TRUE;
		} else {
			throw new IllegalStateException("");
		}
	}
}
