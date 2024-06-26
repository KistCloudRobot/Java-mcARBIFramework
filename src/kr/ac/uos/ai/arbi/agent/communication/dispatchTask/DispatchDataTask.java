package kr.ac.uos.ai.arbi.agent.communication.dispatchTask;

import java.util.Map;

import kr.ac.uos.ai.arbi.agent.ArbiAgentMessage;
import kr.ac.uos.ai.arbi.agent.communication.ArbiAgentMessageToolkit;


public class DispatchDataTask implements Runnable {
	
	private final ArbiAgentMessageToolkit				toolkit;
	private final ArbiAgentMessage						message;

	public DispatchDataTask(ArbiAgentMessageToolkit toolkit, ArbiAgentMessage message) {
		this.toolkit = toolkit;
		this.message = message;
	}
	
	public void run() {
		String sender = message.getSender();
		String data = message.getContent();
		dispatchData(sender, data);
	}
	
	private void dispatchData(String sender, String data) {
		toolkit.onData(sender, data);
	}
	
}
