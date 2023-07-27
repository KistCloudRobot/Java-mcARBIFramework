package kr.ac.uos.ai.arbi.agent.logger.action;

import kr.ac.uos.ai.arbi.agent.ArbiAgent;

public class LoggingActionNonAction extends LoggingAction {
	public LoggingActionNonAction(ArbiAgent agent, String actor, String actionName) {
		super(agent, actor, actionName, null);
	}

	@Override
	public Object execute(Object o){
		this.sendLog(o.toString());
		return o.toString();
	}
}
