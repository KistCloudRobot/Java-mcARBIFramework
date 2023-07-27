package kr.ac.uos.ai.arbi.model;

import java.util.LinkedList;
import java.util.List;

public class GeneralizedListBuilder {
	private String 						_name;
	private List<Expression>			_expressionList;
	
	public GeneralizedListBuilder() {
		this(null);
	}
	
	public GeneralizedListBuilder(String name) {
		_name				= name;
		_expressionList 	= new LinkedList<Expression>();
	}
	
	public void setName(String name) {
		_name = name;
	}
	
	public String getName() {
		return _name;
	}
	
	public void addExpression(Expression expression) {
		_expressionList.add(expression);
	}
	
	public void addExpression(int index, Expression expression) {
		_expressionList.add(index, expression);
	}
	
	public void removeExpression(Expression expression) {
		_expressionList.remove(expression);
	}

	public void removeExpression(int index) {
		_expressionList.remove(index);
	}

	public Expression getExpression(int index) {
		return _expressionList.get(index);
	}
	
	public int getSize() {
		return _expressionList.size();
	}
	
	public void clear() {
		_name = null;
		_expressionList.clear();
	}
	
	private static final Expression[] NULL_EXPRESSION = new Expression[0];
	public GeneralizedList toGeneralizedList() {
		if (_name == null) {
			throw new NullPointerException("name is null");
		}
		return GLFactory.newGL(_name, _expressionList.toArray(NULL_EXPRESSION));
	}
}
