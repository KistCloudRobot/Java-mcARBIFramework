package kr.ac.uos.ai.arbi.model.rule.condition;

import kr.ac.uos.ai.arbi.model.Expression;
import kr.ac.uos.ai.arbi.model.rule.condition.ConditionFactory.Type;


public interface Condition {
	public boolean checkCondition();
	public String getPredicateName();
	public Expression[] getExpressions();
}
