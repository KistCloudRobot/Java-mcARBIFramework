package test.stability;

import java.util.ArrayList;
import java.util.Scanner;

import kr.ac.uos.ai.arbi.agent.ArbiAgent;
import kr.ac.uos.ai.arbi.agent.ArbiAgentExecutor;

public class ArbiCommTest2 extends ArbiAgent {

	String m_strName;
	
	public ArbiCommTest2(int nID) {
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
//		System.out.println(m_strName + " >> onData:\t" + sender + " " + data);
		Delay(String.format("(response (sender \"%s\") (data \"%s\") (type \"%s\"))",
				m_strName, sender, data, "onData"));
	}
	
	@Override
	public String onQuery(String sender, String query) {
//		System.out.println(m_strName + " >> onQuery:\t" + sender + " " + query);
		return Delay(String.format("(response (sender \"%s\") (data \"%s\") (type \"%s\"))",
				m_strName, sender, query, "onQuery"));
	}
	
	@Override
	public String onRequest(String sender, String request) {
//		System.out.println(m_strName + " >> onRequest:\t" + sender + " " + request);
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
	
	
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
	}
}
