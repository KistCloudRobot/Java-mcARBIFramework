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
		ArbiAgentExecutor.execute("tcp://172.16.165.135:61116","agent://www.arbi.com/Lift1/TestAgent", this,2);
		this.send("agent://www.arbi.com/Lift1/TestAgent", "test");
	}
	
	public void onStart(){
		System.out.println("start");
		
		/*
		DataSource dc = new DataSource();
		dc.connect("tcp://localhost:5671", "ds://www.arbi.com/TaskManager", Broker.ZEROMQ);
		dc.assertFact("(Robot_position (time 467015366.818170342) (position 8.2118526332695385 -9.5512692050022654))");
		System.out.println(dc.retrieveFact("(Robot_position $time $position)"));
		 */
		
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