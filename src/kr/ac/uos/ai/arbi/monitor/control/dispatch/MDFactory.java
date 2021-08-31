package kr.ac.uos.ai.arbi.monitor.control.dispatch;


public class MDFactory {
	public static MessageDispatcher getMessageProcessor() {
		return new MessageDispatcherAll();
	}
}
