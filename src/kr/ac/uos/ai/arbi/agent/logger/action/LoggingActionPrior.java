package kr.ac.uos.ai.arbi.agent.logger.action;

import kr.ac.uos.ai.arbi.agent.ArbiAgent;

public class LoggingActionPrior extends LoggingAction {
	public LoggingActionPrior(ArbiAgent agent, String actor, String actionName, ActionBody action) {
		super(agent, actor, actionName, action);
	}

	@Override
	public Object execute(Object o){
		this.sendLog(o.toString());
		Object object =  this.action.execute(o);
		return object;
	}
}
