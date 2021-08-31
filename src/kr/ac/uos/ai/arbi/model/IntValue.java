package kr.ac.uos.ai.arbi.model;

class IntValue implements Value {
	private final int			_value;
	
	IntValue(int value) {
		_value = value;
	}

	public Type getType() {
		return Type.INT; 
	}

	public int intValue() {
		return _value; 
	}

	public float floatValue() {
		return _value; 
	}

	public String stringValue() {
		return String.valueOf(_value); 
	}

	public boolean booleanValue() {
		return (_value != 0); 
	}

	public Value add(Value value) {
		switch(value.getType()) {
		case INT:
			return GLFactory.newValue(_value + value.intValue());
		case FLOAT:
			return GLFactory.newValue(_value + value.floatValue());
		case STRING:
			return GLFactory.newValue(_value + value.stringValue());
		}
		throw new IllegalArgumentException(value.toString());
	}

	public Value sub(Value value) {
		switch(value.getType()) {
		case INT:
			return GLFactory.newValue(_value - value.intValue());
		case FLOAT:
			return GLFactory.newValue(_value - value.floatValue());
		}
		throw new IllegalArgumentException(value.toString());
	}
	
	public Value mul(Value value) {
		switch(value.getType()) {
		case INT:
			return GLFactory.newValue(_value * value.intValue());
		case FLOAT:
			return GLFactory.newValue(_value * value.floatValue());
		}
		throw new IllegalArgumentException(value.toString());
	}

	public Value div(Value value) {
		switch(value.getType()) {
		case INT:
			return GLFactory.newValue(_value / value.intValue());
		case FLOAT:
			return GLFactory.newValue(_value / value.floatValue());
		}
		throw new IllegalArgumentException(value.toString());
	}
	
	public Value mod(Value value) {
		switch(value.getType()) {
		case INT:
		case FLOAT:
			return GLFactory.newValue(_value % value.intValue());
		}
		throw new IllegalArgumentException(value.toString());
	}
	
	public boolean lt(Value value) {
		switch(value.getType()) {
		case INT:
		case FLOAT:
			return (_value < value.floatValue());
		}
		throw new IllegalArgumentException(value.toString());
	}
	
	public boolean gt(Value value) {
		switch(value.getType()) {
		case INT:
		case FLOAT:
			return (_value > value.floatValue());
		}
		throw new IllegalArgumentException(value.toString());
	}
	
	public boolean eq(Value value) {
		switch(value.getType()) {
		case INT:
		case FLOAT:
			return (_value == value.floatValue());
		}
		return false;
	}

	public boolean equals(Object object) {
		if (object == this) {
			return true;
		}
		return (object instanceof Value) ? eq((Value)object) : false;
	}

	public int hashCode() {
		return Integer.valueOf(_value).hashCode();
	}
	
	public String toString() {
		return stringValue(); 
	}
	
	public String toXML() {
		StringBuilder sb = new StringBuilder();
		sb.append("<value type=\"integer\">").append(_value).append("</value>");
		return sb.toString();
	}
}
