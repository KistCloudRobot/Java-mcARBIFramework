package kr.ac.uos.ai.arbi.agent.datastream;

import org.zeromq.ZMQ;

import kr.ac.uos.ai.arbi.agent.ArbiAgent;

public class StreamReceiver extends Thread {
	
	private ArbiAgent agent;
	private ZMQ.Socket socket;
	
	public StreamReceiver(ArbiAgent agent, String url) {
		this.agent = agent;
		socket = ZMQ.context(1).socket(ZMQ.PULL);
		socket.connect(url);
		this.start();
	}
	
	@Override
	public void run() {
		while(true) {
			String data = socket.recvStr();
			agent.onStream(data);
		}
	}
}
