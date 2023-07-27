package kr.ac.uos.ai.arbi.agent.logger.action;

import java.util.Base64;
import java.util.Base64.Encoder;

import kr.ac.uos.ai.arbi.agent.ArbiAgent;
import kr.ac.uos.ai.arbi.agent.logger.LoggerManager;

public abstract class LoggingAction implements ActionBody {
	private String actor;
	private String actionName;
	protected ActionBody action;
	private ArbiAgent agent;
	
	public LoggingAction(ArbiAgent agent, String actor, String actionName, ActionBody action) {
		this.agent = agent;
		this.actor = actor;
		this.actionName = actionName;
		this.action = action;
	}
	
	protected 	void sendLog(String content) {
		String time = String.valueOf(System.currentTimeMillis());
		
		Encoder encoder = Base64.getEncoder();
		byte[] encodedByte = encoder.encode(content.getBytes());

		agent.system(LoggerManager.INTERACTION_MANAGER_ADDRESS,"(SystemLog (actor \""+ actor +"\") "
				+ "(action \""+ actionName +"\") (content \""+   new String(encodedByte) +"\") (time \""+ time +"\"))");
		
	}
}
