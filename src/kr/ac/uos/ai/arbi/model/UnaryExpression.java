package kr.ac.uos.ai.arbi.model;

public class UnaryExpression implements Expression{

	public UnaryExpression(String string, Expression expr) {
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean isValue() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isVariable() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isFunction() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isGeneralizedList() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Value asValue() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Variable asVariable() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Function asFunction() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public GeneralizedList asGeneralizedList() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Expression evaluate(Binding binding) {
		// TODO Auto-generated method stub
		return null;
	}

}
