package kr.ac.uos.ai.arbi.monitor.model;

public class ArbiAgentMessage implements Message{
	
	private final String from;
	private final String to;
	private AgentMessageAction action;
	private final String content;
	private final String[] contents;
	private final String[] contentTitles;
	
	public ArbiAgentMessage(String from, String to, AgentMessageAction action, String content) {
		this.from = from.toLowerCase();
		this.to= to.toLowerCase();
		this.action = action;
		this.content = content;
		this.contents = new String[] { this.content };
		this.contentTitles = new String[] { "Content" };
	}
	
	public String getFrom() {
		return this.from;
	}

	public String getTo() {
		return this.to;
	}

	public String[] getContent() {
		return this.contents;
	}

	public AgentMessageAction getAtion() {
		return this.action;
	}

	public Enum<?> getType() {
		return this.action;
	}

	public String[] getContentTitle() {
		return this.contentTitles;
	}

	public String toString() {
		StringBuilder buffer = new StringBuilder();
		buffer.append("[").append(this.action).append("] ");
		buffer.append(this.from).append(" -> ").append(this.to);
		buffer.append("\t : ").append(this.content);
		return buffer.toString();
	}
	
	public enum AgentMessageAction {
		Request, Query, Inform, Response, Subscribe, Unsubscribe, Notify, System
		
	}

}
