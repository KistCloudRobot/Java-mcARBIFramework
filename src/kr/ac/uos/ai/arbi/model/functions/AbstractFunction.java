package kr.ac.uos.ai.arbi.model.functions;

import kr.ac.uos.ai.arbi.model.Expression;
import kr.ac.uos.ai.arbi.model.Function;


public abstract class AbstractFunction implements Function {
	protected final String					_name;
	protected final Expression[]			_expressions;

	private static final Expression[] NULL_EXPRESSION = new Expression[0];
	public AbstractFunction(String name, Expression... expressions) {
		_name				= name;
		_expressions		= (expressions == null) ? NULL_EXPRESSION : expressions.clone();
	}
	
	public String getName() {
		return _name;
	}
	
	public int getExpressionsSize() {
		return _expressions.length;
	}
	
	public Expression getExpression(int index) {
		return _expressions[index];
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("#(").append(_name);
		for (Expression expression : _expressions) {
			sb.append(" ").append(expression);
		}
		sb.append(")");
		return sb.toString();
	}
	
}
