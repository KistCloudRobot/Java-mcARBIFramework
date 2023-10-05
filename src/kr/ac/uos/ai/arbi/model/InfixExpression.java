package kr.ac.uos.ai.arbi.model;

import java.util.List;

public class InfixExpression implements Expression {

	private String type = "";
	private Expression lefthandExpression = null;
	private Expression righthandExpression = null;

	public InfixExpression(String image, List<Expression> exprList) {
		this.type = image;

		try {

			if (exprList.size() > 1) {
				lefthandExpression = exprList.get(0);
				righthandExpression = exprList.get(1);
			} else {
				throw new Exception();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public InfixExpression(InfixExpression ie) {
		this.type = ie.type;
		this.lefthandExpression = ie.lefthandExpression;
		this.righthandExpression = ie.righthandExpression;
	}

	@Override
	public boolean isValue() {
		return false;
	}

	@Override
	public boolean isVariable() {
		return false;
	}

	@Override
	public boolean isFunction() {
		return false;
	}

	@Override
	public boolean isGeneralizedList() {
		return false;
	}

	@Override
	public Value asValue() {
		return null;
	}

	@Override
	public Variable asVariable() {
		return null;
	}

	@Override
	public Function asFunction() {
		return null;
	}

	@Override
	public GeneralizedList asGeneralizedList() {
		return null;
	}

	@Override
	public Expression evaluate(Binding binding) {
		// TODO Auto-generated method stub
		return null;
	}

}
