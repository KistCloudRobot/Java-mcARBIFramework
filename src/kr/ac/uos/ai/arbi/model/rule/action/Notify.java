package kr.ac.uos.ai.arbi.model.rule.action;

import java.util.HashMap;

import kr.ac.uos.ai.arbi.model.Binding;
import kr.ac.uos.ai.arbi.model.BindingFactory;
import kr.ac.uos.ai.arbi.model.GLFactory;
import kr.ac.uos.ai.arbi.model.GeneralizedList;



public class Notify implements Action {
	private final String 							subscriber;
	private final GeneralizedList					_notificationForm;
	private Binding									bind;
	
	public Notify(String subscriber, GeneralizedList notificationForm) {
		this.subscriber = subscriber;
		_notificationForm 		= notificationForm;
	}

	@Override
	public String getSubscriber() {
		return subscriber;
	}

	@Override
	public String toActionContent() {
		GeneralizedList gl = _notificationForm.evaluate(bind);
		String content = gl.toString();
		return content;
	}

	@Override
	public void bind(Binding b) {
		this.bind = b;
	}
	
}
