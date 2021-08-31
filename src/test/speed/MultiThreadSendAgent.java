package test.speed;

import java.util.ArrayList;

import org.eclipse.jetty.util.BlockingArrayQueue;

import kr.ac.uos.ai.arbi.agent.ArbiAgent;
import kr.ac.uos.ai.arbi.agent.ArbiAgentExecutor;

public class MultiThreadSendAgent extends ArbiAgent{
	private ArrayList<SendWork> workThreadList;
	private long startTime = 0;
	
	public MultiThreadSendAgent(int threadCount) {		
		
		ArbiAgentExecutor.execute("tcp://127.0.0.1:61616", "MultiThreadSendAgent", this,2);
		
		
		workThreadList = new ArrayList<SendWork>();
		generateThread(threadCount);
	}
	
	public void setStartTime() {
		this.startTime = System.currentTimeMillis();
	}
	
	public void startJob() {
		
		for(int i = 0; i < workThreadList.size();i++) {
			Thread t = new Thread(workThreadList.get(i));
			t.start();
		}
	}
	

	
	private void generateThread(int threadCount) {
		for(int i = 0; i < threadCount; i++) {
			SendWork sendWork = new SendWork(this,i);
			workThreadList.add(sendWork);
		}
	}
	
	class SendWork implements Runnable{
		private MultiThreadSendAgent agent;
		private String threadNum = "";
		
		
		public SendWork(MultiThreadSendAgent agent, int threadNum) {
			this.agent = agent;
			this.threadNum = Integer.toString(threadNum);
		}
		
		public synchronized void calculateTime() {
			float result = (System.currentTimeMillis() - startTime);
			result = result/1000;
			System.out.println("Test Finished : " +  result + "s");
		}
		
		@Override
		public void run() {
			for(int i = 0; i < 300; i = i + 1) {
				String sendString = "(testing \"thread" + threadNum + "\" " + i + " )";
				System.out.println("sending : " + sendString);
				String result = this.agent.query("receiver", sendString);
				
				System.out.println("result : " + result);
			}			
			
			calculateTime();
		}
		
	}
}
