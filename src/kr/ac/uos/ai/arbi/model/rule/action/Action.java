package kr.ac.uos.ai.arbi.model.rule.action;

import java.util.HashMap;

import kr.ac.uos.ai.arbi.model.Binding;


public interface Action {
	public String getSubscriber();
	public String toActionContent();
	public void bind(Binding b);
}
