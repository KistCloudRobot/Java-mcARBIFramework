package test;

import java.util.Scanner;

import kr.ac.uos.ai.arbi.BrokerType;
import kr.ac.uos.ai.arbi.agent.ArbiAgent;
import kr.ac.uos.ai.arbi.agent.ArbiAgentExecutor;
import kr.ac.uos.ai.arbi.ltm.DataSource;

public class AgentTest3 extends ArbiAgent{
	
	public void onStop(){}
	public String onRequest(String sender, String request){
		System.out.println(sender);
		System.out.println(request);
		return "Ignored";
	}
	
	public void onData(String sender, String data){
		System.out.println(data + " received!");
	}
	public String onSubscribe(String sender, String subscribe){return "Ignored";}
	public void onUnsubscribe(String sender, String subID){}
	public void onNotify(String sender, String notification){}
	
	public AgentTest3(){
	}
	
	
	
	
	public void onStart(){
		System.out.println("here");
		//this.request("agent://www.arbi.com/Lift2/BehaviorInterface", "(unload (actionID \"11\") 19)");
		DataSource ds = new DataSource();
		ds.connect("tcp://192.168.0.14:61313", "ds://www.arbi.com/Local/TestAgent2",BrokerType.ZEROMQ);
		//String result = this.query("noReceiver", "anyquery");
		//System.out.println("result : " + result);
		
		Scanner in = new Scanner(System.in);
		ds.assertFact("(context (PersonCall \"call001\" \"station19\" \"Storing\"))");

		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		in.nextLine();
		ds.assertFact("(context (PersonCall \"call002\" \"station22\" \"PrepareUnstoring\"))");
		
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		in.nextLine();
		ds.assertFact("(context (PersonCall \"call003\" \"station22\" \"Unstoring\"))");
		System.out.println("asserted");
		//this.send("agent://www.arbi.com/Local/TaskAllocator", "(testing)");
	}
	public String onQuery(String sender, String query){
		return "(ok)";
		
	}
	public static void main(String[] args) {
		ArbiAgentExecutor.execute("tcp://192.168.0.14:61313", "agent://www.arbi.com/Local/TestAgent2", new AgentTest3(), 2);
	}
}