package kr.ac.uos.ai.arbi.model;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

class BindingImpl implements Binding {
	private final ConcurrentMap<String, Expression>			_bindingMap;

	BindingImpl() {
		_bindingMap = new ConcurrentHashMap<String, Expression>();
	}
	
	public Binding copy(Binding binding) {
		if (binding != null) {
			for (String variableName : binding.getBoundedVariableNames()) {
				bind(variableName, binding.retrieve(variableName));
			}
		}
		return this;
	}
	
	private static final String[] NULL_STRING = new String[0];
	public String[] getBoundedVariableNames() {
		return _bindingMap.keySet().toArray(NULL_STRING);
	}
	
	public void bind(Variable variable, Expression expression) {
		bind(variable.getName(), expression);
	}
	
	public void bind(String variableName, Expression expression) {
		_bindingMap.put(variableName, expression);
	}
	
	public void unbind(Variable variable) {
		unbind(variable.getName());
	}
	
	public void unbind(String variableName) {
		_bindingMap.remove(variableName);
	}
	
	public void unbind(Expression expression) {
		if (expression.isVariable()) {
			unbind(expression.asVariable());
		} else if (expression.isGeneralizedList()) {
			unbind(expression.asGeneralizedList());
		} else if (expression.isFunction()) {
			unbind(expression.asFunction());
		}
	}
	
	public void unbind(GeneralizedList gl) {
		for (int i=0, n=gl.getExpressionsSize(); i<n; i++) {
			unbind(gl.getExpression(i));
		}
	}
	
	public void unbind(Function function) {
		for (int i=0, n=function.getExpressionsSize(); i<n; i++) {
			unbind(function.getExpression(i));
		}
	}
	
	public Expression retrieve(Variable variable) {
		return retrieve(variable.getName());
	}
	
	public Expression retrieve(String variableName) {
		Expression expression = _bindingMap.get(variableName);
		return (expression != null) ? expression : Expression.UNDEFINED;
	}
	
	public String toString() {
		return _bindingMap.toString();		
	}

}
