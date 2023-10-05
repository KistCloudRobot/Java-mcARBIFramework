package kr.ac.uos.ai.arbi.model.rule.action;

import kr.ac.uos.ai.arbi.model.Expression;
import kr.ac.uos.ai.arbi.model.GeneralizedList;


public class ActionFactory {
	
	public enum ACTION {notify,post,retract,update ,stream,bind };
	
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
		case bind:
			if (glAction.getExpressionsSize() == 1) {
				Expression notificationExpression = glAction.getExpression(0);
				if (notificationExpression.isGeneralizedList()) {
					action = new BindResult(subscriber, notificationExpression.asGeneralizedList());
				}
			}
			break;
		case post:
			if (glAction.getExpressionsSize() == 1) {
				Expression notificationExpression = glAction.getExpression(0);
				if (notificationExpression.isGeneralizedList()) {
					action = new Post(subscriber, notificationExpression.asGeneralizedList());
				}
			}
			break;
		case retract:
			if (glAction.getExpressionsSize() == 1) {
				Expression notificationExpression = glAction.getExpression(0);
				if (notificationExpression.isGeneralizedList()) {
					action = new Retract(subscriber, notificationExpression.asGeneralizedList());
				}
			}
			break;
		case update:
			if(glAction.getExpressionsSize() == 2) {
				Expression formerGLExpression = glAction.getExpression(0);
				Expression newGLExpression = glAction.getExpression(1);
				if(formerGLExpression.isGeneralizedList() == true && newGLExpression.isGeneralizedList() == true) {
					action = new Update(formerGLExpression,newGLExpression);
				}
				break;
			}
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
