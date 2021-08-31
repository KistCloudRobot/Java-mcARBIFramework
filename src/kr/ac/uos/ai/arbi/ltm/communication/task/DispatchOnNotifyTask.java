package kr.ac.uos.ai.arbi.ltm.communication.task;

import kr.ac.uos.ai.arbi.ltm.communication.DataCenterInterfaceToolkit;
import kr.ac.uos.ai.arbi.ltm.communication.message.LTMMessage;

public class DispatchOnNotifyTask implements Runnable {
	private final DataCenterInterfaceToolkit			toolkit;
	private final LTMMessage							message;
	
	public DispatchOnNotifyTask(DataCenterInterfaceToolkit toolkit, LTMMessage message) {
		this.toolkit = toolkit;
		this.message = message;
	}
	
	@Override
	public void run() {
		String content = message.getContent();
		dispatchOnNotify(content);
	}
	
	private void dispatchOnNotify(String content) {
		toolkit.onNotify(content);
	}
	
}
