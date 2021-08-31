package kr.ac.uos.ai.arbi.model.rule;

import kr.ac.uos.ai.arbi.model.Expression;
import kr.ac.uos.ai.arbi.model.GLFactory;
import kr.ac.uos.ai.arbi.model.GeneralizedList;
import kr.ac.uos.ai.arbi.model.parser.ParseException;
import kr.ac.uos.ai.arbi.model.rule.action.Action;
import kr.ac.uos.ai.arbi.model.rule.action.ActionFactory;
import kr.ac.uos.ai.arbi.model.rule.condition.Condition;
import kr.ac.uos.ai.arbi.model.rule.condition.ConditionFactory;


public class RuleFactory {
	
	private RuleFactory() {
		//
	}
	
	private static int findDelimiterIndex(GeneralizedList glRule) {
		int index = -1;
		for (int i=0, n=glRule.getExpressionsSize(); i<n; i++) {
			Expression expression = glRule.getExpression(i);
			if (expression.isValue()) {
				String value = expression.asValue().stringValue();
				if (value.equals("-->")) {
					index = i;
					break;
				}
			}
		}
		return index;
	}
	
	public static Rule newRuleFromRuleString(String subscriber, String rule){
		try {
			return newRuleFromGL(subscriber, GLFactory.newGLFromGLString(rule));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static Rule newRuleFromGL(String subscriber, GeneralizedList glRule) {
		if (!glRule.getName().equals("rule")) {
			return null;
		}
		int delimiterIndex = findDelimiterIndex(glRule);
		if (delimiterIndex == -1) {
			return null;
		}
		int conditionSize = delimiterIndex;
		Condition[] conditions = new Condition[conditionSize];
		for (int i=0; i<conditionSize; i++) {
			Expression conditionExpression = glRule.getExpression(i);
			if (!conditionExpression.isGeneralizedList()) {
				return null;
			}
			conditions[i] = ConditionFactory.newConditionFromGL(conditionExpression.asGeneralizedList());
			if(conditions[i] == null) {
				return null;
			}
		}
		int actionSize = glRule.getExpressionsSize() - conditionSize - 1;
		Action[] actions = new Action[actionSize];
		for (int i=0; i<actionSize; i++) {
			Expression actionExpression = glRule.getExpression(delimiterIndex + i + 1);
			if (!actionExpression.isGeneralizedList()) {
				return null;
			}
			actions[i] = ActionFactory.newActionFromGL(subscriber, actionExpression.asGeneralizedList());
			if(actions[i] == null) {
				return null;
			}
		}
		return new Rule(conditions, actions);
	}
}
