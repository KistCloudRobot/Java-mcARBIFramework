package test;

import kr.ac.uos.ai.arbi.Broker;
import kr.ac.uos.ai.arbi.agent.ArbiAgent;
import kr.ac.uos.ai.arbi.agent.ArbiAgentExecutor;
import kr.ac.uos.ai.arbi.ltm.DataSource;

public class AgentTest2 extends ArbiAgent{
	
	public void onStop(){}
	
	public String onQuery(String sender, String query){return "Ignored";}
	public String onSubscribe(String sender, String subscribe){return "Ignored";}
	public void onUnsubscribe(String sender, String subID){}
	public void onNotify(String sender, String notification){}
	
	AgentTest2(){
		ArbiAgentExecutor.execute("tcp://127.0.0.1:61616","testAgent2", this,2);
		System.out.println("agent2");
	}
	
	public void onStart(){
		/*
		
		DataSource dc = new DataSource();
		dc.connect("tcp://localhost:5671", "dc://testdc2",Broker.ZEROMQ);
		System.out.println(dc.retrieveFact("(Robot_position $time $position)"));
		System.out.println(dc.match("(Robot_position $time $position)"));
		*/
	}
	
	public String onRequest(String sender, String request){
		System.out.println("ok" + " " + request + " " + sender);
		return "null";
	}
	
	public void onData(String sender, String data) {
		System.out.println("start " + data);
		System.out.println(Thread.currentThread().getId());
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("data end");
		
	}
	
	public static void main(String[] args) {
		new AgentTest2();
	}
}
