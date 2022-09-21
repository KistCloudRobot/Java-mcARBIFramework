package test.demoServer;

import kr.ac.uos.ai.arbi.BrokerType;
import kr.ac.uos.ai.arbi.agent.ArbiAgent;
import kr.ac.uos.ai.arbi.agent.ArbiAgentExecutor;
import kr.ac.uos.ai.arbi.framework.ArbiFrameworkServer;
import kr.ac.uos.ai.arbi.ltm.DataSource;

public class Dummy extends ArbiAgent{
	
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
	
	public Dummy(){}
	
	
	
	
	public void onStart(){}
	public String onQuery(String sender, String query){return "Ignored";}
	public static void main(String[] args) {
//		new ArbiFrameworkServer(2, "Lift1").start("tcp://172.16.165.204:61616", "tcp://172.16.165.204:61116");
//		new ArbiFrameworkServer(2, "Lift2").start("tcp://172.16.165.204:61616", "tcp://172.16.165.204:61115");
//		new ArbiFrameworkServer(2, "Tow1").start("tcp://172.16.165.204:61616", "tcp://172.16.165.204:61114");
//		new ArbiFrameworkServer(2, "Tow2").start("tcp://172.16.165.204:61616", "tcp://172.16.165.204:61113");
//		ArbiAgentExecutor.execute("tcp://127.0.0.1:61316","agent://www.arbi.com/Local/ContextManager", new Dummy(), 2);	
		ArbiAgentExecutor.execute("tcp://127.0.0.1:61116","agent://www.arbi.com/Lift1/ContextManager", new Dummy(), 2);	
		ArbiAgentExecutor.execute("tcp://127.0.0.1:61115","agent://www.arbi.com/Lift2/ContextManager", new Dummy(), 2);	
		ArbiAgentExecutor.execute("tcp://127.0.0.1:61114","agent://www.arbi.com/Tow1/ContextManager", new Dummy(), 2);	
		ArbiAgentExecutor.execute("tcp://127.0.0.1:61113","agent://www.arbi.com/Tow2/ContextManager", new Dummy(), 2);	
	}
}