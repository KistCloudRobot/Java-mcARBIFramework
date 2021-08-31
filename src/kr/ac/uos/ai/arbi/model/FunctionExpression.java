package kr.ac.uos.ai.arbi.model;

class FunctionExpression implements Expression {
	private final Function				_function;
	
	FunctionExpression(Function function) {
		_function = function;
	}

	public boolean isValue() {
		return false; 
	}

	public boolean isVariable() {
		return false; 
	}

	public boolean isFunction() {
		return true;
	}
	
	public boolean isGeneralizedList() {
		return false; 
	}

	public Value asValue() {
		throw new UnsupportedOperationException(); 
	}

	public Variable asVariable() {
		throw new UnsupportedOperationException(); 
	}

	public Function asFunction() {
		return _function;
	}
	
	public GeneralizedList asGeneralizedList() {
		throw new UnsupportedOperationException(); 
	}

	public Expression evaluate(Binding binding) {
		return _function.evaluate(binding); 
	}
	
	public String toString() {
		return _function.toString();
	}
	
}
