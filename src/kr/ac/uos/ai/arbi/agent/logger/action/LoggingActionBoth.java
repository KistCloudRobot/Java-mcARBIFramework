package kr.ac.uos.ai.arbi.agent.logger.action;

import kr.ac.uos.ai.arbi.agent.ArbiAgent;

public class LoggingActionBoth extends LoggingAction {
	public LoggingActionBoth(ArbiAgent agent, String actor, String actionName, ActionBody action) {
		super(agent, actor, actionName, action);
	}

	@Override
	public Object execute(Object o){
		this.sendLog(o.toString());
		Object object =  this.action.execute(o);
		this.sendLog(o.toString());
		return object;
	}
}
