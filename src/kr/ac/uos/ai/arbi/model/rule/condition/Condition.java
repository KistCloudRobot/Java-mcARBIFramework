package kr.ac.uos.ai.arbi.model.rule.condition;

import kr.ac.uos.ai.arbi.model.Binding;
import kr.ac.uos.ai.arbi.model.Expression;
import kr.ac.uos.ai.arbi.model.GeneralizedList;
import kr.ac.uos.ai.arbi.model.rule.condition.ConditionFactory.ConditionType;


public interface Condition {
	public boolean checkCondition();
	public String getPredicateName();
	public Expression[] getExpressions();
	public ConditionType getType();
	public Expression getEvaluatedExpression(Binding b);
	
}
