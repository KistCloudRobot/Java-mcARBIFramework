package kr.ac.uos.ai.arbi.model;

public interface Value {
	public static enum Type { INT, FLOAT, STRING, UNDEFINED };
	public static final Value UNDEFINED 		= GLFactory.getUndefinedValue();
	public static final Value TRUE 				= GLFactory.newValue(1);
	public static final Value FALSE 			= GLFactory.newValue(0);
	
	public Type 	getType();
	public int 		intValue();
	public float 	floatValue();
	public String 	stringValue();
	public boolean 	booleanValue();
	
	public Value	add(Value value);
	public Value 	sub(Value value);
	public Value	mul(Value value);
	public Value 	div(Value value);
	public Value 	mod(Value value);
	
	public boolean	lt(Value value);
	public boolean	gt(Value value);
	public boolean	eq(Value value);
	
}
