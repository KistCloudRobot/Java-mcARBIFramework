package kr.ac.uos.ai.arbi.agent.logger;

import java.util.Base64;
import java.util.Base64.Encoder;

import kr.ac.uos.ai.arbi.agent.ArbiAgent;
import kr.ac.uos.ai.arbi.ltm.DataSource;

public class LoggingActionNonAction implements ActionBody {
	
	private ArbiAgent agent;
	private String actor;
	private String action;
	
	public LoggingActionNonAction(ArbiAgent agent, String actor, String action) {
		this.agent = agent;
		this.actor = actor;
		this.action = action;
	}

	public Object execute(Object o){
		this.sendLog(o.toString());
		return o.toString();
	}
	
	private void sendLog(String content) {
		String time = String.valueOf(System.currentTimeMillis());
		
		Encoder encoder = Base64.getEncoder();
		byte[] encodedByte = encoder.encode(content.getBytes());
		
		
		System.out.println("[System Log]	(SystemLog (actor \""+ actor +"\")"
				+ "(action \""+ action +"\") (content \""+ new String(encodedByte) +"\") (time \""+ time +"\"))");
		System.out.println(agent);
		agent.system(LoggerManager.INTERACTION_MANAGER_ADDRESS,"(SystemLog (actor \""+ actor +"\") "
				+ "(action \""+ action +"\") (content \""+  new String(encodedByte) +"\") (time \""+ time +"\"))");
		
	}

}
