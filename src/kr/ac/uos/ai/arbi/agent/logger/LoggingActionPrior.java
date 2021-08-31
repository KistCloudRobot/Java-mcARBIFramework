package kr.ac.uos.ai.arbi.agent.logger;

import java.util.Base64;
import java.util.Base64.Encoder;

import kr.ac.uos.ai.arbi.agent.ArbiAgent;

public class LoggingActionPrior implements ActionBody {
	
	private ArbiAgent agent;
	private String actor;
	private String action;
	private ActionBody normalAction;

	public LoggingActionPrior(ArbiAgent agent, String actor, String action, ActionBody normalAction) {
		this.agent = agent;
		this.actor = actor;
		this.action = action;
		this.normalAction = normalAction;
	}

	public Object execute(Object o){
		this.sendLog(o.toString());
		return normalAction.execute(o);
	}
	
	private void sendLog(String content) {
		String time = String.valueOf(System.currentTimeMillis());
		
		/*
		content = content.replace("\"", "\\\"");
		content = content.replace("\t", "\\t");
		content = content.replace("\r", "\\r");
		content = content.replace("\b", "\\b");
		content = content.replace("\f", "\\f");
		content = content.replace("\n", "\\n");
		*/
		
		Encoder encoder = Base64.getEncoder();
		byte[] encodedByte = encoder.encode(content.getBytes());
		
		
		System.out.println("[System Log]	(SystemLog (actor \""+ actor +"\") "
				+ "(action \""+ action +"\") (content \""+ new String(encodedByte) +"\") (time \""+ time +"\"))");
		
		agent.system(LoggerManager.INTERACTION_MANAGER_ADDRESS,"(SystemLog (actor \""+ actor +"\") "
				+ "(action \""+ action +"\") (content \""+ new String(encodedByte) +"\") (time \""+ time +"\"))");
	}
	
	public String toString() {
		return "Logging Action Prior";
	}

}
