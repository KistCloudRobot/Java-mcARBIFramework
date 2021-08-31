package test.stability;

import java.util.LinkedList;
import java.util.Scanner;

public class LTMStabilityTestManager {
	public static void main(String[] ar) {
		new LTMStabilityTestManager();
	}
	
	public LTMStabilityTestManager() {
		LinkedList<MassAssertAgent> agentList = new LinkedList<MassAssertAgent>();
		LinkedList<MassSubscriberAgent> sAgentList = new LinkedList<MassSubscriberAgent>();
		
		for(int i = 0; i < 3; i++) {
			MassAssertAgent sAgent = new MassAssertAgent(i);
			System.out.println("????");
			MassSubscriberAgent ssAgent = new MassSubscriberAgent(i,"");
			System.out.println("????2");
			MassSubscriberAgent ss2Agent = new MassSubscriberAgent(i,"aa");
			System.out.println("????3");
			
			agentList.add(sAgent);
			sAgentList.add(ssAgent);
			sAgentList.add(ss2Agent);
		}
		System.out.println("waiting");
		Scanner sc  = new Scanner(System.in);
		sc.nextLine();
		LinkedList<Thread> tList = new LinkedList<Thread>();
		System.out.println("next");
		for(int i = 0; i < 3; i++) {
			Thread t = new Thread(agentList.get(i));
			tList.add(t);
			
		}
		
		for(int i = 0; i < 3; i++) {
			tList.get(i).start();
		}
		
		
	}
}
