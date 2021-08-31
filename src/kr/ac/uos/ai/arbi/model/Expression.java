package kr.ac.uos.ai.arbi.model;


public interface Expression {
	public static final Expression UNDEFINED	= GLFactory.getUndefinedExpression();
	public static final Expression TRUE			= GLFactory.newExpression(Value.TRUE);
	public static final Expression FALSE		= GLFactory.newExpression(Value.FALSE);
	
	public boolean 			isValue();
	public boolean 			isVariable();
	public boolean			isFunction();
	public boolean			isGeneralizedList();
	public Value 			asValue();
	public Variable 		asVariable();
	public Function			asFunction();
	public GeneralizedList	asGeneralizedList();
	public Expression		evaluate(Binding binding);
}
