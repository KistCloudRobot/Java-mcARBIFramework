package kr.ac.uos.ai.arbi.monitor.control.dispatch;

import javax.jms.MapMessage;
import javax.jms.TextMessage;

public interface DispatchTask {
	public abstract void doTask(String message);
}
