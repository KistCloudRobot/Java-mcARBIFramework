package kr.ac.uos.ai.arbi.model.functions;

import kr.ac.uos.ai.arbi.model.Expression;
import kr.ac.uos.ai.arbi.model.Function;

public class FunctionFactory {
	private FunctionFactory() {
		//
	}
	
	public static Function newFunction(String name, Expression... expressions) {
		if (name.equals("+")) {
			return new Addition(expressions);
		} else if (name.equals("-")) {
			return new Subtraction(expressions);
		} else if (name.equals("*")) {
			return new Multiplication(expressions);
		} else if (name.equals("/")) {
			return new Division(expressions);
		} else if (name.equals("%")) {
			return new Modulo(expressions);
		} else if (name.equals("<")) {
			return new GreaterThan(expressions);
		} else if (name.equals("<=")) {
			return new GreaterThanEquals(expressions);
		} else if (name.equals("==")) {
			return new Equals(expressions);
		} else if (name.equals("!=")) {
			return new NotEquals(expressions);
		} else if (name.equals(">=")) {
			return new LessThanEquals(expressions);
		} else if (name.equals(">")) {
			return new LessThan(expressions);
		} else if (name.equals("&&")) {
			return new And(expressions);
		} else if (name.equals("||")) {
			return new Or(expressions);
		} else if (name.equals("!")) {
			return new Not(expressions);
		} else if (name.equals("?")) {
			return new Test(expressions);
		} 
		throw new IllegalArgumentException("Illegal function: " + name);
	}
}
