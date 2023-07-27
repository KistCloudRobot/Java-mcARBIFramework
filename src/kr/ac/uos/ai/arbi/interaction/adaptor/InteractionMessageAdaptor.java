package kr.ac.uos.ai.arbi.interaction.adaptor;

public interface InteractionMessageAdaptor {
	public void send(String monitorID, String message);
	public void sendStatus(String status);
	public void close();
}
