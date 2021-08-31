package kr.ac.uos.ai.arbi.framework.center.command;

import kr.ac.uos.ai.arbi.framework.center.LTMService;

public class SubscribeCommand extends LTMCommand {

	@Override
	public String deploy(LTMService service, String author, String fact) {
		return  service.subscribe(author, fact);
	}


}
