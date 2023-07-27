package kr.ac.uos.ai.arbi.model;

public interface GeneralizedList {
	public String getName();
	public int getExpressionsSize();
	public Expression getExpression(int index);
	public GeneralizedList evaluate(Binding binding);
	public Binding unify(GeneralizedList gl, Binding binding);
}
