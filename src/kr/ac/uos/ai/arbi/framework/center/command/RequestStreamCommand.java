package kr.ac.uos.ai.arbi.framework.center.command;

import kr.ac.uos.ai.arbi.framework.center.LTMServiceInterface;
import kr.ac.uos.ai.arbi.framework.center.RedisLTMService;

public class RequestStreamCommand extends LTMCommand {



	@Override
	public String deploy(LTMServiceInterface service, String author, String fact) {
		return service.requestStream(author, fact);
	}

}
