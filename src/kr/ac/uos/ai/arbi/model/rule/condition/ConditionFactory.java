package kr.ac.uos.ai.arbi.model.rule.condition;

import kr.ac.uos.ai.arbi.model.Expression;
import kr.ac.uos.ai.arbi.model.GLFactory;
import kr.ac.uos.ai.arbi.model.GeneralizedList;
import kr.ac.uos.ai.arbi.model.parser.ParseException;



public class ConditionFactory {
	
	public enum Type {fact, expression, post, event, retracted};
	
	private ConditionFactory() {
		//
	}
	
	public static Condition newConditionFromGL(GeneralizedList glCondition) {
		Condition condition = null;
		
		Type condionName = Type.valueOf(glCondition.getName());
		switch(condionName) {
		case fact:
			if (glCondition.getExpressionsSize() == 1) {
				Expression predicateExpression = glCondition.getExpression(0);
				if (predicateExpression.isGeneralizedList()) {
					condition = new FactCondition(predicateExpression.asGeneralizedList());
				}
			}
			break;
		case expression:
			if (glCondition.getExpressionsSize() == 1) {
				condition = new ExpressionCondition(glCondition.getExpression(0));
			}
			break;
		case post:
			if (glCondition.getExpressionsSize() == 1) {
				Expression eventExpression = glCondition.getExpression(0);
				if (eventExpression.isGeneralizedList()) {
					condition = new PostCondition(eventExpression.asGeneralizedList());
				}
			}
			break;
			
		case event:
			if (glCondition.getExpressionsSize() == 1) {
				Expression eventExpression = glCondition.getExpression(0);
				if (eventExpression.isGeneralizedList()) {
					condition = new EventCondition(eventExpression.asGeneralizedList());
				}
			}
			break;
		case retracted:
			if (glCondition.getExpressionsSize() == 1) {
				Expression retractedExpression = glCondition.getExpression(0);
				if (retractedExpression.isGeneralizedList()) {
					condition = new RetractedCondition(retractedExpression.asGeneralizedList());
				}
			}
			break;
		default : break;
		}

		return condition;
	}

	public static Condition newConditionFromGLString(String event) {
		GeneralizedList gl = null;
		try {
			gl = GLFactory.newGLFromGLString(event);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return newConditionFromGL(gl);
	}
	
	public static String toFactString(Condition con) {
		String conString = "(fact ("+con.getPredicateName();
		for(Expression exp : con.getExpressions()) {
			
			conString += " " +exp.toString();
		}
		conString += "))";
		return conString;
	}

}
