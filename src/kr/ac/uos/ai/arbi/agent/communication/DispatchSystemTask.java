package kr.ac.uos.ai.arbi.agent.communication;

import kr.ac.uos.ai.arbi.agent.ArbiAgentMessage;

public class DispatchSystemTask implements Runnable{

	private final ArbiAgentMessageToolkit			toolkit;
	private final ArbiAgentMessage					message;
	
	public DispatchSystemTask(ArbiAgentMessageToolkit toolkit, ArbiAgentMessage message) {
		// TODO Auto-generated constructor stub
		this.toolkit = toolkit;
		this.message = message;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		String sender = message.getSender();
		String data = message.getContent();
		dispatchSystem(sender, data);
	}
	
	private void dispatchSystem(String sender, String data) {
		toolkit.onSystem(sender, data);
	}

}
