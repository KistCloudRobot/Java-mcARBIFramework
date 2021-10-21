package test.demoServer;

import kr.ac.uos.ai.arbi.agent.ArbiAgent;
import kr.ac.uos.ai.arbi.agent.ArbiAgentExecutor;

public class AllocationTest extends ArbiAgent{

	private static String TAAddress = "agent://www.arbi.com/Local/TaskAllocator";
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
		String result = this.request(TAAddress, "(TaskAllocation \"StoringCarrier\" (goal (metadata \"goal1\") \"PalletTransported\" (argument \"RACK_LIFT1\" 1 18)))");
		

		//result = this.request(TAAddress, "(TaskAllocation \"UnstoringSmallCarrier\" (goal (metadata \"goal1\") \"MovingRackTransported\" (argument \"RACK_TOW1\" 20 22)))");

		
		System.out.println(result);
	}
	
	public String onQuery(String sender, String query){return "Ignored";}
	public static void main(String[] args) {
		ArbiAgentExecutor.execute("tcp://127.0.0.1:61316","agent://www.arbi.com/Local/TaskManager", new AllocationTest(), 2);	
		while(true);
	}
}
