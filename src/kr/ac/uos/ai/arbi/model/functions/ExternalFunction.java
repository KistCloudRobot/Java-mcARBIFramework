package kr.ac.uos.ai.arbi.model.functions;

import java.util.ArrayList;

import javax.servlet.jsp.jstl.core.Config;

import kr.ac.uos.ai.arbi.agent.logger.LoggerManager;
import kr.ac.uos.ai.arbi.agent.logger.action.AgentAction;
import kr.ac.uos.ai.arbi.model.Binding;
import kr.ac.uos.ai.arbi.model.Expression;

public class ExternalFunction extends AbstractFunction {
	
	public ExternalFunction(String name, Expression[] expressions) {
		super(name, expressions);
	}

	
	
	@Override
	public Expression evaluate(Binding binding) {
		AgentAction action = LoggerManager.getInstance().getAction(this.getName());
		if(action == null) {
			return null;
		}
		Object[] argumentList = new Object[this._expressions.length];
		for(int i = 0; i < this._expressions.length;i++) {
			Object argument = this._expressions[i].evaluate(binding);
			argumentList[i] = argument;
		}
		
		Expression result = (Expression)action.execute(argumentList);
		
		return result;
		
	}

}
