package kr.ac.uos.ai.arbi.framework.center.command;

import kr.ac.uos.ai.arbi.framework.center.LTMServiceInterface;

public class UpdateFactCommand extends LTMCommand {

	@Override
	public String deploy(LTMServiceInterface service, String author, String fact) {
		return service.updateFact(author, fact);
	}

}
