package test;

import kr.ac.uos.ai.arbi.Broker;
import kr.ac.uos.ai.arbi.agent.ArbiAgent;
import kr.ac.uos.ai.arbi.agent.ArbiAgentExecutor;
import kr.ac.uos.ai.arbi.ltm.DataSource;

public class AgentTest extends ArbiAgent{
	
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
	
	public AgentTest(){
	}
	
	
	
	
	public void onStart(){
		System.out.println("here");
		this.send("agent://www.arbi.com/Local/TestAgent", "(hi)");
		System.out.println(this.query("agent://www.arbi.com/Local/TestAgent", "(hi)"));
	}
	public String onQuery(String sender, String query){
		return "(ok)";
		
	}
	public static void main(String[] args) {
		ArbiAgentExecutor.execute("tcp://172.16.165.204:61313", "agent://www.arbi.com/Local/TestAgent", new AgentTest(), 2);
	}
}