package test.stability;

import java.util.ArrayList;
import java.util.Scanner;

import kr.ac.uos.ai.arbi.agent.ArbiAgent;
import kr.ac.uos.ai.arbi.agent.ArbiAgentExecutor;

public class ArbiCommTest extends ArbiAgent {

	String m_strName;
	
	public ArbiCommTest(int nID) {
		m_strName = "agent:test" + nID;
		ArbiAgentExecutor.execute("tcp://127.0.0.1:61616", m_strName, this, 2);
	}
	
	public String Delay(String strGL) {
//		try {
//			Thread.sleep(1000);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		System.out.println(strLogName + " <Delay END>");
		return strGL;
	}

	@Override
	public void onData(String sender, String data) {
		//System.out.println(m_strName + " >> onData:\t" + sender + " " + data);
		Delay(String.format("(response (sender \"%s\") (data \"%s\") (type \"%s\"))",
				m_strName, sender, data, "onData"));
	}
	
	@Override
	public String onQuery(String sender, String query) {
		//System.out.println(m_strName + " >> onQuery:\t" + sender + " " + query);
		return Delay(String.format("(response (sender \"%s\") (data \"%s\") (type \"%s\"))",
				m_strName, sender, query, "onQuery"));
	}
	
	@Override
	public String onRequest(String sender, String request) {
		//System.out.println(m_strName + " >> onRequest:\t" + sender + " " + request);
		return Delay(String.format("(response (sender \"%s\") (data \"%s\") (type \"%s\"))",
				m_strName, sender, request, "onRequest"));
	}
	
	@Override
	public void send(String receiver, String data) {
		
//		System.out.println(m_strName + " send: " + receiver + " " + data);
		super.send(receiver, data);
	}
	
	@Override
	public String query(String receiver, String query) {
//		System.out.println(m_strName + " query: " + receiver + " " + query);
		return super.query(receiver, query);
	}
	
	@Override
	public String request(String receiver, String request) {
//		System.out.println(m_strName + " request: " + receiver + " " + request);
		return super.request(receiver, request);
	}
	
	public String GetURI() { return m_strName; }
	
	@FunctionalInterface
	interface callFunc {
		public void call(String strThreadName);
	}
	
	static class A extends Thread {
		String m_strName;
		int m_nVal;
		callFunc m_func;
		long m_nCreateTime;
		
		public A(int nVal, callFunc func) {
			m_func = func;
			m_nVal = nVal;
//			m_strName = String.format("Thread #%d : %d", Thread.currentThread().getId(), nVal);
			m_nCreateTime = System.currentTimeMillis();
		}
		
		@Override
		public void run() {
			
			m_strName = String.format("Thread #%d : val-%02d", Thread.currentThread().getId(), m_nVal);
			//System.out.println(">> START " + m_strName);
			m_func.call(m_strName);
			//System.out.println("<< END   " + m_strName);
			System.out.println(String.format("%s [%d ~ %d]", m_strName, m_nCreateTime, System.currentTimeMillis()));
		}
		
		public A S() {
			super.start();
			return this;
		}
		
		public String GetName() { return m_strName; }
	}
	
	public static void Wait1Millsecond() {
		try {
			Thread.sleep(1);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner sc = new Scanner(System.in);
		
		ArbiCommTest a1 = new ArbiCommTest(1);
		ArbiCommTest a2 = new ArbiCommTest(2);
		ArbiCommTest a3 = new ArbiCommTest(3);
		
		ArrayList<A> listThread = new ArrayList<>();
		
		int nTemp;

		sc.nextLine();
		
		int nCnt = 1;
		for(int i = 0; i < 50; i++) {
			nTemp = nCnt++;
			listThread.add(new A(nTemp, (String str) -> {
				a2.send(a1.GetURI(), String.format("(msg (type \"send\") (content \"%s\")", str));
				}).S());

			nTemp = nCnt++;
			listThread.add(new A(nTemp, (String str) -> { 
				a3.send(a1.GetURI(), String.format("(msg (type \"send\") (content \"%s\")", str));
				}).S());
		}
		
		sc.nextLine();
		
		for(int i = 0; i < 50; i++) {
			nTemp = nCnt++;
			listThread.add(new A(nTemp, (String str) -> { a2.query(a1.GetURI(), String.format("(msg (type \"query\") (content \"%s\")", str)); }).S());

			nTemp = nCnt++;
			listThread.add(new A(nTemp, (String str) -> { a3.query(a1.GetURI(), String.format("(msg (type \"query\") (content \"%s\")", str)); }).S());
		}
		
		sc.nextLine();
		
		for(int i = 0; i < 50; i++) {
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
