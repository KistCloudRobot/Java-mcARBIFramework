package test.demoServer;

import java.util.Scanner;

import kr.ac.uos.ai.arbi.agent.ArbiAgent;
import kr.ac.uos.ai.arbi.agent.ArbiAgentExecutor;

public class LoadUnloadTest extends ArbiAgent{

	public void onStop(){}
	public String onRequest(String sender, String request){
		return "Ignored";
	}
	
	public void onData(String sender, String data){
		System.out.println("on data\t: " + data);
	}
	
	public void onNotify(String sender, String notification){
		System.out.println("on notify\t: " + notification);
	}
	
	public String onSubscribe(String sender, String subscribe){return "Ignored";}
	public void onUnsubscribe(String sender, String subID){}

	
	public void onStart(){
		Scanner s = new Scanner(System.in);
		System.out.println("response : " + this.request("agent://www.arbi.com/Lift1/BehaviorInterface", "(move (actionID \"1\") (path 201 213 205 206))"));
		// move to station1 
		s.nextLine();
		System.out.println("response : " + this.request("agent://www.arbi.com/Lift1/BehaviorInterface", "(load (actionID \"2\") 1)"));
		// load at station1
		s.nextLine();
		System.out.println("response : " + this.request("agent://www.arbi.com/Lift1/BehaviorInterface", "(unload (actionID \"3\") 1)"));
		// unload at station1
	}
	
	
	public String onQuery(String sender, String query){return "Ignored";}
	public static void main(String[] args) {
		ArbiAgentExecutor.execute("tcp://127.0.0.1:61116","agent://www.arbi.com/Lift1/TaskManager", new AllocationTest(), 2);	
		while(true);
	}
}
