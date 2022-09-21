package test.demoServer;

import java.util.Scanner;

import kr.ac.uos.ai.arbi.BrokerType;
import kr.ac.uos.ai.arbi.agent.ArbiAgent;
import kr.ac.uos.ai.arbi.agent.ArbiAgentExecutor;
import kr.ac.uos.ai.arbi.ltm.DataSource;

public class AgentTest {
	
	private static class Lift1 extends ArbiAgent implements Runnable {
		private int seq;
		public Lift1() {
			seq = 0;
			new Thread(this).start();
		}
		
		@Override
		public void onData(String sender, String data) {
			System.out.println("[LIFT1]\ton data : " + data);

			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			switch(seq) {
			case 0:
				seq++;
				System.out.println("[LIFT1]\tload 22");
				System.out.println("[LIFT1]\t" + this.request("agent://www.arbi.com/Lift1/BehaviorInterface", "(load (actionID \"Lift1_2\") 22)"));
				break;
			case 1:
				seq++;
				System.out.println("[LIFT1]\tmove 228 14");
				System.out.println("[LIFT1]\t" + this.request("agent://www.arbi.com/Local/NavigationController", "(Move (actionID \"Lift1_3\") \"AMR_LIFT1\" 228 14)"));
				break;
			case 2:
				seq++;
				System.out.println("[LIFT1]\tunload 14");
				System.out.println("[LIFT1]\t" + this.request("agent://www.arbi.com/Lift1/BehaviorInterface", "(unload (actionID \"Lift1_4\") 14)"));
				break;
			case 3:
				seq++;
				System.out.println("[LIFT1]\tmove 221 101");
				System.out.println("[LIFT1]\t" + this.request("agent://www.arbi.com/Local/NavigationController", "(Move (actionID \"Lift1_5\") \"AMR_LIFT1\" 221 101)"));
				break;
			}
				
		}
		
		public void run() {
			ArbiAgentExecutor.execute("tcp://127.0.0.1:61116","agent://www.arbi.com/Lift1/TaskManager", this,2);	
		}
		
		public void onStart() {
			System.out.println("[LIFT1]\tmove 201 22");
			System.out.println("[LIFT1]\t" + this.request("agent://www.arbi.com/Local/NavigationController", "(Move (actionID \"Lift1_1\") \"AMR_LIFT1\" 201 22)"));
//			System.out.println(this.request("agent://www.arbi.com/Lift1/BehaviorInterface", "(unload (actionID \"Lift1_1\") 1)"));
		}
	}
	
	private static class Lift2 extends ArbiAgent implements Runnable {
		private int seq;
		public Lift2() {
			seq = 0;
			new Thread(this).start();
		}
		
		@Override
		public void onData(String sender, String data) {
			System.out.println("[LIFT2]\ton data : " + data);

			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			switch(seq) {
			case 0:
				seq++;
				System.out.println("[LIFT2]\tload 19");
				System.out.println("[LIFT2]\t" + this.request("agent://www.arbi.com/Lift2/BehaviorInterface", "(load (actionID \"Lift2_2\") 19)"));
				break;
			case 1:
				seq++;
				System.out.println("[LIFT2]\tmove 226 12");
				System.out.println("[LIFT2]\t" + this.request("agent://www.arbi.com/Local/NavigationController", "(Move (actionID \"Lift2_3\") \"AMR_LIFT2\" 226 12)"));
				break;
			case 2:
				seq++;
				System.out.println("[LIFT2]\tunload 12");
				System.out.println("[LIFT2]\t" + this.request("agent://www.arbi.com/Lift2/BehaviorInterface", "(unload (actionID \"Lift2_4\") 12)"));
				break;
			case 3:
				seq++;
				System.out.println("[LIFT2]\tmove 219 102");
				System.out.println("[LIFT2]\t" + this.request("agent://www.arbi.com/Local/NavigationController", "(Move (actionID \"Lift2_5\") \"AMR_LIFT2\" 219 102)"));
				break;
			}
				
		}
		
		public void run() {
			ArbiAgentExecutor.execute("tcp://127.0.0.1:61115","agent://www.arbi.com/Lift2/TaskManager", this,2);	
		}
		
