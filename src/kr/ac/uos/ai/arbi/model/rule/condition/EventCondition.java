package kr.ac.uos.ai.arbi.model.rule.condition;

import kr.ac.uos.ai.arbi.model.Binding;
import kr.ac.uos.ai.arbi.model.Expression;
import kr.ac.uos.ai.arbi.model.GeneralizedList;
import kr.ac.uos.ai.arbi.model.rule.condition.ConditionFactory.ConditionType;

public class EventCondition implements Condition{
	
	public EventCondition(GeneralizedList asGeneralizedList) {
		// TODO Auto-generated constructor stub
	}
	

	
	@Override
	public boolean checkCondition() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getPredicateName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Expression[] getExpressions() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public ConditionType getType() {
		return ConditionType.event;
	}





	@Override
	public Expression getEvaluatedExpression(Binding b) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
