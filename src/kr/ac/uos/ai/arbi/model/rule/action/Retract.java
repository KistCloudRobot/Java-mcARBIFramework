package kr.ac.uos.ai.arbi.model.rule.action;

import kr.ac.uos.ai.arbi.model.Binding;
import kr.ac.uos.ai.arbi.model.GeneralizedList;

public class Retract implements Action{
	private final String 							subscriber;
	private final GeneralizedList					generalizedList;
	private Binding									bind;
	
	public Retract(String subscriber, GeneralizedList asGeneralizedList) {
		this.subscriber = subscriber;
		this.generalizedList = asGeneralizedList;
		
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
		
		return generalizedList;
	}


}
