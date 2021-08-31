package kr.ac.uos.ai.arbi.model.rule.action;

import kr.ac.uos.ai.arbi.model.Expression;
import kr.ac.uos.ai.arbi.model.GeneralizedList;


public class ActionFactory {
	
	public enum ACTION {notify, stream };
	
	private ActionFactory() {
		//
	}
	
	public static Action newActionFromGL(String subscriber, GeneralizedList glAction) {
		Action action = null;
		
		ACTION actionName = ACTION.valueOf(glAction.getName());
		
		switch(actionName) {
		case notify:
			if (glAction.getExpressionsSize() == 1) {
				Expression notificationExpression = glAction.getExpression(0);
				if (notificationExpression.isGeneralizedList()) {
					action = new Notify(subscriber, notificationExpression.asGeneralizedList());
				}
			}
			break;
		default : 
			if(glAction.getExpressionsSize() == 1) {
				Expression streamExpression = glAction.getExpression(0);
				if(streamExpression.isGeneralizedList()) {
					action = new Stream(subscriber, streamExpression.asGeneralizedList());
				}
			}
			break;
		}
		return action;
	}
}
