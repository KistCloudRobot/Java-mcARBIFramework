package kr.ac.uos.ai.arbi.model;

class GeneralizedListImpl implements GeneralizedList {
	private final String				_name;
	private final Expression[]			_expressions;
	
	GeneralizedListImpl(String name, Expression... expressions) {
		_name				= name;
		_expressions		= expressions.clone();
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

	public GeneralizedList evaluate(Binding binding) {
		Expression[] evaluatedExpressions = new Expression[_expressions.length];
		for (int i=0, n=_expressions.length; i<n; i++) {
			evaluatedExpressions[i] = _expressions[i].evaluate(binding);
		}
		return new GeneralizedListImpl(_name, evaluatedExpressions);
	}
	
	public Binding unify(GeneralizedList gl, Binding binding) {
		if (!_name.equals(gl.getName())) {
			return null;
		}
		if (getExpressionsSize() != gl.getExpressionsSize()) {
			return null;
		}
		
		Binding unifier = BindingFactory.newBinding();
		for (int i=0, n=_expressions.length; i<n; i++) {
			if (!unify(_expressions[i], gl.getExpression(i), binding, unifier)) {
				return null;
			}
		}
		return unifier;
	}
	
	private Expression getBoundedExpression(Variable variable, Binding binding, Binding unifier) {
		Expression boundedExpression = (binding != null) ? binding.retrieve(variable) : Expression.UNDEFINED;
		if (boundedExpression == Expression.UNDEFINED) {
			boundedExpression = unifier.retrieve(variable);
		}
		if (boundedExpression.isVariable()) {
			throw new IllegalStateException();	// boundedExpresion: variable; ������� ��=
		}
		return boundedExpression;
	}
	
	private Expression getEvaluatedExpression(Function function, Binding binding, Binding unifier) {
		return function.evaluate(BindingFactory.newBinding(binding).copy(unifier));
	}
	
	private boolean unify(Expression lhExpression, Expression rhExpression, Binding binding, Binding unifier) {
		if (lhExpression.isValue()) {
			return unify(rhExpression, lhExpression.asValue(), binding, unifier);
		} else if (lhExpression.isVariable()) {
			return unify(rhExpression, lhExpression.asVariable(), binding, unifier);
		} else if (lhExpression.isFunction()) {
			return unify(rhExpression, lhExpression.asFunction(), binding, unifier);
		} else if (lhExpression.isGeneralizedList()) {
			return unify(rhExpression, lhExpression.asGeneralizedList(), binding, unifier);
		}
		return false;
	}
	
	private boolean unify(Expression lhExpression, Value rhValue, Binding binding, Binding unifier) {
		if (lhExpression.isValue()) {
			return unify(rhValue, lhExpression.asValue());
		} else if (lhExpression.isVariable()) {
			return unify(lhExpression.asVariable(), rhValue, binding, unifier);
		} else if (lhExpression.isFunction()) {
			return unify(lhExpression.asFunction(), rhValue, binding, unifier);
		}
		return false;
	}
	
	private boolean unify(Expression lhExpression, Variable rhVariable, Binding binding, Binding unifier) {
		if (lhExpression.isValue()) {
			return unify(rhVariable, lhExpression.asValue(), binding, unifier);
		} else if (lhExpression.isVariable()) {
			return unify(rhVariable, lhExpression.asVariable(), binding, unifier);
		} else if (lhExpression.isFunction()) {
			return unify(rhVariable, lhExpression.asFunction(), binding, unifier);
		} else if (lhExpression.isGeneralizedList()) {
			return unify(rhVariable, lhExpression.asGeneralizedList(), binding, unifier);
		}
		return false;
	}
	
	private boolean unify(Expression lhExpression, Function rhFunction, Binding binding, Binding unifier) {
		if (lhExpression.isValue()) {
			return unify(rhFunction, lhExpression.asValue(), binding, unifier);
		} else if (lhExpression.isVariable()) {
			return unify(lhExpression.asVariable(), rhFunction, binding, unifier);
		} else if (lhExpression.isFunction()) {
			return unify(lhExpression.asFunction(), rhFunction, binding, unifier);
		} else if (lhExpression.isGeneralizedList()) {
			return unify(rhFunction, lhExpression.asGeneralizedList(), binding, unifier);
		}
		return false;
	}
	
	private boolean unify(Expression lhExpression, GeneralizedList rhGL, Binding binding, Binding unifier) {
		if (lhExpression.isVariable()) {
			return unify(lhExpression.asVariable(), rhGL, binding, unifier);
		} else if (lhExpression.isGeneralizedList()) {
			return unify(lhExpression.asGeneralizedList(), rhGL, binding, unifier);
		}
		return false;	
	}

	private boolean unify(Value lhValue, Value rhValue) {
		return lhValue.equals(rhValue);
	}
	
	private boolean unify(Variable lhVariable, Value rhValue, Binding binding, Binding unifier) {
		Expression lhBoundedExpression = getBoundedExpression(lhVariable, binding, unifier);
		if (lhBoundedExpression == Expression.UNDEFINED) {
			
			unifier.bind(lhVariable, GLFactory.newExpression(rhValue));
			return true;
			
		} else if (lhBoundedExpression.isValue()) {
			return unify(lhBoundedExpression.asValue(), rhValue);
		} 
		return false;
	}
	
	private boolean unify(Variable lhVariable, Variable rhVariable, Binding binding, Binding unifier) {
		Expression lhBoundedExpression = getBoundedExpression(lhVariable, binding, unifier);
		if (lhBoundedExpression == Expression.UNDEFINED) {
			
			Expression rhBoundedExpression = getBoundedExpression(rhVariable, binding, unifier);
			if (rhBoundedExpression != Expression.UNDEFINED) {
				unifier.bind(lhVariable, rhBoundedExpression);
			} 
			return true;
			
		} else if (lhBoundedExpression.isValue()) {
			return unify(rhVariable, lhBoundedExpression.asValue(), binding, unifier);
		} else if (lhBoundedExpression.isGeneralizedList()) {
			return unify(rhVariable, lhBoundedExpression.asGeneralizedList(), binding, unifier);
		}
		return false;
	}
	
	private boolean unify(Variable lhVariable, Function rhFunction, Binding binding, Binding unifier) {
		Expression lhBoundedExpression = getBoundedExpression(lhVariable, binding, unifier);
		if (lhBoundedExpression == Expression.UNDEFINED) {

			Expression rhEvaluatedExpression = getEvaluatedExpression(rhFunction, binding, unifier);
			if (rhEvaluatedExpression != Expression.UNDEFINED) {
				unifier.bind(lhVariable, rhEvaluatedExpression);
			} 
			return true;
			
		} else if (lhBoundedExpression.isValue()) {
			return unify(rhFunction, lhBoundedExpression.asValue(), binding, unifier);
		} else if (lhBoundedExpression.isGeneralizedList()) {
			return unify(rhFunction, lhBoundedExpression.asGeneralizedList(), binding, unifier);
		}
		return false;
	}
	
	private boolean unify(Variable lhVariable, GeneralizedList rhGL, Binding binding, Binding unifier) {
		Expression lhBoundedExpression = getBoundedExpression(lhVariable, binding, unifier);
		if (lhBoundedExpression == Expression.UNDEFINED) {
			
			unifier.bind(lhVariable, GLFactory.newExpression(rhGL));
			return true;
			
		} else if (lhBoundedExpression.isGeneralizedList()) {
			return unify(rhGL, lhBoundedExpression.asGeneralizedList(), binding, unifier);
		}
		return false;
	}

	private boolean unify(Function lhFunction, Value rhValue, Binding binding, Binding unifier) {
		Expression lhEvaluatedExpression = getEvaluatedExpression(lhFunction, binding, unifier);
		if (lhEvaluatedExpression.isValue()) {
			return unify(lhEvaluatedExpression.asValue(), rhValue);
		} else if (lhEvaluatedExpression.isVariable()) {
			return unify(lhEvaluatedExpression.asVariable(), rhValue, binding, unifier);
		} else if (lhEvaluatedExpression.isFunction()) {
			return unify(lhEvaluatedExpression.asFunction(), rhValue, binding, unifier);
		}
		return false;
	}
	
	private boolean unify(Function lhFunction, Function rhFunction, Binding binding, Binding unifier) {
		Expression lhEvaluatedExpression = getEvaluatedExpression(lhFunction, binding, unifier);
		if (lhEvaluatedExpression == Expression.UNDEFINED) {
			
			Expression rhEvaluatedExpression = getEvaluatedExpression(rhFunction, binding, unifier);
			return (rhEvaluatedExpression == Expression.UNDEFINED);
			
		} else if (lhEvaluatedExpression.isValue()) {
			return unify(rhFunction, lhEvaluatedExpression.asValue(), binding, unifier);
		} else if (lhEvaluatedExpression.isVariable()) {
			return unify(lhEvaluatedExpression.asVariable(), rhFunction, binding, unifier);
		} else if (lhEvaluatedExpression.isFunction()) {
			return unify(lhEvaluatedExpression.asFunction(), rhFunction, binding, unifier);
		} else if (lhEvaluatedExpression.isGeneralizedList()) {
			return unify(rhFunction, lhEvaluatedExpression.asGeneralizedList(), binding, unifier);
		}
		return false;
	}
	
	private boolean unify(Function lhFunction, GeneralizedList rhGL, Binding binding, Binding unifier) {
		Expression lhEvaluatedExpression = getEvaluatedExpression(lhFunction, binding, unifier);
		if (lhEvaluatedExpression.isVariable()) {
			return unify(lhEvaluatedExpression.asVariable(), rhGL, binding, unifier);
		} else if (lhEvaluatedExpression.isFunction()) {
			return unify(lhEvaluatedExpression.asFunction(), rhGL, binding, unifier);
		} else if (lhEvaluatedExpression.isGeneralizedList()) {
			return unify(lhEvaluatedExpression.asGeneralizedList(), rhGL, binding, unifier);
		}
		return false;
	}
	
	private boolean unify(GeneralizedList lhGL, GeneralizedList rhGL, Binding binding, Binding unifier) {
		Binding tempBinding = BindingFactory.newBinding(binding).copy(unifier);
		Binding tempUnifier = lhGL.unify(rhGL, tempBinding);
		if (tempUnifier != null) {
			unifier.copy(tempUnifier);
			return true;
		}
		return false;
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("(").append(_name);
		for (Expression expression : _expressions) {
			sb.append(" ").append(expression);
		}
		sb.append(")");
		return sb.toString();
	}

}
