package kr.ac.uos.ai.arbi.monitor.model;

public abstract interface Message {
	
	public static final Message[] NULL_ARRAY = new Message[0];

	public abstract Enum<?> getType();

	public abstract String getFrom();

	public abstract String getTo();

	public abstract String[] getContent();

	public abstract String[] getContentTitle();
}
