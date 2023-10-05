package kr.ac.uos.ai.arbi.model.rule.action;

import kr.ac.uos.ai.arbi.model.Binding;
import kr.ac.uos.ai.arbi.model.GeneralizedList;

public class Post implements Action {
	private final String 							subscriber;
	private final GeneralizedList					generalizedList;
	private Binding									bind;
	
	public Post(String subscriber, GeneralizedList generalizedList) {
		this.subscriber = subscriber;
		this.generalizedList = generalizedList;
	}

	@Override
	public String getSubscriber() {
		return this.subscriber;
	}

	@Override
	public String toActionContent() {
		String result = "";
		if(bind != null) {
			result = generalizedList.evaluate(bind).toString();
		}
		return result;
	}

	@Override
	public void bind(Binding b) {
		this.bind = b;
	}

	public GeneralizedList getBindedGL() {
		
		return generalizedList.evaluate(bind);
	}

}

