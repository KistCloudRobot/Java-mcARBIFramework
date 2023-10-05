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
		
		sb.append("#");
		if(_expressions.length == 1) {
			sb.append("(");
			sb.append(_name);
			sb.append(" ");
			sb.append(_expressions[0]);
			sb.append(")");
		}else if(_expressions.length == 2) {
			sb.append("(");
			sb.append(_expressions[0]).append(" ");
			sb.append(_name).append(" ");
			sb.append(_expressions[1]);
			sb.append(")");
		}else {
			sb.append("(").append(_name);
			for (Expression expression : _expressions) {
				sb.append(" ").append(expression);
			}
			sb.append(")");
		}
		
		
		return sb.toString();
	}
	
}
