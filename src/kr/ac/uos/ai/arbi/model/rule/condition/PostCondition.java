package kr.ac.uos.ai.arbi.model.rule.condition;

import java.util.LinkedList;
import java.util.List;

import kr.ac.uos.ai.arbi.model.Binding;
import kr.ac.uos.ai.arbi.model.Expression;
import kr.ac.uos.ai.arbi.model.GLFactory;
import kr.ac.uos.ai.arbi.model.GeneralizedList;
import kr.ac.uos.ai.arbi.model.rule.condition.ConditionFactory.ConditionType;

class PostCondition implements Condition {

	private GeneralizedList condition = null; 
	
	public PostCondition(GeneralizedList glCondition) {
		this.condition = glCondition;
	}

	@Override
	public boolean checkCondition() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getPredicateName() {
		
		return this.condition.getName();
	}

	@Override
	public Expression[] getExpressions() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public ConditionType getType() {
		return ConditionType.post;
	}

	public Expression getEvaluatedExpression(Binding b) {

		 return GLFactory.newExpression(condition);
	}
	
}
