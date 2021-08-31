package kr.ac.uos.ai.arbi.model;


class UndefinedExpression implements Expression {
	UndefinedExpression() {
		//
	}
	
	public boolean isValue() {
		return false; 
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
		throw new UnsupportedOperationException(); 
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
		return "undefined";
	}
	
	public String toXML() {
		return "<expression type=\"undefined\"/>";
	}
}
