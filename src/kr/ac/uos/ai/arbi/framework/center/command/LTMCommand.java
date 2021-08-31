package kr.ac.uos.ai.arbi.framework.center.command;

import kr.ac.uos.ai.arbi.framework.center.LTMService;

public abstract class LTMCommand {
	public abstract String deploy(LTMService service, String author, String fact);
}
