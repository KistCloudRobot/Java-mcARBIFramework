package kr.ac.uos.ai.arbi.model;


class GLExpression implements Expression {
	private final GeneralizedList				_gl;
	
	GLExpression(GeneralizedList gl) {
		_gl = gl;
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
		return true; 
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
		return _gl; 
	}

	public Expression evaluate(Binding binding) {
		return GLFactory.newExpression(_gl.evaluate(binding));
	}
	
	public String toString() {
		return _gl.toString();
	}
	
}
