package kr.ac.uos.ai.arbi.model;

public interface Function {
	public String getName();
	public int getExpressionsSize();
	public Expression getExpression(int index);
	public Expression evaluate(Binding binding);
}
