package kr.ac.uos.ai.arbi.model.log;

public class ActionLog {

	private String actor;
	private String action;
	private long time;
	
	public ActionLog(String actor, String action) {
		this.actor = actor;
		this.action = action;
		this.time = 0;
	}
	
	public ActionLog(String actor, String action, long time) {
		this.actor = actor;
		this.action = action;
		this.time = time;
	}
	
	public String getActor() {
		return this.actor;
	}
	

	public String getAction() {
		return this.action;
	}
	
	public long getTime() {
		return this.time;
	}
	
}
