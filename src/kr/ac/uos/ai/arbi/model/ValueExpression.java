package kr.ac.uos.ai.arbi.model;


class ValueExpression implements Expression {
	private final Value				_value;
	
	ValueExpression(Value value) {
		_value = value;
	}

	public boolean isValue() {
		return true; 
	}

	public boolean isVariable() {
		return false; 
	}

	public boolean isFunction() {
		return false;
	}
	
	public boolean isGeneralizedList() {
		return false; 
	}

	public Value asValue() {
		return _value; 
	}

	public Variable asVariable() {
		throw new UnsupportedOperationException(); 
	}

	public Function asFunction() {
		throw new UnsupportedOperationException();
	}
	
	public GeneralizedList asGeneralizedList() {
		throw new UnsupportedOperationException(); 
	}

	public Expression evaluate(Binding binding) {
		return this; 
	}
	
	public String toString() {
		return _value.toString();
	}
	
}
