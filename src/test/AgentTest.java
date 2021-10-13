package test;

import kr.ac.uos.ai.arbi.Broker;
import kr.ac.uos.ai.arbi.agent.ArbiAgent;
import kr.ac.uos.ai.arbi.agent.ArbiAgentExecutor;
import kr.ac.uos.ai.arbi.ltm.DataSource;

public class AgentTest extends ArbiAgent{
	
	public void onStop(){}
	public String onRequest(String sender, String request){return "Ignored";}
	
	public void onData(String sender, String data){
		System.out.println(data + " received!");
	}
	public String onSubscribe(String sender, String subscribe){return "Ignored";}
	public void onUnsubscribe(String sender, String subID){}
	public void onNotify(String sender, String notification){}
	
	public AgentTest(){
		ArbiAgentExecutor.execute("tcp://127.0.0.1:61114","agent://www.arbi.com/Tow1/TestAgent", this,2);	
		
		System.out.println("start");

		this.send("agent://www.arbi.com/BehaviorInterface", "test");
	
		DataSource dc = new DataSource();
		dc.connect("tcp://127.0.0.1:61114", "ds://www.arbi.com/testAgent", Broker.ZEROMQ);
		
		
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		dc.assertFact("(Robot_position (time 467015366.818170342) (position 8.2118526332695385 -9.5512692050022654))");
		System.out.println(dc.retrieveFact("(Robot_position $time $position)"));
	}
	
	
	
	
	public void onStart(){
		
		
		
	}
	public String onQuery(String sender, String query){
		System.out.println("sender sent message : " + query);
		
		return "(ok)";
		
	}
	public static void main(String[] args) {
		new AgentTest();
		while(true);
	}
}