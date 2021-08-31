package test.speed;

public class AgentSpeedTest {
	public static void main(String[] ar) {
		new AgentSpeedTest();
	}
	
	public AgentSpeedTest() {
		ReceiveAgent agent = new ReceiveAgent();
		MultiThreadSendAgent sendAgent = new MultiThreadSendAgent(10);
		sendAgent.setStartTime();
		sendAgent.startJob();
		
	}
	
	
}
