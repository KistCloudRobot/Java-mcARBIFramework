package kr.ac.uos.ai.arbi.model;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

public class ExpressionList {
	private final List<Expression>				_expressionList;
	
	public ExpressionList() {
		_expressionList = new LinkedList<Expression>();
	}
	
	public void add(Expression expression) {
		_expressionList.add(expression);
	}
	
	public void add(int index, Expression expression) {
		_expressionList.add(index, expression);
	}
	
	public void remove(Expression expression) {
		_expressionList.remove(expression);
	}
	
	public void remove(int index) {
		_expressionList.remove(index);
	}
	
	public Expression get(int index) {
		return _expressionList.get(index);
	}
	
	public void clear() {
		_expressionList.clear();
	}
	
	
	
	private static final Expression[] NULL_EXPRESSION = new Expression[0];
	public Expression[] toArray() {
		return _expressionList.toArray(NULL_EXPRESSION);
	}
}
