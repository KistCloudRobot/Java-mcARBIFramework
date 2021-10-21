package test.demoServer;

import java.util.Scanner;

import kr.ac.uos.ai.arbi.Broker;
import kr.ac.uos.ai.arbi.agent.ArbiAgent;
import kr.ac.uos.ai.arbi.agent.ArbiAgentExecutor;
import kr.ac.uos.ai.arbi.ltm.DataSource;

public class AgentTest {
	private static class Lift1 extends ArbiAgent implements Runnable {
		public Lift1() {
			new Thread(this).start();
		}
		
		public void run() {
			ArbiAgentExecutor.execute("tcp://127.0.0.1:61116","agent://www.arbi.com/Lift1/TaskManager", this,2);	
		}
		
		public void onStart() {
			System.out.println(this.request("agent://www.arbi.com/Local/NavigationController", "(Move (actionID \"Lift1_1\") \"AMR_LIFT1\" 216 206)"));
		}
	}
	
	private static class Lift2 extends ArbiAgent implements Runnable {
		public Lift2() {
			new Thread(this).start();
		}
		
		public void run() {
			ArbiAgentExecutor.execute("tcp://127.0.0.1:61115","agent://www.arbi.com/Lift2/TaskManager", this,2);	
		}
		
		public void onStart() {
			System.out.println(this.request("agent://www.arbi.com/Local/NavigationController", "(Move (actionID \"Lift2_1\") \"AMR_LIFT2\" 202 205)"));
		}
	}
	
	private static class Tow1 extends ArbiAgent implements Runnable {
		public Tow1() {
			new Thread(this).start();
		}
		
		public void run() {
			ArbiAgentExecutor.execute("tcp://127.0.0.1:61114","agent://www.arbi.com/Tow1/TaskManager", this,2);	
		}
		
		public void onStart() {
			System.out.println(this.request("agent://www.arbi.com/Local/NavigationController", "(Move (actionID \"Tow1_1\") \"AMR_TOW1\" 234 239)"));
		}
	}
	
	private static class Tow2 extends ArbiAgent implements Runnable {
		public Tow2() {
			new Thread(this).start();
		}
		
		public void run() {
			ArbiAgentExecutor.execute("tcp://127.0.0.1:61113","agent://www.arbi.com/Tow2/TaskManager", this,2);	
		}
		
		public void onStart() {
			System.out.println(this.request("agent://www.arbi.com/Local/NavigationController", "(Move (actionID \"Tow2_1\") \"AMR_TOW2\" 239 234)"));
		}
	}
	
	public static void main(String[] args) throws InterruptedException {
		new Tow2();
		while(true);
	}
}