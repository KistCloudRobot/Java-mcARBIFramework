package kr.ac.uos.ai.arbi.model.functions;

import kr.ac.uos.ai.arbi.model.*;

public class Test extends AbstractFunction {
	public Test(Expression... expressions) {
		super("test", expressions);
		if (expressions.length != 1) {
			throw new IllegalArgumentException("");
		}
	}
	
	public Expression evaluate(Binding binding) {
		Expression evaluatedExpression = _expressions[0].evaluate(binding);
		if (evaluatedExpression.isValue()) {
			return evaluatedExpression.asValue().booleanValue() ? Expression.TRUE : Expression.FALSE;
		}
		return Expression.FALSE;
	}
}
