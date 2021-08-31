package test.stability;

import java.util.LinkedList;

public class AgentStabilityTestManager {
	private long startTime = 0;
	public static void main(String[] ar) {
		new AgentStabilityTestManager();
	}
	
	public void onFinish() {
		System.out.println("finished : " + (System.currentTimeMillis() - startTime));
	}
	
	public AgentStabilityTestManager() {
		LinkedList<MassSendAgent> sendList = new LinkedList<MassSendAgent>();
		LinkedList<MassReceiveAgent> receiveAgent = new LinkedList<MassReceiveAgent>();
		for(int i = 0; i < 10; i++) {
			MassSendAgent sAgent = new MassSendAgent(i, this);
			MassReceiveAgent rAgent = new MassReceiveAgent(i);
			
			sendList.add(sAgent);
			receiveAgent.add(rAgent);
			
		}
		
		LinkedList<Thread> tList = new LinkedList<Thread>();
		for(int i = 0; i < 10; i++) {
			Thread t = new Thread(sendList.get(i));
			tList.add(t);
			
		}
		
		startTime = System.currentTimeMillis();
		for(int i = 0; i < 10; i++) {
			tList.get(i).start();
		}
	}
}
