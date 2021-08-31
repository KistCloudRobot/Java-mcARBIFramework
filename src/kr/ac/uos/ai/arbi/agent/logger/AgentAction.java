package kr.ac.uos.ai.arbi.agent.logger;

import kr.ac.uos.ai.arbi.agent.ArbiAgent;
import kr.ac.uos.ai.arbi.ltm.DataSource;

public class AgentAction implements ActionBody {

	private String actionName;

	private ActionBody currentAction;
	private ActionBody normalAction;
	private ActionBody loggingAction;

	public AgentAction(String actionName, ActionBody normalAction) {
		this.actionName = actionName;
		this.normalAction = normalAction;
		this.currentAction = normalAction;
	}
	

	public void initLoggingFunction(ArbiAgent agent, String actor, LogTiming logTiming) {
		switch (logTiming) {
		case Prior:
			loggingAction = new LoggingActionPrior(agent, actor, this.actionName, this.normalAction);
			break;
		case Later:
			loggingAction = new LoggingActionLater(agent, actor, this.actionName, this.normalAction);
			break;
		case Both:
			loggingAction = new LoggingActionBoth(agent, actor, this.actionName, this.normalAction);
			break;
		case NonAction:
			loggingAction = new LoggingActionNonAction(agent, actor, this.actionName);
			currentAction = loggingAction;
			break;
		default:
			loggingAction = new LoggingActionPrior(agent, actor, this.actionName, this.normalAction);
			break;
		}
	}

	public String getActionName() {
		return this.actionName;
	}

	public void changeAction(boolean flag) {
		if (flag) {
			currentAction = loggingAction;
		} else {
			currentAction = normalAction;
		}
	}

	public ActionBody getFunction() {
		return currentAction;
	}

	@Override
	public Object execute(Object o) {

		// TODO Auto-generated method stub
		return currentAction.execute(o);
	}

}
