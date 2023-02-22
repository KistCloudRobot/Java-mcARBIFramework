package kr.ac.uos.ai.arbi.agent.logger.action;

import kr.ac.uos.ai.arbi.agent.ArbiAgent;

public class LoggingActionLater extends LoggingAction {
	public LoggingActionLater(ArbiAgent agent, String actor, String actionName, ActionBody action) {
		super(agent, actor, actionName, action);
	}

	@Override
	public Object execute(Object o){
		Object object =  this.action.execute(o);
		this.sendLog(o.toString());
		return object;
	}
}
