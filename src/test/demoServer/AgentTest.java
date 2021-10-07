package test.demoServer;

import java.util.Scanner;

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
		ArbiAgentExecutor.execute("tcp://127.0.0.1:61116","agent://www.arbi.com/Lift1/TestAgent", this,2);	
	}
	
	
	
	
	public void onStart(){
		Scanner s = new Scanner(System.in);
		System.out.println("response : " + this.request("agent://www.arbi.com/Lift1/BehaviorInterface", "(move (actionID \"1\") (path 201 213 205 206))"));
		s.nextLine();
		System.out.println("response : " + this.request("agent://www.arbi.com/Lift1/BehaviorInterface", "(load (actionID \"2\") 1)"));
		s.nextLine();
		System.out.println("response : " + this.request("agent://www.arbi.com/Lift1/BehaviorInterface", "(unload (actionID \"3\") 1)"));
		
		
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