		public void onStart() {
			System.out.println("[LIFT2]\tmove 202 19");
			System.out.println("[LIFT2]\t" + this.request("agent://www.arbi.com/Local/NavigationController", "(Move (actionID \"Lift2_1\") \"AMR_LIFT2\" 202 19)"));

		}
	}
	
	private static class Tow1 extends ArbiAgent implements Runnable {
		private int seq;
		
		public Tow1() {
			seq = 0;
			new Thread(this).start();
		}
		
		@Override
		public void onData(String sender, String data) {
			System.out.println("[TOW1]\ton data : " + data);

			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			switch(seq) {
			case 0:
				seq++;
				System.out.println("[TOW1]\tload 21");
				System.out.println("[TOW1]\t" + this.request("agent://www.arbi.com/Tow1/BehaviorInterface", "(load (actionID \"Tow1_2\") 21)"));
				break;
			case 1:
				seq++;
				System.out.println("[TOW1]\tmove 240 23");
				System.out.println("[TOW1]\t" + this.request("agent://www.arbi.com/Local/NavigationController", "(Move (actionID \"Tow1_3\") \"AMR_TOW1\" 240 23)"));
				break;
			case 2:
				seq++;
				System.out.println("[TOW1]\tunload 23");
				System.out.println("[TOW1]\t" + this.request("agent://www.arbi.com/Tow1/BehaviorInterface", "(unload (actionID \"Tow1_4\") 23)"));
				break;
			case 3:
				seq++;
				System.out.println("[TOW1]\tmove 229 103");
				System.out.println("[TOW1]\t" + this.request("agent://www.arbi.com/Local/NavigationController", "(Move (actionID \"Tow1_5\") \"AMR_TOW1\" 229 103)"));
				break;
			}
		}
		
		public void run() {
			ArbiAgentExecutor.execute("tcp://127.0.0.1:61114","agent://www.arbi.com/Tow1/TaskManager", this,2);	
		}
		
		public void onStart() {
			System.out.println("[TOW1]\tmove 203 21");
			System.out.println("[TOW1]\t" + this.request("agent://www.arbi.com/Local/NavigationController", "(Move (actionID \"Tow1_1\") \"AMR_TOW1\" 203 21)"));
		}
	}
	
	private static class Tow2 extends ArbiAgent implements Runnable {
		private int seq;
		
		public Tow2() {
			seq = 0;
			new Thread(this).start();
		}
		
		@Override
		public void onData(String sender, String data) {
			System.out.println("[TOW2]\ton data : " + data);

			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			switch(seq) {
			case 0:
				seq++;
				System.out.println("[TOW2]\tload 23");
				System.out.println("[TOW2]\t" + this.request("agent://www.arbi.com/Tow2/BehaviorInterface", "(load (actionID \"Tow2_2\") 23)"));
				break;
			case 1:
				seq++;
				System.out.println("[TOW2]\tmove 229 20");
				System.out.println("[TOW2]\t" + this.request("agent://www.arbi.com/Local/NavigationController", "(Move (actionID \"Tow2_3\") \"AMR_TOW2\" 229 20)"));
				break;
			case 2:
				seq++;
				System.out.println("[TOW2]\tunload 20");
				System.out.println("[TOW2]\t" + this.request("agent://www.arbi.com/Tow2/BehaviorInterface", "(unload (actionID \"Tow2_4\") 20)"));
				break;
			case 3:
				seq++;
				System.out.println("[TOW2]\tmove 239 104");
				System.out.println("[TOW2]\t" + this.request("agent://www.arbi.com/Local/NavigationController", "(Move (actionID \"Tow2_5\") \"AMR_TOW2\" 239 104)"));
				break;
			}
		}
		
		public void run() {
			ArbiAgentExecutor.execute("tcp://127.0.0.1:61412","agent://www.arbi.com/Tow2/TaskManager", this,2);	
		}
		
		public void onStart() {
			System.out.println("[TOW2]\tmove 204 23");
			System.out.println("[TOW2]\t" + this.request("agent://www.arbi.com/Local/NavigationController", "(Move (actionID \"Tow2_1\") \"AMR_TOW2\" 204 23)"));
		}
	}	
	
	public static void main(String[] args) throws InterruptedException {
		Thread.sleep(1000);
		new Lift1();
		Thread.sleep(1000);
		new Lift2();
		Thread.sleep(1000);
		new Tow1();
		Thread.sleep(1000);
		new Tow2();
	}
}