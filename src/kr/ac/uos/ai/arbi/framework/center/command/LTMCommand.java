package kr.ac.uos.ai.arbi.framework.center.command;

import kr.ac.uos.ai.arbi.framework.center.LTMServiceInterface;
import kr.ac.uos.ai.arbi.framework.center.RedisLTMService;

public abstract class LTMCommand {
	public abstract String deploy(LTMServiceInterface service, String author, String fact);
}
