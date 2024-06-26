package kr.ac.uos.ai.arbi.agent.communication.dispatchTask;

import kr.ac.uos.ai.arbi.agent.ArbiAgentMessage;
import kr.ac.uos.ai.arbi.agent.communication.ArbiAgentMessageToolkit;

public class DispatchReleaseStreamTask implements Runnable{

	private final ArbiAgentMessageToolkit		toolkit;
	private final ArbiAgentMessage				message;
	
	public DispatchReleaseStreamTask(ArbiAgentMessageToolkit toolkit, ArbiAgentMessage message) {
		this.toolkit = toolkit;
		this.message = message;
	}
	
	@Override
	public void run() {
		dispatchReleaseStream();
	}

	private void dispatchReleaseStream() {
		String sender = message.getSender();
		String streamID = message.getContent();
		toolkit.onReleaseStream(sender, streamID);
	}
}
