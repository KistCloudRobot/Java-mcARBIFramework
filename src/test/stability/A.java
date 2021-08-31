package test.stability;

import test.stability.ArbiCommTest2.callFunc;

class A extends Thread {
	String m_strName;
	int m_nVal;
	callFunc m_func;
	long m_nCreateTime;
	
	public A(int nVal, callFunc func) {
		m_func = func;
		m_nVal = nVal;
//		m_strName = String.format("Thread #%d : %d", Thread.currentThread().getId(), nVal);
		m_nCreateTime = System.currentTimeMillis();
		System.out.println("new A : " + nVal);
	}
	
	@Override
	public void run() {
		m_strName = String.format("Thread #%d : val-%02d", Thread.currentThread().getId(), m_nVal);
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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