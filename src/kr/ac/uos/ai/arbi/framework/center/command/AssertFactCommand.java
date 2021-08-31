package kr.ac.uos.ai.arbi.framework.center.command;

import kr.ac.uos.ai.arbi.framework.center.LTMService;

public class AssertFactCommand extends LTMCommand{


	@Override
	public String deploy(LTMService service, String author, String fact) {
		System.out.println("AssertFactCommand: Before service.assertFact");
		return  service.assertFact(author, fact);
	}

}
