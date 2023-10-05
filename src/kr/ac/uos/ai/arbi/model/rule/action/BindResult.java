package kr.ac.uos.ai.arbi.model.rule.action;

import kr.ac.uos.ai.arbi.model.Binding;
import kr.ac.uos.ai.arbi.model.GeneralizedList;

public class BindResult implements Action {
	private final String 							subscriber;
	private final GeneralizedList					generalizedList;
	private Binding									bind;
	
	public BindResult(String subscriber, GeneralizedList generalizedList) {
		this.subscriber = subscriber;
		this.generalizedList = generalizedList;
	}
	@Override
	public String getSubscriber() {
		return subscriber;
	}

	@Override
	public String toActionContent() {
		
		return generalizedList.evaluate(bind).toString();
	}

	@Override
	public void bind(Binding b) {
		this.bind = b;
		
	}

}
