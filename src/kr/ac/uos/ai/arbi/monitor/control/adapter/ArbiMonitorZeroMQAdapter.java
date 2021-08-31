package kr.ac.uos.ai.arbi.monitor.control.adapter;

import org.zeromq.ZMQ;
import org.zeromq.ZMQ.Context;
import org.zeromq.ZMQ.Socket;

import kr.ac.uos.ai.arbi.monitor.ArbiFrameworkMonitor;

public class ArbiMonitorZeroMQAdapter implements ArbiMonitorAdapter {
	
	private String arbiAgentURI;

	private MessageRecvTask messageRecvTask;

	private Context zmqContext;
	private Socket zmqProducer;
	private Socket zmqConsumer;

	private ArbiFrameworkMonitor monitor;
	
	public ArbiMonitorZeroMQAdapter(ArbiFrameworkMonitor monitor) {
		this.monitor = monitor;
	}
	
	@Override
	public boolean connect(String brokerURL, String monitorID, String interactionManagerURL) {
		this.arbiAgentURI = monitorID;
		zmqContext = ZMQ.context(1);
		

		zmqProducer = zmqContext.socket(ZMQ.DEALER);
		zmqProducer.connect(brokerURL);
		zmqProducer.setIdentity((arbiAgentURI + "/message").getBytes());
		
		
		zmqConsumer = zmqContext.socket(ZMQ.DEALER);
		zmqConsumer.connect(brokerURL);
		zmqConsumer.setIdentity((arbiAgentURI).getBytes());

		messageRecvTask = new MessageRecvTask();
		messageRecvTask.start();
		
		System.out.println("Broker Connected : " + brokerURL);
		return true;
	}

	@Override
	public void send(String message) {
		zmqProducer.sendMore("");
		zmqProducer.send(message);
	}
	
	public void close() {

		if (zmqProducer != null)
			zmqProducer.close();

		if (zmqConsumer != null)
			zmqConsumer.close();

		if (zmqContext != null)
			zmqContext.term();

		messageRecvTask.stop();
	}


	private class MessageRecvTask extends Thread {

		@Override
		public void run() {

			while (true) {
				zmqConsumer.recvStr();
				String message = zmqConsumer.recvStr();
				System.out.println("on Message : " + message);
				monitor.onLogMessage(message);
			}
		}

	}

}
