package kr.ac.uos.ai.arbi.model.functions;

import kr.ac.uos.ai.arbi.model.Expression;
import kr.ac.uos.ai.arbi.model.Function;

public class FunctionFactory {
	private FunctionFactory() {
		//
	}
	
	public static Function newFunction(String name, Expression... expressions) {
		if (name.equals("add")) {
			return new Addition(expressions);
		} else if (name.equals("sub")) {
			return new Subtraction(expressions);
		} else if (name.equals("mul")) {
			return new Multiplication(expressions);
		} else if (name.equals("div")) {
			return new Division(expressions);
		} else if (name.equals("mod")) {
			return new Modulo(expressions);
		} else if (name.equals("gt")) {
			return new GreaterThan(expressions);
		} else if (name.equals("ge")) {
			return new GreaterThanEquals(expressions);
		} else if (name.equals("eq")) {
			return new Equals(expressions);
		} else if (name.equals("ne")) {
			return new NotEquals(expressions);
		} else if (name.equals("le")) {
			return new LessThanEquals(expressions);
		} else if (name.equals("lt")) {
			return new LessThan(expressions);
		} else if (name.equals("and")) {
			return new And(expressions);
		} else if (name.equals("or")) {
			return new Or(expressions);
		} else if (name.equals("not")) {
			return new Not(expressions);
		} else if (name.equals("test")) {
			return new Test(expressions);
		} 
		throw new IllegalArgumentException("Illegal function: " + name);
	}
}
