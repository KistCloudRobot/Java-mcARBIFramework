package kr.ac.uos.ai.arbi.model.rule.action;

import java.util.HashMap;

import kr.ac.uos.ai.arbi.model.Binding;
import kr.ac.uos.ai.arbi.model.BindingFactory;
import kr.ac.uos.ai.arbi.model.GLFactory;
import kr.ac.uos.ai.arbi.model.GeneralizedList;



public class Stream implements Action {
	private final String 							subscriber;
	private final GeneralizedList					streamForm;
	private Binding									bind;
	
	public Stream(String subscriber, GeneralizedList streamForm) {
		this.subscriber = subscriber;
		this.streamForm 		= streamForm;
	}

	@Override
	public String getSubscriber() {
		return subscriber;
	}

	@Override
	public String toActionContent() {
		GeneralizedList gl = streamForm.evaluate(bind);
		String content = gl.toString();
		return content;
	}

	@Override
	public void bind(Binding b) {
		this.bind = b;
	}
	
}
