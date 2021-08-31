package test.stability;

import kr.ac.uos.ai.arbi.agent.ArbiAgent;
import kr.ac.uos.ai.arbi.agent.ArbiAgentExecutor;

public class MassSendAgent extends ArbiAgent implements Runnable{
	private String id = "";
	private int count = 0;
	private AgentStabilityTestManager agentStabilityTestManager;
	
	public MassSendAgent(int num, AgentStabilityTestManager agentStabilityTestManager) {
		this.agentStabilityTestManager = agentStabilityTestManager;
		String id = "SendAgent" + num;
		this.id = Integer.toString(num);
		ArbiAgentExecutor.execute("tcp://127.0.0.1:61616",id, this,2);
	}
	
	
	public void sendAction() {
		
		
		String targetAgent = "ReceiveAgent" + this.id;
		for(int i = 1; i <= 300; i++) {
			String text = "(SendTest \"sendAgent\" " + id +" " + i + ")";
			//System.out.println(text);
			this.send(targetAgent, text);
			
		}
	}
	
	public void queryAction() {
		String targetAgent = "ReceiveAgent" + this.id;
		for(int i = 1; i <= 300; i++) {
			String text = "(SendTest \"sendAgent\" " + id + " i : " + i + " )";
			//System.out.println(text);
			String result = this.query(targetAgent, text);
			count++;
		}
		if(count == 300) {
			System.out.println("complete");
			agentStabilityTestManager.onFinish();
		}
	}


	@Override
	public void run() {
		this.queryAction();
		
	}
	
}
