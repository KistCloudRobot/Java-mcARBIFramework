package test;

import kr.ac.uos.ai.arbi.agent.ArbiAgent;
import kr.ac.uos.ai.arbi.agent.ArbiAgentExecutor;
import kr.ac.uos.ai.arbi.agent.datastream.DataStream;

public class StreamTest {
	
	
	public static void main(String[] args) {

		ArbiAgent agent = new ArbiAgent() {
			@Override
			public void onStream(String data) {
				System.out.println("stream data = " +data);
				super.onStream(data);
			}
		};
		ArbiAgentExecutor.execute("Agent1.xml", agent);
		
		ArbiAgent agent2 = new ArbiAgent() {
			
		};
		ArbiAgentExecutor.execute("Agent2.xml", agent2);
		
		ArbiAgent agent3 = new ArbiAgent() {
			
		};
		ArbiAgentExecutor.execute("Agent3.xml", agent3);
		
		String id = agent.requestStream("testAgent2", "(requestStream (url \"tcp://127.0.0.1:7770\") (rule (fact (q $b $c)) --> (stream (q $b $c))))");
		DataStream dataStream = agent2.getDataStream();
		
		dataStream.push("(fact (a \"b\" \"c\"))");
		dataStream.push("(fact (a \"b\" \"c\"))");
		dataStream.push("(fact (a \"b\" \"c\"))");
		dataStream.push("(fact (a \"b\" \"c\"))");
		dataStream.push("(fact (a \"b\" \"c\"))");
		dataStream.push("(fact (a \"b\" \"c\"))");
		dataStream.push("(fact (a \"b\" \"c\"))");
		dataStream.push("(fact (a \"b\" \"c\"))");
		dataStream.push("(fact (a \"b\" \"c\"))");
		dataStream.push("(fact (a \"b\" \"c\"))");
		dataStream.push("(fact (a \"b\" \"c\"))");
		
		dataStream.push("(fact (q \"b\" \"c\"))");
		dataStream.push("(fact (q \"b\" \"c\"))");
		dataStream.push("(fact (q \"b\" \"c\"))");
		dataStream.push("(fact (q \"b\" \"c\"))");
		dataStream.push("(fact (q \"b\" \"c\"))");
		
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		agent.releaseStream("testAgent2", id);

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		dataStream.push("(fact (q \"b\" \"c\"))");
		dataStream.push("(fact (q \"b\" \"c\"))");
		dataStream.push("(fact (q \"b\" \"c\"))");
		dataStream.push("(fact (q \"b\" \"c\"))");
		dataStream.push("(fact (q \"b\" \"c\"))");
		
//		agent3.push("(fact (q \"b\" \"c\"))");
//		agent3.push("(fact (q \"b\" \"c\"))");
//		agent3.push("(fact (q \"b\" \"c\"))");
//		agent3.push("(fact (q \"b\" \"c\"))");
//		agent3.push("(fact (q \"b\" \"c\"))");
		
	}

}
