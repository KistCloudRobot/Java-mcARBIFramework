package test.stability;

import kr.ac.uos.ai.arbi.Broker;
import kr.ac.uos.ai.arbi.agent.ArbiAgent;
import kr.ac.uos.ai.arbi.agent.ArbiAgentExecutor;
import kr.ac.uos.ai.arbi.ltm.DataSource;

public class MassAssertAgent extends ArbiAgent implements Runnable{
	private String id = "";
	private DataSource ds = null;
	
	
	public MassAssertAgent(int num) {
		String id = "assertAgent" + num;
		this.id = Integer.toString(num);
		ArbiAgentExecutor.execute("tcp://127.0.0.1:61616",id, this,2);
		ds = new DataSource();
		ds.connect("tcp://127.0.0.1:61616", "assertAgentDS" + this.id ,Broker.ZEROMQ);
		
		
	}
	
	
	public void assertAction() {
		System.out.println("starting: ");
		for(int i = 1; i <= 100; i++) {
			//String text = "(assertAction " + id + " " + i + ")";
			String text = "(assertAction \"" + id + "\" " + i + ")";
			System.out.println("running :" + text);
			ds.assertFact(text);
			
		}
	}
	
	public static void main(String[] ar) {
		MassAssertAgent a = new MassAssertAgent(1);
		a.assertAction();
	}
	
	

	@Override
	public void run() {
		this.assertAction();
	}
}
