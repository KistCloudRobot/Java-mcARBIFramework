package test.timeget;

import kr.ac.uos.ai.arbi.Broker;
import kr.ac.uos.ai.arbi.agent.ArbiAgent;
import kr.ac.uos.ai.arbi.agent.ArbiAgentExecutor;
import kr.ac.uos.ai.arbi.ltm.DataSource;
import test.AgentTest;

public class TimeGetTest extends ArbiAgent{

	public void onStop(){}
	public String onRequest(String sender, String request){return "Ignored";}
	public void onData(String sender, String data){
		
		
		
		System.out.println(data + " received!");
	}
	public String onSubscribe(String sender, String subscribe){return "Ignored";}
	public void onUnsubscribe(String sender, String subID){}
	public void onNotify(String sender, String notification){}
	
	public TimeGetTest(){
		ArbiAgentExecutor.execute("tcp://127.0.0.1:61616","agent://www.arbi.com/TestAgent", this,2);
	
		System.out.println("agent1");
	}
	
	public void onStart(){
		System.out.println("start");
		
		System.out.println("??????");
		
		DataSource ds = new DataSource();
		System.out.println("started");
		ds.connect("tcp://127.0.0.1:61616", "timegetTest",Broker.ZEROMQ);
		
		ds.assertFact("(testFact \"testing\")");
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(System.currentTimeMillis());
		String result = ds.getLastModifiedTime("(testFact \"testing\")");
		System.out.println("?????" + result);
	}
	
	public String onQuery(String sender, String query){
		System.out.println("sender sent message : " + query);
		
		return "(ok)";
		
	}
	public static void main(String[] args) {
		new TimeGetTest();
	}
}
