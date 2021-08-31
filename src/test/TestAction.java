package test;

import kr.ac.uos.ai.arbi.agent.logger.ActionBody;

public class TestAction implements ActionBody{

	@Override
	public Object execute(Object o) {
		System.out.println("test");
		return null;
	}

}
