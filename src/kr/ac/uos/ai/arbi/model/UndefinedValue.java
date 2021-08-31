package kr.ac.uos.ai.arbi.model;

class UndefinedValue implements Value {
	UndefinedValue() {
		//
	}
	
	public Type getType() {
		return Value.Type.UNDEFINED;
	}

	public int intValue() {
		throw new UnsupportedOperationException(toString());
	}

	public float floatValue() {
		throw new UnsupportedOperationException(toString());
	}
	
	public boolean booleanValue() {
		throw new UnsupportedOperationException(toString());
	}
	
	public String stringValue() {
		throw new UnsupportedOperationException(toString());
	}
	
	public Value add(Value value) {
		throw new UnsupportedOperationException(toString());
	}
	
	public Value sub(Value value) {
		throw new UnsupportedOperationException(toString());
	}
	
	public Value mul(Value value) {
		throw new UnsupportedOperationException(toString());
	}

	public Value div(Value value) {
		throw new UnsupportedOperationException(toString());
	}
	
	public Value mod(Value value) {
		throw new UnsupportedOperationException(toString());
	}
	
	public boolean lt(Value value) {
		throw new UnsupportedOperationException(toString());
	}
	
	public boolean gt(Value value) {
		throw new UnsupportedOperationException(toString());
	}
	
	public boolean eq(Value value) {
		return (value == this);
	}

	public String toString() {
		return "undefined";
	}
	
	public String toXML() {
		return "<value type=\"undefined\"/>";
	}
}
