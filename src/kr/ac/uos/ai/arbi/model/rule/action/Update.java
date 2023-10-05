package kr.ac.uos.ai.arbi.model.rule.action;

import kr.ac.uos.ai.arbi.model.Binding;
import kr.ac.uos.ai.arbi.model.Expression;
import kr.ac.uos.ai.arbi.model.GeneralizedList;

public class Update implements Action {

	private GeneralizedList formerGLExpression = null; 
	private GeneralizedList newGLExpression = null;
	private Binding b;
	
	public Update(Expression formerGLExpression, Expression newGLExpression) {
		this.formerGLExpression = formerGLExpression.asGeneralizedList();
		this.newGLExpression = newGLExpression.asGeneralizedList();
	}

	@Override
	public String getSubscriber() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String toActionContent() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void bind(Binding b) {
		// TODO Auto-generated method stub
		
	}
	
	public GeneralizedList getFormerGLExpression() {
		return formerGLExpression.evaluate(b);
	}
	
	public GeneralizedList getNewGLExpression() {
		return newGLExpression.evaluate(b);
	}

}
