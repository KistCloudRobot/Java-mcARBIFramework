package kr.ac.uos.ai.arbi.monitor.model;

public class LTMMessage implements Message {
	private final String from;
	private final String to;
	private final LTMMessageAction action;
	private final String content;
	private final String[] contents;
	private final String[] contentTitles;

	public LTMMessage(String from, String to, LTMMessageAction action, String content) {
		this.from = from.toLowerCase();
		this.to = to.toLowerCase();
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

	public LTMMessageAction getAction() {
		return this.action;
	}

	public String[] getContent() {
		return this.contents;
	}

	public String[] getContentTitle() {
		return this.contentTitles;
	}

	public Enum<?> getType() {
		return this.action;
	}
	
	public String toString() {
		StringBuilder buffer = new StringBuilder();
		buffer.append("[").append(this.action).append("] ");
		buffer.append(this.from).append(" -> ").append(this.to);
		buffer.append("\t : ").append(this.content);
		return buffer.toString();
	}

	public static enum LTMMessageAction {
		AssertFact, RetrieveFact, UpdateFact, RetractFact, Match, Result,
		Subscribe, Unsubscribe, Notify
	}

}
