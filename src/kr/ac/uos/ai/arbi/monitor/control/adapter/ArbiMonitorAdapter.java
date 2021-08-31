package kr.ac.uos.ai.arbi.monitor.control.adapter;

import javax.jms.MessageListener;

public interface ArbiMonitorAdapter {
	public boolean connect(String brokerURL, String monitorID, String interactionManagerURL);
	public void send(String message);

}
