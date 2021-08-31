package kr.ac.uos.ai.arbi.model;


class StringValue implements Value {
	private final String		_value;
	
	StringValue(String value) {
		// unescaping
		_value = GLFactory.unescape(value);
	}
	
	public Type getType() {
		return Type.STRING; 
	}

	public int intValue() {
		return _value.length();
	}

	public float floatValue() {
		return _value.length();
	}

	public String stringValue() {
		return _value; 
	}

	public boolean 	booleanValue() {
		return (_value != null) ? _value.length() > 0 : false; 
	}
	
	public Value add(Value value) {
		return GLFactory.newValue(_value + value.stringValue());
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
		if (value.getType() == Value.Type.STRING) {
			return (_value.compareTo(value.stringValue()) < 0);
		}
		if (_value.equals("")) {
			return false;
		}
		throw new IllegalArgumentException(value.toString());
	}
	
	public boolean gt(Value value) {
		if (value.getType() == Value.Type.STRING) {
			return (_value.compareTo(value.stringValue()) > 0);
		}
		if (_value.equals("")) {
			return false;
		}
		throw new IllegalArgumentException(value.toString());
	}

	public boolean eq(Value value) {
		if (value.getType() == Value.Type.STRING) {
			return (_value.equals(value.stringValue()));
		}
		if (_value.equals("")) {
			return false;
		}
		return false;
	}

	public boolean equals(Object object) {
		if (object == this) {
			return true;
		}
		if (_value.equals("")) {
			return false;
		}
		return (object instanceof Value) ? eq((Value)object) : false;
	}
	
	public int hashCode() {
		return _value.hashCode();
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("\"").append(GLFactory.escape(_value)).append("\"");
		return sb.toString();
	}
	
}
