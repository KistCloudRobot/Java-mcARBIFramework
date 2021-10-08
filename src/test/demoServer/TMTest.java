package test.demoServer;

import kr.ac.uos.ai.arbi.agent.ArbiAgent;
import kr.ac.uos.ai.arbi.agent.ArbiAgentExecutor;
import kr.ac.uos.ai.arbi.framework.ArbiFrameworkServer;

public class TMTest extends ArbiAgent{


	private static String NCAddress = "agent://www.arbi.com/Local/NavigationController";
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
	
	@Override
	public void onStart() {
		String result1 = this.request(NCAddress, "(Move (actionID \"Lift1_1\") \"AMR_LIFT1\" 209 213)");

		// goal request message (Move (actionID $id) $robotID $start $end)
		// goal response message (MoveResult (actionID $id) $robotID $result))
		// $result is "success" or "fail"
		
		System.out.println(result1);
		String result2 = this.request(NCAddress, "(Move (actionID \"Lift2_1\") \"AMR_LIFT2\" 222 229)");

		System.out.println(result2);
	}
	
	public String onQuery(String sender, String query){return "Ignored";}
	public static void main(String[] args) {
		ArbiAgentExecutor.execute("tcp://127.0.0.1:61116","agent://www.arbi.com/Lift1/TaskManager", new TMTest(), 2);	
		while(true);
	}
}
