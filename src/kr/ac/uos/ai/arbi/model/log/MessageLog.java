package kr.ac.uos.ai.arbi.model.log;


public class MessageLog {

	private String type;
	private String action;
	
	public MessageLog(String type, String action) {
		this.type = type;
		this.action = action;
	}
	
	public String getType() {
		return this.type;
	}
	
	public String getAction() {
		return this.action;
	}
	
}
