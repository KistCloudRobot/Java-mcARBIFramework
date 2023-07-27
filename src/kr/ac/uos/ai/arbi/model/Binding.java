package kr.ac.uos.ai.arbi.model;

public interface Binding {
	public Binding copy(Binding binding);
	public String[] getBoundedVariableNames();
	public void bind(Variable variable, Expression expression);
	public void bind(String variableName, Expression expression);
	public void unbind(Variable variable);
	public void unbind(String variableName);
	public void unbind(Expression expression);
	public void unbind(GeneralizedList gl);
	public void unbind(Function function);
	public Expression retrieve(Variable variable);
	public Expression retrieve(String variableName);
}
