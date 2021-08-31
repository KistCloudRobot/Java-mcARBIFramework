package test.stability;

import java.util.ArrayList;
import java.util.Scanner;

public class MixedStabilityTestManager {
	public static void main(String[] ar) {
		new MixedStabilityTestManager();
	}
	
	
	public MixedStabilityTestManager() {
		Scanner sc = new Scanner(System.in);
		
		ArbiCommTest2 a1 = new ArbiCommTest2(1);
		ArbiCommTest2 a2 = new ArbiCommTest2(2);
		ArbiCommTest2 a3 = new ArbiCommTest2(3);
		
		ArrayList<A> listThread = new ArrayList<>();
		
		int nTemp;

		sc.nextLine();
		
		int nCnt = 1;
		for(int i = 0; i < 5; i++) {
			nTemp = nCnt++;
			listThread.add(new A(nTemp, (String str) -> { a2.send(a1.GetURI(), String.format("(msg (type \"send\") (content \"%s\")", str)); }));

			nTemp = nCnt++;
			listThread.add(new A(nTemp, (String str) -> { a3.send(a1.GetURI(), String.format("(msg (type \"send\") (content \"%s\")", str)); }));
		}
		
		sc.nextLine();
		
		for(int i = 0; i < 10; i = i + 2) {
			//nTemp = nCnt++;
			listThread.get(i).S();

			//nTemp = nCnt++;
			listThread.get(i+1).S();
		}
		
		sc.nextLine();
		
		
		for(int i = 0; i < 5; i++) {
			nTemp = nCnt++;
			listThread.add(new A(nTemp, (String str) -> { a2.query(a1.GetURI(), String.format("(msg (type \"query\") (content \"%s\")", str)); }).S());

			nTemp = nCnt++;
			listThread.add(new A(nTemp, (String str) -> { a3.query(a1.GetURI(), String.format("(msg (type \"query\") (content \"%s\")", str)); }).S());
		}
		
		sc.nextLine();
		
		for(int i = 0; i < 5; i++) {
			nTemp = nCnt++;
			listThread.add(new A(nTemp, (String str) -> { a2.request(a1.GetURI(), String.format("(msg (type \"request\") (content \"%s\")", str)); }).S());

			nTemp = nCnt++;
			listThread.add(new A(nTemp, (String str) -> { a3.request(a1.GetURI(), String.format("(msg (type \"request\") (content \"%s\")", str)); }).S());
		}
		
		sc.nextLine();

		System.out.println("-------------------------------------------------");
		
		boolean bEnd = false;
		
		while(!bEnd) {
			bEnd = true;
			sc.nextLine();
			for(A a : listThread) {
				if(a.isAlive()) {
					bEnd = false;
					System.err.println(" + " + a.GetName());
				}
			}
			System.out.println("-------------------------------------------------");
		}
		
		a1.close();
		a2.close();
		a3.close();
		
		System.err.println("-------------------------------------------------");
	}
}
