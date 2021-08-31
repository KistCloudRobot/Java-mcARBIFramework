package kr.ac.uos.ai.arbi.model;


class VariableExpression implements Expression {
	private final Variable			_variable;
	
	VariableExpression(Variable variable) {
		_variable = variable;
	}

	public boolean isValue() {
		return false; 
	}

	public boolean isVariable() {
		return true; 
	}

	public boolean isFunction() {
		return false;
	}
	
	public boolean isGeneralizedList() {
		return false; 
	}

	public Value asValue() {
		throw new UnsupportedOperationException(); 
	}

	public Variable asVariable() {
		return _variable; 
	}

	public Function asFunction() {
		throw new UnsupportedOperationException();
	}
	
	public GeneralizedList asGeneralizedList() {
		throw new UnsupportedOperationException(); 
	}

	public Expression evaluate(Binding binding) {
		return (binding != null) ? binding.retrieve(_variable) : Expression.UNDEFINED; 
	}
	
	public String toString() {
		return _variable.toString();
	}
	
}
