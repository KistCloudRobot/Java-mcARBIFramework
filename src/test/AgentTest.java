package test;

import kr.ac.uos.ai.arbi.BrokerType;
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
		//this.request("agent://www.arbi.com/Lift2/BehaviorInterface", "(unload (actionID \"11\") 19)");
		DataSource ds = new DataSource();
		ds.connect("tcp://127.0.0.1:61316", "ds://www.arbi.com/Local/TestAgent2",BrokerType.ZEROMQ);
		//String result = this.query("noReceiver", "anyquery");
		//System.out.println("result : " + result);
		ds.assertFact("(context (PersonCall \"call01\" \"http://www.arbi.com/ontologies/arbi.owl#station1\" \"Storing\"))");
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		ds.assertFact("(context (PersonCall \"call02\" \"http://www.arbi.com/ontologies/arbi.owl#station4\" \"PrepareUnstoring\"))");

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		//ds.assertFact("(context (PersonCall \"call03\" \"http://www.arbi.com/ontologies/arbi.owl#station3\" \"PrepareUnstoring\"))");

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		ds.assertFact("(context (PersonCall \"call04\" \"http://www.arbi.com/ontologies/arbi.owl#station2\" \"PrepareStoring\"))");
	}
	public String onQuery(String sender, String query){
		return "(ok)";
		
	}
	public static void main(String[] args) {
		ArbiAgentExecutor.execute("tcp://127.0.0.1:61316", "agent://www.arbi.com/Local/TestAgent2", new AgentTest(), 2);
	}
}